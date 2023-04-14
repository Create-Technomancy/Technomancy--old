package dev.Cosmos616.technomancy.content.contraptions.aether.battery;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.logistics.block.vault.ItemVaultBlock;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.foundation.LEGACYenergy.IAetherStorage;
import dev.Cosmos616.technomancy.foundation.aether.subtypes.AetherAccumulator;
import dev.Cosmos616.technomancy.foundation.aether.subtypes.AetherProducer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;

public class BatteryBlockEntity extends AetherAccumulator implements IMultiTileContainerAether, AetherProducer {
    
    final boolean creative;
    
    protected BlockPos controller;
    protected BlockPos lastKnownPos;
    protected boolean updateConnectivity;
    protected int width;
    protected int height;
    
    public BatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        creative = ((BatteryBlock) state.getBlock()).isCreative();
        updateConnectivity = false;
        width = 1;
        height = 1;
    }
    
    @Override
    public int getMaxStorage() {
        return creative ? 10000000 : 50000;
    }
    
    @Override
    public int getStoredAether() {
        return creative ? 1000000 : super.getStoredAether();
    }
    
    @Override
    public int getProducedAether() {
        return creative ? 1000000 : 0;
    }
    
    protected void updateConnectivity() {
        updateConnectivity = false;
        if (level.isClientSide())
            return;
        if (!isController())
            return;
        ConnectivityHandler.formMulti(this);
    }

    @Override
    public void initialize() {
        super.initialize();
        sendData();
        if (level.isClientSide)
            invalidateRenderBoundingBox();
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
    }

    private void onPositionChanged() {
        removeController(true);
        lastKnownPos = worldPosition;
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
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
    
        BlockPos controllerBefore = controller;
        int prevWidth = width;
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
        }
    
        boolean changeOfController =
            !Objects.equals(controllerBefore, controller);
        if (hasLevel() && (changeOfController || prevWidth != width || prevHeight != height))
            level.setBlocksDirty(getBlockPos(), Blocks.AIR.defaultBlockState(), getBlockState());
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        try {
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        if (updateConnectivity)
            compound.putBoolean("Uninitialized", true);
        if (lastKnownPos != null)
            compound.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));
        if (!isController())
            compound.put("Controller", NbtUtils.writeBlockPos(controller));
        if (isController()) {
            compound.putInt("Size", width);
            compound.putInt("Height", height);
        }
    
        super.write(compound, clientPacket);
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
        setChanged();
        sendData();
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
    
        setShapes();
        setChanged();
        sendData();
    }

    public void applyBatterySize(int blocks) {
    
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
            state = state.setValue(BatteryBlock.BOTTOM,
                getController().getY() == getBlockPos().getY());
            state = state.setValue(BatteryBlock.TOP,
                getController().getY() + height - 1 == getBlockPos().getY());
            level.setBlock(getBlockPos(), state, 6);
        }
        if (isController()) {
            setShapes();
        }
        setChanged();
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
