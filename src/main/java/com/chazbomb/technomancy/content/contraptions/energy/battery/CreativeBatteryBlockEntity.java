package com.chazbomb.technomancy.content.contraptions.energy.battery;

import com.simibubi.create.foundation.utility.Lang;
import com.chazbomb.technomancy.foundation.TMLang;
import com.chazbomb.technomancy.foundation.energy.AetherStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CreativeBatteryBlockEntity extends BatteryBlockEntity {
    public CreativeBatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected AetherStorage createInventory() {
        return new CreativeAetherStorage();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        TMLang.translate("gui.goggles.aether_container").forGoggles(tooltip);
        Lang.builder()
                .text(ChatFormatting.AQUA, "Unlimited Power!")
                .style(ChatFormatting.ITALIC)
                .forGoggles(tooltip, 1);
        return true;
    }

    public static class CreativeAetherStorage extends AetherStorage {

        public CreativeAetherStorage() {
            super(1, i -> {});
        }

        public int receiveAether(int maxReceive, boolean simulate)
        {
            return maxReceive;
        }

        @Override
        public int extractAether(int maxExtract, boolean simulate) {
            return maxExtract;
        }
    }
}
