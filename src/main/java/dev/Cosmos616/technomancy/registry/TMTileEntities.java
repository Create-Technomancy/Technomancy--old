package dev.Cosmos616.technomancy.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.content.contraptions.components.laser.LaserRenderer;
import dev.Cosmos616.technomancy.content.contraptions.components.laser.LaserTileEntity;
import dev.Cosmos616.technomancy.content.contraptions.energy.accumulator.SoulAccumulatorTileEntity;
import dev.Cosmos616.technomancy.content.contraptions.energy.battery.BatteryTileEntity;
import dev.Cosmos616.technomancy.content.contraptions.energy.battery.CreativeBatteryTileEntity;
import dev.Cosmos616.technomancy.content.contraptions.energy.cable.CableTileEntity;
import dev.Cosmos616.technomancy.content.contraptions.energy.combustor.SoulCombustorTileEntity;
import dev.Cosmos616.technomancy.content.contraptions.energy.tesla_coil.coil_topload.CoilToploadTileEntity;
import dev.Cosmos616.technomancy.content.contraptions.energy.tesla_coil.sparkgap.SparkGapRenderer;
import dev.Cosmos616.technomancy.content.contraptions.energy.tesla_coil.sparkgap.SparkGapTileEntity;
import dev.Cosmos616.technomancy.content.contraptions.energy.tesla_coil.tesla_primary.TeslaPrimaryTileEntity;
import dev.Cosmos616.technomancy.content.contraptions.energy.tesla_coil.teslastalk.TeslaStalkTileEntity;

public class TMTileEntities {

    private static final CreateRegistrate REGISTRATE = Technomancy.getRegistrate();

//    public static final BlockEntityEntry<SpectreCoilTileEntity> SPECTRE_COIL = REGISTRATE
//            .tileEntity("spectre_coil", SpectreCoilTileEntity::new)
//            .validBlocks(TMBlocks.SPECTRE_COIL_BLOCK)
//            .register();

    public static final BlockEntityEntry<BatteryTileEntity> BATTERY = REGISTRATE
            .tileEntity("soul_battery", BatteryTileEntity::new)
            .validBlocks(TMBlocks.BATTERY_BLOCK)
            .register();

    public static final BlockEntityEntry<CreativeBatteryTileEntity> CREATIVE_BATTERY = REGISTRATE
            .tileEntity("creative_soul_battery", CreativeBatteryTileEntity::new)
            .validBlocks(TMBlocks.CREATIVE_BATTERY_BLOCK)
            .register();

    public static final BlockEntityEntry<CableTileEntity> CABLE = REGISTRATE
            .tileEntity("cable", CableTileEntity::new)
            .validBlocks(TMBlocks.CABLE_BLOCK)
            .register();

    public static final BlockEntityEntry<CableTileEntity> ENCASED_CABLE = REGISTRATE
            .tileEntity("encased_cable", CableTileEntity::new)
            .validBlocks(TMBlocks.ENCASED_CABLE_BLOCK)
            .register();
    
    public static final BlockEntityEntry<LaserTileEntity> LASER = REGISTRATE
            .tileEntity("laser", LaserTileEntity::new)
            .validBlocks(TMBlocks.LASER)
            .renderer(() -> LaserRenderer::new)
            .register();
    
    public static final BlockEntityEntry<SoulAccumulatorTileEntity> SOUL_ACCUMULATOR = REGISTRATE
            .tileEntity("soul_accumulator", SoulAccumulatorTileEntity::new)
            .validBlocks(TMBlocks.SOUL_ACCUMULATOR)
            .register();

    public static final BlockEntityEntry<SparkGapTileEntity> SPARK_GAP =REGISTRATE
            .tileEntity("spark_gap", SparkGapTileEntity::new)
            .validBlocks(TMBlocks.SPARK_GAP)
            .renderer(() -> SparkGapRenderer::new)
            .register();

    public static final BlockEntityEntry<SoulCombustorTileEntity> SOUL_COMBUSTOR = REGISTRATE
            .tileEntity("soul_combustor", SoulCombustorTileEntity::new)
            .validBlocks(TMBlocks.SOUL_COMBUSTOR)
            .register();

    public static final BlockEntityEntry<TeslaPrimaryTileEntity> TESLA_PRIMARY = REGISTRATE
            .tileEntity("tesla_primary", TeslaPrimaryTileEntity::new)
            .validBlocks(TMBlocks.TESLA_PRIMARY)
            .register();

    public static final BlockEntityEntry<TeslaStalkTileEntity> TESLA_STALK = REGISTRATE
            .tileEntity("tesla_stalk", TeslaStalkTileEntity::new)
            .validBlocks(TMBlocks.TESLA_STALK)
            .register();

    public static final BlockEntityEntry<CoilToploadTileEntity> COIL_TOPLOAD = REGISTRATE
            .tileEntity("coil_topload", CoilToploadTileEntity::new)
            .validBlocks(TMBlocks.COIL_TOPLOAD)
            .register();

  //  public static final BlockEntityEntry<IonizedBulbTileEntity> IONIZED_BULB = REGISTRATE
  //          .tileEntity("ionized_bulb", IonizedBulbTileEntity::new)
  //          .validBlocks(TMBlocks.IONIZED_BULB)
  //          .register();

    public static void register() {}
}
