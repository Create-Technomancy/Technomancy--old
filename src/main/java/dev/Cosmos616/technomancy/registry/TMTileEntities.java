package dev.Cosmos616.technomancy.registry;

import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.base.CutoutRotatingInstance;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.content.contraptions.components.soulengine.SoulEngineTileEntity;
import dev.Cosmos616.technomancy.content.contraptions.energy.battery.QuantumBatteryTileEntity;
import dev.Cosmos616.technomancy.content.contraptions.energy.cable.CableTileEntity;
import dev.Cosmos616.technomancy.content.contraptions.energy.spectre_coil.SpectreCoilTileEntity;

public class TMTileEntities {

    private static final CreateRegistrate REGISTRATE = Technomancy.getRegistrate();

    public static final BlockEntityEntry<SpectreCoilTileEntity> SPECTRE_COIL = REGISTRATE
            .tileEntity("spectre_coil", SpectreCoilTileEntity::new)
            .validBlocks(TMBlocks.SPECTRE_COIL_BLOCK)
            .register();

    public static final BlockEntityEntry<QuantumBatteryTileEntity> QUANTUM_BATTERY = REGISTRATE
            .tileEntity("quantum_battery", QuantumBatteryTileEntity::new)
            .validBlocks(TMBlocks.QUANTUM_BATTERY_BLOCK)
            .register();

    public static final BlockEntityEntry<CableTileEntity> CABLE = REGISTRATE
            .tileEntity("cable", CableTileEntity::new)
            .validBlocks(TMBlocks.CABLE_BLOCK)
            .register();

    public static final BlockEntityEntry<CableTileEntity> ENCASED_CABLE = REGISTRATE
            .tileEntity("encased_cable", CableTileEntity::new)
            .validBlocks(TMBlocks.ENCASED_CABLE_BLOCK)
            .register();

    public static final BlockEntityEntry<SoulEngineTileEntity> SOUL_ENGINE = Create.registrate()
            .tileEntity("soul_engine", SoulEngineTileEntity::new)
            .instance(() -> CutoutRotatingInstance::new, false)
            .validBlocks(TMBlocks.SOUL_ENGINE)
            .renderer(() -> KineticTileEntityRenderer::new)
            .register();

    public static void register() {}
}
