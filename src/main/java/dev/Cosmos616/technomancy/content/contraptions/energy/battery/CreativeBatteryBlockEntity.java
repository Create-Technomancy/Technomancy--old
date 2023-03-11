package dev.Cosmos616.technomancy.content.contraptions.energy.battery;

import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import dev.Cosmos616.technomancy.foundation.TMLang;
import dev.Cosmos616.technomancy.foundation.energy.AetherStorage;
import dev.Cosmos616.technomancy.foundation.energy.AetherStorageBehavior;
import dev.Cosmos616.technomancy.registry.TMCapabilities;
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
        return new CreativeAetherStorage(getCapacityMultiplier());
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

        public CreativeAetherStorage(int capacity) {
            super(capacity);
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
