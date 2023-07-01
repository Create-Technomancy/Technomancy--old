package com.chazbomb.technomancy.content.contraptions.energy.spectre_coil;
/*
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.tileEntity.IMultiTileContainer;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import dev.Cosmos616.technomancy.content.contraptions.energy.TechTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class SpectreCoilTileEntity extends TechTileEntity implements IHaveGoggleInformation, IMultiTileContainer {

    protected BlockPos controller;
    protected BlockPos lastKnownPos;
    protected boolean updateConnectivity;
    protected int height = 1;
    protected final EnergyStorage energy;
    private LazyOptional<IEnergyStorage> lazyEnergy;

    public SpectreCoilTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 0, 0, 0);
        energy = new EnergyStorage(0);
        lazyEnergy = LazyOptional.of(() -> energy);
    }

    protected void updateConnectivity() {
        updateConnectivity = false;
        if (level.isClientSide)
            return;
        SpectreCoilConnectivityHandler.formCoils(this);
    }

    public void updateShape() {
        for (int yOffset = 0; yOffset < height; yOffset++) {

            BlockPos pos = this.worldPosition.offset(0, yOffset, 0);
            BlockState blockState = level.getBlockState(pos);
            if (!SpectreCoilBlock.isCoil(blockState))
                continue;

            blockState = blockState.setValue(SpectreCoilBlock.BOTTOM, yOffset == 0);
            blockState = blockState.setValue(SpectreCoilBlock.TOP, yOffset == height - 1);

            level.setBlock(pos, blockState, 22);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (lastKnownPos == null)
            lastKnownPos = getBlockPos();
        else if (!lastKnownPos.equals(worldPosition) && worldPosition != null) {
            onPositionChanged();
            return;
        }

        if (updateConnectivity)
            updateConnectivity();

        if(level.isClientSide()) {
//            level.addParticle(new DustParticleOptions(new Color(113, 249, 240, 128).asVectorF(), 1),
//                    worldPosition.getX() + 0.5D * (2.0D * level.random.nextDouble() - 1.0D),
//                    worldPosition.getY() + 0.5D * (2.0D * level.random.nextDouble() - 1.0D),
//                    worldPosition.getZ() + 0.5D * (2.0D * level.random.nextDouble() - 1.0D),
//                    (level.random.nextDouble() - 0.5D) * 2.0D,
//                    0,
//                    (level.random.nextDouble() - 0.5D) * 2.0D);

//            for(int i = 0; i < 2; ++i) {
//                double x = worldPosition.getX() + 0.5D;
//                double y = worldPosition.getY() + 0.5D;
//                double z = worldPosition.getZ() + 0.5D;
//                Random r = level.random;
//
//                double x0 = x + 3D * (2.0D * r.nextDouble() - 1.0D);
//                double y0 = y + 0.5D * (2.0D * r.nextDouble() - 1.0D);
//                double z0 = z + 3D * (2.0D * r.nextDouble() - 1.0D);
//                double x1 = (x - x0) / 0.25D;
//                double y1 = 0D;
//                double z1 = (z - z0) / 0.25D;
//
//                level.addParticle(
//                        new DustParticleOptions(
//                                new Color(113, 249, 240, 128)
//                                        .asVectorF(),1), x0, y0, z0, x1, y1, z1);
//            }
            return;
        }


        BlockEntity top = level.getBlockEntity(worldPosition.relative(Direction.UP, height));
        BlockEntity bottom = level.getBlockEntity(worldPosition.relative(Direction.DOWN));
        IEnergyStorage iesTop = top == null ? null : top.getCapability(CapabilityEnergy.ENERGY, Direction.DOWN).orElse(null);
        IEnergyStorage iesBottom = bottom == null ? null : bottom.getCapability(CapabilityEnergy.ENERGY, Direction.UP).orElse(null);
        int energy = 100 * height;
        if(iesTop != null && iesBottom != null)
            energy /= 2;
        if(iesTop != null)
            iesTop.receiveEnergy(energy, false);
        if(iesBottom != null)
            iesBottom.receiveEnergy(energy, false);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if(cap == CapabilityEnergy.ENERGY && (side == Direction.UP || side == Direction.DOWN))
            return lazyEnergy.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public BlockPos getLastKnownPos() {
        return lastKnownPos;
    }

    @Override
    public void preventConnectivityUpdate() {

    }

    @Override
    public void notifyMultiUpdated() {

    }

    @Override
    public Direction.Axis getMainConnectionAxis() {
        return null;
    }

    @Override
    public int getMaxLength(Direction.Axis longAxis, int width) {
        return 0;
    }

    @Override
    public int getMaxWidth() {
        return 0;
    }

    @Override
    public boolean isController() {
        return controller == null
                || worldPosition.getX() == controller.getX()
                && worldPosition.getY() == controller.getY()
                && worldPosition.getZ() == controller.getZ();
    }

    private void onPositionChanged() {
        removeController();
        lastKnownPos = worldPosition;
    }

    public SpectreCoilTileEntity getControllerTE() {
        if (isController())
            return this;
        BlockEntity tileEntity = level.getBlockEntity(controller);
        if (tileEntity instanceof SpectreCoilTileEntity)
            return (SpectreCoilTileEntity) tileEntity;
        return null;
    }

    public void removeController() {
        if (level.isClientSide)
            return;
        updateConnectivity = true;
        controller = null;
        height = 1;

        BlockState state = getBlockState();
        if (SpectreCoilBlock.isCoil(state)) {
            state = state.setValue(SpectreCoilBlock.BOTTOM, true);
            state = state.setValue(SpectreCoilBlock.TOP, true);
            updateShape();
            getLevel().setBlock(worldPosition, state, 22);
        }
        setChanged();
        sendData();
    }

    @Override
    public void setController(BlockPos controller) {
        if (level.isClientSide && !isVirtual())
            return;
        if (controller.equals(this.controller))
            return;
        this.controller = controller;
        setChanged();
        sendData();
    }

    @Override
    public void removeController(boolean keepContents) {

    }

    @Override
    public BlockPos getController() {
        return isController() ? worldPosition : controller;
    }

//    @Override
//    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
//
//    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        BlockPos controllerBefore = controller;
        int prevHeight = height;

        updateConnectivity = compound.contains("Uninitialized");
        controller = null;
        lastKnownPos = null;

        if (compound.contains("LastKnownPos"))
            lastKnownPos = NbtUtils.readBlockPos(compound.getCompound("LastKnownPos"));
        if (compound.contains("Controller"))
            controller = NbtUtils.readBlockPos(compound.getCompound("Controller"));

        if (isController()) {
            height = compound.getInt("Height");
        }

        if (!clientPacket)
            return;

        boolean changeOfController = !Objects.equals(controllerBefore, controller);
        if (changeOfController || prevHeight != height) {
            if (hasLevel())
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            invalidateRenderBoundingBox();
        }
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
            compound.putInt("Height", height);
        }
        super.write(compound, clientPacket);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {}

    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {

    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public void setWidth(int width) {

    }
}
*/