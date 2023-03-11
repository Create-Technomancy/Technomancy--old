package dev.Cosmos616.technomancy.content.contraptions.energy.battery;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.tileEntity.IMultiTileContainer;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import dev.Cosmos616.technomancy.foundation.energy.AetherStorage;
import dev.Cosmos616.technomancy.foundation.energy.AetherStorageBehavior;
import dev.Cosmos616.technomancy.foundation.energy.IAetherStorage;
import dev.Cosmos616.technomancy.registry.TMCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class BatteryBlockEntity extends SmartTileEntity implements IHaveGoggleInformation, IMultiTileContainer {

    private static final int MAX_SIZE = 3;

    protected AetherStorage aether;
    protected LazyOptional<IAetherStorage> capability;

    protected BlockPos controller;
    protected BlockPos lastKnownPos;
    protected boolean updateConnectivity;
    protected int width;
    protected int height;

    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;

    public BatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        aether = createInventory();
        capability = LazyOptional.of(() -> aether);
        updateConnectivity = false;
        width = 1;
        height = 1;
        refreshCapability();
    }

    protected AetherStorage createInventory() {
        return new AetherStorage(getCapacityMultiplier());
    }

    protected void updateConnectivity() {
        updateConnectivity = false;
        if (level.isClientSide)
            return;
        if (!isController())
            return;
        ConnectivityHandler.formMulti(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }

        if (lastKnownPos == null)
            lastKnownPos = getBlockPos();
        else if (!lastKnownPos.equals(worldPosition) && worldPosition != null) {
            onPositionChanged();
            return;
        }

        if (updateConnectivity)
            updateConnectivity();
    }

    private void onPositionChanged() {
        removeController(true);
        lastKnownPos = worldPosition;
    }

    @Override
    public void sendData() {
        if (syncCooldown > 0) {
            queuedSync = true;
            return;
        }
        super.sendData();
        queuedSync = false;
        syncCooldown = SYNC_RATE;
    }

    public void sendDataImmediately() {
        syncCooldown = 0;
        queuedSync = false;
        sendData();
    }

    public void setShapes() {
        for (int yOffset = 0; yOffset < height; yOffset++) {
            for (int xOffset = 0; xOffset < width; xOffset++) {
                for (int zOffset = 0; zOffset < width; zOffset++) {

                    BlockPos pos = this.worldPosition.offset(xOffset, yOffset, zOffset);
                    BlockState blockState = level.getBlockState(pos);
                    if (!BatteryBlock.isBattery(blockState))
                        continue;

                    BatteryBlock.Shape shape = BatteryBlock.Shape.SINGLE;
                    // SIZE 2: Every battery is a corner
                    if (width == 2)
                        shape = xOffset == 0 ? zOffset == 0 ? BatteryBlock.Shape.NW : BatteryBlock.Shape.SW
                                : zOffset == 0 ? BatteryBlock.Shape.NE : BatteryBlock.Shape.SE;

                    level.setBlock(pos, blockState.setValue(BatteryBlock.SHAPE, shape), 22);
                    level.getChunkSource()
                            .getLightEngine()
                            .checkBlock(pos);
                }
            }
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        BatteryBlockEntity controllerTE = getControllerTE();
        if (controllerTE == null)
            return false;
        return AetherStorageBehavior.containedAetherTooltip(tooltip,
            controllerTE.getCapability(TMCapabilities.AETHER));
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        BlockPos controllerBefore = controller;
        int prevSize = width;
        int prevHeight = height;

        updateConnectivity = compound.contains("Uninitialized");
        controller = null;
        lastKnownPos = null;

        if (compound.contains("LastKnownPos"))
            lastKnownPos = NbtUtils.readBlockPos(compound.getCompound("LastKnownPos"));
        if (compound.contains("Controller"))
            controller = NbtUtils.readBlockPos(compound.getCompound("Controller"));

        if (isController()) {
            width = compound.getInt("Size");
            height = compound.getInt("Height");
            aether.readFromNBT(compound.getCompound("AetherStorage"));
            aether.setAetherCapacity(getTotalBatterySize() * getCapacityMultiplier());
            if (aether.getSpace() < 0)
                aether.extractAether(-aether.getSpace(), false);
        }

        if (!clientPacket)
            return;

        boolean changeOfController = !Objects.equals(controllerBefore, controller);
        if (changeOfController || prevSize != width || prevHeight != height) {
            if (hasLevel())
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            if (isController())
                aether.setAetherCapacity(getCapacityMultiplier() * getTotalBatterySize());
            invalidateRenderBoundingBox();
        }
    }

    public float getChargeState() {
        return (float) aether.getAetherStored() / aether.getAetherCapacity();
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        if (updateConnectivity)
            compound.putBoolean("Uninitialized", true);
        if (lastKnownPos != null)
            compound.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));
        if (!isController())
            compound.put("Controller", NbtUtils.writeBlockPos(controller));
        if (isController()) {
            compound.put("AetherStorage", aether.writeToNBT(new CompoundTag()));
            compound.putInt("Size", width);
            compound.putInt("Height", height);
        }
        super.write(compound, clientPacket);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!capability.isPresent())
            refreshCapability();
        if (cap == TMCapabilities.AETHER && (side == null || side.getAxis().isVertical()))
            return capability.cast();
        return super.getCapability(cap, side);
    }

    public IAetherStorage getBatteryInventory() {
        return aether;
    }

    public int getTotalBatterySize() {
        return width * width * height;
    }
    
    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {}

    @Override
    public BlockPos getController() {
        return isController() ? worldPosition : controller;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BatteryBlockEntity getControllerTE() {
        if (isController())
            return this;
        BlockEntity tileEntity = level.getBlockEntity(controller);
        if (tileEntity instanceof BatteryBlockEntity)
            return (BatteryBlockEntity) tileEntity;
        return null;
    }

    @Override
    public boolean isController() {
        return controller == null
                || worldPosition.getX() == controller.getX()
                && worldPosition.getY() == controller.getY()
                && worldPosition.getZ() == controller.getZ();
    }

    @Override
    public void setController(BlockPos controller) {
        if (level.isClientSide && !isVirtual())
            return;
        if (controller.equals(this.controller))
            return;
        this.controller = controller;
        refreshCapability();
        setChanged();
        sendData();
    }

    private void refreshCapability() {
        LazyOptional<IAetherStorage> oldCap = capability;
        capability = LazyOptional.of(this::handlerForCapability);
        oldCap.invalidate();
    }

    private IAetherStorage handlerForCapability() {
        return isController() ? aether
                : getControllerTE() != null ? getControllerTE().handlerForCapability() : new AetherStorage(0);
    }

    @Override
    public void removeController(boolean keepContents) {
        if (level.isClientSide)
            return;
        updateConnectivity = true;
        if (!keepContents)
            applyBatterySize(1);
        controller = null;
        width = 1;
        height = 1;

        BlockState state = getBlockState();
        if (BatteryBlock.isBattery(state)) {
            state = state.setValue(BatteryBlock.BOTTOM, true);
            state = state.setValue(BatteryBlock.TOP, true);
            state = state.setValue(BatteryBlock.SHAPE, BatteryBlock.Shape.SINGLE);
            getLevel().setBlock(worldPosition, state, 22);
        }

        refreshCapability();
        setChanged();
        sendData();
    }

    public void applyBatterySize(int blocks) {
        aether.setAetherCapacity(blocks * getCapacityMultiplier());
        int overflow = aether.getAetherStored() - aether.getAetherCapacity();
        if (overflow > 0)
            aether.extractAether(overflow, false);
    }

    public static int getCapacityMultiplier() {
//        return TMConfigs.SERVER.energy.batteryCapacity.get() * 1000;
        return 1000;
    }

    @Override
    public BlockPos getLastKnownPos() {
        return lastKnownPos;
    }

    @Override
    public void preventConnectivityUpdate() {
        updateConnectivity = false;
    }

    @Override
    public void notifyMultiUpdated() {
        BlockState state = this.getBlockState();
        if (BatteryBlock.isBattery(state)) { // safety
            state = state.setValue(BatteryBlock.BOTTOM, getController().getY() == getBlockPos().getY());
            state = state.setValue(BatteryBlock.TOP, getController().getY() + height - 1 == getBlockPos().getY());
            level.setBlock(getBlockPos(), state, 6);
        }
        if (isController()) {
            applyBatterySize(getTotalBatterySize());
            setShapes();
        }
        refreshCapability();
        setChanged();
        sendData();
    }

    @Override
    public Direction.Axis getMainConnectionAxis() {
        return Direction.Axis.Y;
    }

    @Override
    public int getMaxLength(Direction.Axis longAxis, int width) {
        if (longAxis == Direction.Axis.Y)
            return getMaxHeight(width);
        return getMaxWidth();
    }

    @Override
    public int getMaxWidth() {
        return 2;
    }

    public int getMaxHeight(int width) {
        return 1 + 2 * width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }
}
