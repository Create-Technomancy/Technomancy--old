package com.chazbomb.technomancy.registry;

import com.chazbomb.technomancy.Technomancy;
import com.chazbomb.technomancy.content.contraptions.actors.liquidator.SoulLiquidatorBlockEntity;
import com.chazbomb.technomancy.content.contraptions.energy.battery.BatteryBlockEntity;
import com.chazbomb.technomancy.content.contraptions.energy.battery.CreativeBatteryBlockEntity;
import com.chazbomb.technomancy.content.contraptions.energy.cable.CableBlockEntity;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.chazbomb.technomancy.content.contraptions.components.laser.LaserBlockEntity;
import com.chazbomb.technomancy.content.contraptions.energy.accumulator.SoulAccumulatorBlockEntity;
import com.chazbomb.technomancy.content.contraptions.components.combustor.SoulBurnerBlockEntity;
import com.chazbomb.technomancy.content.contraptions.energy.tesla_coil.coil_topload.CoilToploadTileEntity;
import com.chazbomb.technomancy.content.contraptions.energy.tesla_coil.sparkgap.SparkGapBlockEntity;
import com.chazbomb.technomancy.content.contraptions.energy.tesla_coil.tesla_primary.TeslaPrimaryBlockEntity;
import com.chazbomb.technomancy.content.contraptions.energy.tesla_coil.teslastalk.TeslaStalkBlockEntity;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import static com.chazbomb.technomancy.Technomancy.REGISTRATE;

public class TMBlockEntities {

//    public static final BlockEntityEntry<SpectreCoilTileEntity> SPECTRE_COIL =
//            be("spectre_coil", SpectreCoilTileEntity::new, TMBlocks.SPECTRE_COIL);

    public static final BlockEntityEntry<BatteryBlockEntity> BATTERY =
            be("soul_battery", BatteryBlockEntity::new, TMBlocks.BATTERY_BLOCK);

    public static final BlockEntityEntry<CreativeBatteryBlockEntity> CREATIVE_BATTERY =
            be("creative_soul_battery", CreativeBatteryBlockEntity::new, TMBlocks.CREATIVE_BATTERY_BLOCK);

    public static final BlockEntityEntry<CableBlockEntity> CABLE =
            be("cable", CableBlockEntity::new, TMBlocks.CABLE_BLOCK);

    public static final BlockEntityEntry<CableBlockEntity> ENCASED_CABLE =
            be("encased_cable", CableBlockEntity::new, TMBlocks.ENCASED_CABLE_BLOCK);

    public static final BlockEntityEntry<LaserBlockEntity> LASER =
            be("laser", LaserBlockEntity::new, TMBlocks.LASER);
    
    public static final BlockEntityEntry<SoulAccumulatorBlockEntity> SOUL_ACCUMULATOR =
            be("soul_accumulator", SoulAccumulatorBlockEntity::new, TMBlocks.SOUL_ACCUMULATOR);

    public static final BlockEntityEntry<SparkGapBlockEntity> SPARK_GAP =
            be("spark_gap", SparkGapBlockEntity::new, TMBlocks.SPARK_GAP);

    public static final BlockEntityEntry<SoulBurnerBlockEntity> SOUL_BURNER =
            be("soul_burner", SoulBurnerBlockEntity::new, TMBlocks.SOUL_BURNER);

    public static final BlockEntityEntry<TeslaPrimaryBlockEntity> TESLA_PRIMARY =
            be("tesla_primary", TeslaPrimaryBlockEntity::new, TMBlocks.TESLA_PRIMARY);

    public static final BlockEntityEntry<TeslaStalkBlockEntity> TESLA_STALK =
            be("tesla_stalk", TeslaStalkBlockEntity::new, TMBlocks.TESLA_STALK);

    public static final BlockEntityEntry<CoilToploadTileEntity> COIL_TOPLOAD =
            be("coil_topload", CoilToploadTileEntity::new, TMBlocks.COIL_TOPLOAD);

    public static final BlockEntityEntry<SoulLiquidatorBlockEntity> SOUL_LIQUIDATOR =
            be("soul_liquidator", SoulLiquidatorBlockEntity::new, TMBlocks.SOUL_LIQUIDATOR);


//    public static final BlockEntityEntry<IonizedBulbTileEntity> IONIZED_BULB =
//          be("ionized_bulb", IonizedBulbTileEntity::new, TMBlocks.IONIZED_BULB);

    @SafeVarargs
    private static <T extends BlockEntity> BlockEntityEntry<T> be(String name,
                                                                  BlockEntityBuilder.BlockEntityFactory<T> factory,
                                                                  NonNullSupplier<? extends Block>... blocks) {
        return REGISTRATE.blockEntity(name, factory)
                .validBlocks(blocks)
                .register();
    }

    @SafeVarargs
    private static <T extends BlockEntity> BlockEntityEntry<T> be(String name,
                                                                  BlockEntityBuilder.BlockEntityFactory<T> factory,
                                                                  NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>> renderer,
                                                                  NonNullSupplier<? extends Block>... blocks) {
        return REGISTRATE.blockEntity(name, factory)
                .validBlocks(blocks)
                .renderer(() -> renderer)
                .register();
    }

    public static void register() {
        Technomancy.LOGGER.info("Registering block entities!");
    }
}
