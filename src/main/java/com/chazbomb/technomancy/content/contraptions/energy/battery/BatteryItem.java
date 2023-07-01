package com.chazbomb.technomancy.content.contraptions.energy.battery;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.chazbomb.technomancy.registry.TMBlocks;
import com.chazbomb.technomancy.registry.TMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BatteryItem extends BlockItem {

    public BatteryItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public InteractionResult place(BlockPlaceContext ctx) {
        InteractionResult initialResult = super.place(ctx);
        if (!initialResult.consumesAction())
            return initialResult;
        tryMultiPlace(ctx);
        return initialResult;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pPos, Level pLevel, Player pPlayer, ItemStack pItem, BlockState pState) {
        MinecraftServer minecraftserver = pLevel.getServer();
        if (minecraftserver == null)
            return false;
        CompoundTag nbt = pItem.getTagElement("BlockEntityTag");
        if (nbt != null) {
            nbt.remove("Size");
            nbt.remove("Height");
            nbt.remove("Controller");
            nbt.remove("LastKnownPos");
            if (nbt.contains("Content")) {
                int charge = nbt.getInt("Content");
                if (charge > 0) {
                    charge = (Math.min(BatteryBlockEntity.getCapacityMultiplier(), charge));
                    nbt.putInt("Content", charge);
                }
            }
        }
        return super.updateCustomBlockEntityTag(pPos, pLevel, pPlayer, pItem, pState);
    }

    private void tryMultiPlace(BlockPlaceContext ctx) {
        Player player = ctx.getPlayer();
        if (player == null)
            return;
        if (player.isShiftKeyDown())
            return;
        Direction face = ctx.getClickedFace();
        if (!face.getAxis()
                .isVertical())
            return;
        ItemStack stack = ctx.getItemInHand();
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockPos placedOnPos = pos.relative(face.getOpposite());
        BlockState placedOnState = world.getBlockState(placedOnPos);

        if (!BatteryBlock.isBattery(placedOnState))
            return;
        boolean creative = getBlock().equals(TMBlocks.CREATIVE_BATTERY_BLOCK.get());
        BatteryBlockEntity batteryAt = ConnectivityHandler.partAt(
                creative ? TMBlockEntities.CREATIVE_BATTERY.get() : TMBlockEntities.BATTERY.get(), world, placedOnPos
        );
        if (batteryAt == null)
            return;
        BatteryBlockEntity controllerTE = batteryAt.getControllerBE();
        if (controllerTE == null)
            return;

        int width = controllerTE.width;
        if (width == 1)
            return;

        int batteriesToPlace = 0;
        BlockPos startPos = face == Direction.DOWN ? controllerTE.getBlockPos()
                .below()
                : controllerTE.getBlockPos()
                .above(controllerTE.height);

        if (startPos.getY() != pos.getY())
            return;

        for (int xOffset = 0; xOffset < width; xOffset++) {
            for (int zOffset = 0; zOffset < width; zOffset++) {
                BlockPos offsetPos = startPos.offset(xOffset, 0, zOffset);
                BlockState blockState = world.getBlockState(offsetPos);
                if (BatteryBlock.isBattery(blockState))
                    continue;
                if (!blockState.getMaterial()
                        .isReplaceable())
                    return;
                batteriesToPlace++;
            }
        }

        if (!player.isCreative() && stack.getCount() < batteriesToPlace)
            return;

        for (int xOffset = 0; xOffset < width; xOffset++) {
            for (int zOffset = 0; zOffset < width; zOffset++) {
                BlockPos offsetPos = startPos.offset(xOffset, 0, zOffset);
                BlockState blockState = world.getBlockState(offsetPos);
                if (BatteryBlock.isBattery(blockState))
                    continue;
                BlockPlaceContext context = BlockPlaceContext.at(ctx, offsetPos, face);
                player.getPersistentData()
                        .putBoolean("SilenceTankSound", true);
                super.place(context);
                player.getPersistentData()
                        .remove("SilenceTankSound");
            }
        }
    }

}