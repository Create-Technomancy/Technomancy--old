package com.chazbomb.technomancy.registry;

import com.chazbomb.technomancy.Technomancy;
import com.chazbomb.technomancy.content.contraptions.energy.battery.BatteryBlockEntity;
import com.chazbomb.technomancy.content.contraptions.energy.battery.CreativeBatteryBlockEntity;
import com.chazbomb.technomancy.content.contraptions.energy.cable.CableBlockEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.chazbomb.technomancy.content.contraptions.components.laser.LaserBlockEntity;
import com.chazbomb.technomancy.content.contraptions.energy.accumulator.SoulAccumulatorBlockEntity;
import com.chazbomb.technomancy.content.contraptions.components.combustor.SoulBurnerBlockEntity;
import com.chazbomb.technomancy.content.contraptions.energy.tesla_coil.coil_topload.CoilToploadTileEntity;
import com.chazbomb.technomancy.content.contraptions.energy.tesla_coil.sparkgap.SparkGapBlockEntity;
import com.chazbomb.technomancy.content.contraptions.energy.tesla_coil.tesla_primary.TeslaPrimaryBlockEntity;
import com.chazbomb.technomancy.content.contraptions.energy.tesla_coil.teslastalk.TeslaStalkBlockEntity;

public class TMBlockEntities {

    private static final CreateRegistrate REGISTRATE = Technomancy.getRegistrate();

//    public static final BlockEntityEntry<SpectreCoilTileEntity> SPECTRE_COIL = REGISTRATE
//            .blockEntity("spectre_coil", SpectreCoilTileEntity::new)
//            .validBlocks(TMBlocks.SPECTRE_COIL_BLOCK)
//            .register();

    public static final BlockEntityEntry<BatteryBlockEntity> BATTERY = REGISTRATE
            .blockEntity("soul_battery", BatteryBlockEntity::new)
            .validBlocks(TMBlocks.BATTERY_BLOCK)
            .register();

    public static final BlockEntityEntry<CreativeBatteryBlockEntity> CREATIVE_BATTERY = REGISTRATE
            .blockEntity("creative_soul_battery", CreativeBatteryBlockEntity::new)
            .validBlocks(TMBlocks.CREATIVE_BATTERY_BLOCK)
            .register();

    public static final BlockEntityEntry<CableBlockEntity> CABLE = REGISTRATE
            .blockEntity("cable", CableBlockEntity::new)
            .validBlocks(TMBlocks.CABLE_BLOCK)
            .register();

    public static final BlockEntityEntry<CableBlockEntity> ENCASED_CABLE = REGISTRATE
            .blockEntity("encased_cable", CableBlockEntity::new)
            .validBlocks(TMBlocks.ENCASED_CABLE_BLOCK)
            .register();
    
    public static final BlockEntityEntry<LaserBlockEntity> LASER = REGISTRATE
            .blockEntity("laser", LaserBlockEntity::new)
            .validBlocks(TMBlocks.LASER)
            //.renderer(() -> LaserRenderer::new)
            .register();
    
    public static final BlockEntityEntry<SoulAccumulatorBlockEntity> SOUL_ACCUMULATOR = REGISTRATE
            .blockEntity("soul_accumulator", SoulAccumulatorBlockEntity::new)
            .validBlocks(TMBlocks.SOUL_ACCUMULATOR)
            .register();

    public static final BlockEntityEntry<SparkGapBlockEntity> SPARK_GAP =REGISTRATE
            .blockEntity("spark_gap", SparkGapBlockEntity::new)
            .validBlocks(TMBlocks.SPARK_GAP)
          //  .renderer(() -> SparkGapRenderer::new)
            .register();

    public static final BlockEntityEntry<SoulBurnerBlockEntity> SOUL_BURNER = REGISTRATE
            .blockEntity("soul_burner", SoulBurnerBlockEntity::new)
            .validBlocks(TMBlocks.SOUL_BURNER)
//            .renderer(() -> SoulBurnerRenderer:: new)
            .register();

    public static final BlockEntityEntry<TeslaPrimaryBlockEntity> TESLA_PRIMARY = REGISTRATE
            .blockEntity("tesla_primary", TeslaPrimaryBlockEntity::new)
            .validBlocks(TMBlocks.TESLA_PRIMARY)
            .register();

    public static final BlockEntityEntry<TeslaStalkBlockEntity> TESLA_STALK = REGISTRATE
            .blockEntity("tesla_stalk", TeslaStalkBlockEntity::new)
            .validBlocks(TMBlocks.TESLA_STALK)
            .register();

    public static final BlockEntityEntry<CoilToploadTileEntity> COIL_TOPLOAD = REGISTRATE
            .blockEntity("coil_topload", CoilToploadTileEntity::new)
            .validBlocks(TMBlocks.COIL_TOPLOAD)
            .register();


//    public static final BlockEntityEntry<IonizedBulbTileEntity> IONIZED_BULB = REGISTRATE
//            .blockEntity("ionized_bulb", IonizedBulbTileEntity::new)
//            .validBlocks(TMBlocks.IONIZED_BULB)
//            .register();

    public static void register() {}
}
