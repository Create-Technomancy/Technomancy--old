package dev.Cosmos616.technomancy.content.contraptions.base;

import com.jozufozu.flywheel.backend.instancing.InstancedRenderDispatcher;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.contraptions.relays.elementary.ICogWheel;
import com.simibubi.create.content.contraptions.relays.gearbox.GearboxBlock;
import com.simibubi.create.foundation.sound.SoundScapes;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.foundation.block.BlockLoadValues;
import dev.Cosmos616.technomancy.foundation.energy.EmissionPropagator;
import dev.Cosmos616.technomancy.foundation.energy.IConduct;
import dev.Cosmos616.technomancy.foundation.energy.MagicNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.List;

public class TechnomaticTileEntity extends SmartTileEntity implements IHaveGoggleInformation, IHaveHoveringInformation {

    public @Nullable Long network;
    public @Nullable BlockPos source;
    public boolean networkDirty;
    public boolean updateEmission;
    public int preventEmissionUpdate;

//    protected MagicEffectHandler effects;
    protected float emission;
    protected float capacity;
    protected float load;
    protected boolean overLoaded;
    protected boolean wasMoved;

    private int flickerTally;
    private int networkSize;
    private int validationCountdown;
    protected float lastLoadApplied;
    protected float lastCapacityProvided;

    public TechnomaticTileEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
//        effects = new MagicEffectHandler(this);
        updateEmission = true;
    }

    @Override
    public void initialize() {
        if (hasNetwork() && !level.isClientSide) {
            MagicNetwork network = getOrCreateNetwork();
            if (!network.initialized)
                network.initFromTE(capacity, load, networkSize);
            network.addSilently(this, lastCapacityProvided, lastLoadApplied);
        }

        super.initialize();
    }

    @Override
    public void tick() {
        if (!level.isClientSide && needsEmissionUpdate())
            attachMagics();

        super.tick();
//        effects.tick();

        preventEmissionUpdate = 0;

        if (level.isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::tickAudio);
            return;
        }

        if (validationCountdown-- <= 0) {
            // TODO: Replace with config
//            validationCountdown = TMConfigs.SERVER.magics.magicValidationFrequency.get();
            validationCountdown = 60;
            validateMagics();
        }

        if (getFlickerScore() > 0)
            flickerTally = getFlickerScore() - 1;

        if (networkDirty) {
            if (hasNetwork())
                getOrCreateNetwork().updateNetwork();
            networkDirty = false;
        }
    }

    private void validateMagics() {
        if (hasSource()) {
            if (!hasNetwork()) {
                removeSource();
                return;
            }

            if (!level.isLoaded(source))
                return;

            BlockEntity tileEntity = level.getBlockEntity(source);
            TechnomaticTileEntity sourceTe =
                    tileEntity instanceof TechnomaticTileEntity ? (TechnomaticTileEntity) tileEntity : null;
            if (sourceTe == null || sourceTe.emission == 0) {
                removeSource();
                detachMagics();
                return;
            }

            return;
        }

        if (emission != 0) {
            if (getGeneratedEmission() == 0)
                emission = 0;
        }
    }

    public void updateFromNetwork(float maxLoad, float currentLoad, int networkSize) {
        networkDirty = false;
        this.capacity = maxLoad;
        this.load = currentLoad;
        this.networkSize = networkSize;
        boolean overLoaded = maxLoad < currentLoad && IConduct.Load.isEnabled();
        setChanged();

        if (overLoaded != this.overLoaded) {
            float prevEmission = getEmission();
            this.overLoaded = overLoaded;
            onEmissionChanged(prevEmission);
            sendData();
        }
    }

    protected Block getLoadConfigKey() {
        return getBlockState().getBlock();
    }

    public float calculateLoadApplied() {
        float load = (float) BlockLoadValues.getLoad(getLoadConfigKey());
        this.lastLoadApplied = load;
        return load;
    }

    public float calculateAddedLoadCapacity() {
        float capacity = (float) BlockLoadValues.getCapacity(getLoadConfigKey());
        this.lastCapacityProvided = capacity;
        return capacity;
    }

    public void onEmissionChanged(float previousEmission) {
        boolean fromOrToZero = (previousEmission == 0) != (getEmission() == 0);
        boolean directionSwap = !fromOrToZero && Math.signum(previousEmission) != Math.signum(getEmission());
        if (fromOrToZero || directionSwap)
            flickerTally = getFlickerScore() + 5;
        setChanged();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    protected void setRemovedNotDueToChunkUnload() {
        if (!level.isClientSide) {
            if (hasNetwork())
                getOrCreateNetwork().remove(this);
            detachMagics();
        }
        super.setRemovedNotDueToChunkUnload();
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putFloat("Emission", emission);

        if (needsEmissionUpdate())
            compound.putBoolean("NeedsEmissionUpdate", true);

        if (hasSource())
            compound.put("Source", NbtUtils.writeBlockPos(source));

        if (hasNetwork()) {
            CompoundTag networkTag = new CompoundTag();
            networkTag.putLong("Id", this.network);
            networkTag.putFloat("Load", load);
            networkTag.putFloat("Capacity", capacity);
            networkTag.putInt("Size", networkSize);

            if (lastLoadApplied != 0)
                networkTag.putFloat("AddedLoad", lastLoadApplied);
            if (lastCapacityProvided != 0)
                networkTag.putFloat("AddedCapacity", lastCapacityProvided);

            compound.put("Network", networkTag);
        }

        super.write(compound, clientPacket);
    }

    public boolean needsEmissionUpdate() {
        return updateEmission;
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        boolean overLoadedBefore = overLoaded;
        clearMagicInformation();

        // DO NOT READ magic information when placed after movement
        if (wasMoved) {
            super.read(compound, clientPacket);
            return;
        }

        emission = compound.getFloat("Emission");

        if (compound.contains("Source"))
            source = NbtUtils.readBlockPos(compound.getCompound("Source"));

        if (compound.contains("Network")) {
            CompoundTag networkTag = compound.getCompound("Network");
            network = networkTag.getLong("Id");
            load = networkTag.getFloat("Load");
            capacity = networkTag.getFloat("Capacity");
            networkSize = networkTag.getInt("Size");
            lastLoadApplied = networkTag.getFloat("AddedLoad");
            lastCapacityProvided = networkTag.getFloat("AddedCapacity");
            overLoaded = capacity < load && IConduct.Load.isEnabled();
        }

        super.read(compound, clientPacket);

//        if (clientPacket && overLoadedBefore != overLoaded && emission != 0)
//            effects.triggerOverLoadedEffect();

        if (clientPacket)
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> InstancedRenderDispatcher.enqueueUpdate(this));
    }

    public float getGeneratedEmission() {
        return 0;
    }

    public boolean isSource() {
        return getGeneratedEmission() != 0;
    }

    public float getEmission() {
        if (overLoaded)
            return 0;
        return getTheoreticalEmission();
    }

    public float getTheoreticalEmission() {
        return emission;
    }

    public void setEmission(float emission) {
        this.emission = emission;
    }

    public boolean hasSource() {
        return source != null;
    }

    public void setSource(BlockPos source) {
        this.source = source;
        if (level == null || level.isClientSide)
            return;

        BlockEntity tileEntity = level.getBlockEntity(source);
        if (!(tileEntity instanceof TechnomaticTileEntity)) {
            removeSource();
            return;
        }

        TechnomaticTileEntity sourceTe = (TechnomaticTileEntity) tileEntity;
        setNetwork(sourceTe.network);
    }

    public void removeSource() {
        float prevEmission = getEmission();

        emission = 0;
        source = null;
        setNetwork(null);

        onEmissionChanged(prevEmission);
    }

    public void setNetwork(@Nullable Long networkIn) {
        if (network == networkIn)
            return;
        if (network != null)
            getOrCreateNetwork().remove(this);

        network = networkIn;
        setChanged();

        if (networkIn == null)
            return;

        network = networkIn;
        MagicNetwork network = getOrCreateNetwork();
        network.initialized = true;
        network.add(this);
    }

    public MagicNetwork getOrCreateNetwork() {
        return Technomancy.QUANTA_PROPAGATOR.getOrCreateNetworkFor(this);
    }

    public boolean hasNetwork() {
        return network != null;
    }

    public void attachMagics() {
        updateEmission = false;
        EmissionPropagator.handleAdded(level, worldPosition, this);
    }

    public void detachMagics() {
        EmissionPropagator.handleRemoved(level, worldPosition, this);
    }

    public boolean isEmissionRequirementFulfilled() {
        BlockState state = getBlockState();
        if (!(getBlockState().getBlock() instanceof IConduct))
            return true;
        IConduct def = (IConduct) state.getBlock();
        IConduct.EmissionLevel minimumRequiredEmissionLevel = def.getMinimumRequiredEmissionLevel();
        return Math.abs(getEmission()) >= minimumRequiredEmissionLevel.getEmissionValue();
    }

    public static void switchToBlockState(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide)
            return;

        BlockEntity tileEntityIn = world.getBlockEntity(pos);
        BlockState currentState = world.getBlockState(pos);
        boolean isMagic = tileEntityIn instanceof TechnomaticTileEntity;

        if (currentState == state)
            return;
        if (tileEntityIn == null || !isMagic) {
            world.setBlock(pos, state, 3);
            return;
        }

        TechnomaticTileEntity tileEntity = (TechnomaticTileEntity) tileEntityIn;
        if (state.getBlock() instanceof TechnomaticBlock
                && !((TechnomaticBlock) state.getBlock()).areStatesMagicallyEquivalent(currentState, state)) {
            if (tileEntity.hasNetwork())
                tileEntity.getOrCreateNetwork()
                        .remove(tileEntity);
            tileEntity.detachMagics();
            tileEntity.removeSource();
        }

        world.setBlock(pos, state, 3);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {}

//    @Override
//    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
//        boolean notFastEnough = !isEmissionRequirementFulfilled() && getEmission() != 0;
//
//        if (overLoaded && AllConfigs.CLIENT.enableOverstressedTooltip.get()) {
//            Lang.translate("gui.stressometer.overstressed")
//                    .style(GOLD)
//                    .forGoggles(tooltip);
//            Component hint = Lang.translateDirect("gui.contraptions.network_overstressed");
//            List<Component> cutString = TooltipHelper.cutTextComponent(hint, GRAY, ChatFormatting.WHITE);
//            for (int i = 0; i < cutString.size(); i++)
//                Lang.builder()
//                        .add(cutString.get(i)
//                                .copy())
//                        .forGoggles(tooltip);
//            return true;
//        }
//
//        if (notFastEnough) {
//            Lang.translate("tooltip.speedRequirement")
//                    .style(GOLD)
//                    .forGoggles(tooltip);
//            MutableComponent hint =
//                    Lang.translateDirect("gui.contraptions.not_fast_enough", I18n.get(getBlockState().getBlock()
//                            .getDescriptionId()));
//            List<Component> cutString = TooltipHelper.cutTextComponent(hint, GRAY, ChatFormatting.WHITE);
//            for (int i = 0; i < cutString.size(); i++)
//                Lang.builder()
//                        .add(cutString.get(i)
//                                .copy())
//                        .forGoggles(tooltip);
//            return true;
//        }
//
//        return false;
//    }
//
//    @Override
//    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
//        boolean added = false;
//
//        if (!IRotate.StressImpact.isEnabled())
//            return added;
//        float stressAtBase = calculateStressApplied();
//        if (Mth.equal(stressAtBase, 0))
//            return added;
//
//        Lang.translate("gui.goggles.kinetic_stats")
//                .forGoggles(tooltip);
//
//        addStressImpactStats(tooltip, stressAtBase);
//
//        return true;
//
//    }

//    protected void addLoadStats(List<Component> tooltip, float stressAtBase) {
//        Lang.translate("tooltip.stressImpact")
//                .style(GRAY)
//                .forGoggles(tooltip);
//
//        float stressTotal = stressAtBase * Math.abs(getTheoreticalSpeed());
//
//        Lang.number(stressTotal)
//                .translate("generic.unit.stress")
//                .style(ChatFormatting.AQUA)
//                .space()
//                .add(Lang.translate("gui.goggles.at_current_speed")
//                        .style(ChatFormatting.DARK_GRAY))
//                .forGoggles(tooltip, 1);
//    }

    public void clearMagicInformation() {
        emission = 0;
        source = null;
        network = null;
        overLoaded = false;
        load = 0;
        capacity = 0;
        lastLoadApplied = 0;
        lastCapacityProvided = 0;
    }

    public void warnOfMovement() {
        wasMoved = true;
    }

    public int getFlickerScore() {
        return flickerTally;
    }

//    public static float convertToDirection(float axisSpeed, Direction d) {
//        return d.getAxisDirection() == Direction.AxisDirection.POSITIVE ? axisSpeed : -axisSpeed;
//    }

//    public static float convertToLinear(float speed) {
//        return speed / 512f;
//    }

//    public static float convertToAngular(float speed) {
//        return speed * 3 / 10f;
//    }

    public boolean isOverLoaded() {
        return overLoaded;
    }

    // Custom Propagation

    public float propagateEmissionTo(TechnomaticTileEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff,
                                     boolean connectedViaAxes, boolean connectedViaCogs) {
        return 0;
    }

    public List<BlockPos> addPropagationLocations(IConduct block, BlockState state, List<BlockPos> neighbours) {
//        if (!canPropagateDiagonally(block, state))
//            return neighbours;
//
//        Direction.Axis axis = block.getRotationAxis(state);
//        BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1))
//                .forEach(offset -> {
//                    if (axis.choose(offset.getX(), offset.getY(), offset.getZ()) != 0)
//                        return;
//                    if (offset.distSqr(BlockPos.ZERO) != 2)
//                        return;
//                    neighbours.add(worldPosition.offset(offset));
//                });
        return neighbours;
    }

    public boolean isCustomConnection(TechnomaticTileEntity other, BlockState state, BlockState otherState) {
        return false;
    }

    protected boolean canPropagateDiagonally(IConduct block, BlockState state) {
        return ICogWheel.isSmallCog(state);
    }

    @Override
    public void requestModelDataUpdate() {
        super.requestModelDataUpdate();
        if (!this.remove)
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> InstancedRenderDispatcher.enqueueUpdate(this));
    }

    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        float componentEmission = Math.abs(getEmission());
        if (componentEmission == 0)
            return;
        float pitch = Mth.clamp((componentEmission / 256f) + .45f, .85f, 1f);

        if (isNoisy())
            SoundScapes.play(SoundScapes.AmbienceGroup.KINETIC, worldPosition, pitch);

        Block block = getBlockState().getBlock();
        if (ICogWheel.isSmallCog(block) || ICogWheel.isLargeCog(block) || block instanceof GearboxBlock)
            SoundScapes.play(SoundScapes.AmbienceGroup.COG, worldPosition, pitch);
    }

    protected boolean isNoisy() {
        return true;
    }

    public int getRotationAngleOffset(Direction.Axis axis) {
        return 0;
    }

}
