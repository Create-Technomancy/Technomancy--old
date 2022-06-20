package dev.Cosmos616.technomancy.registry;

import com.simibubi.create.content.contraptions.base.CasingBlock;
import com.simibubi.create.content.contraptions.relays.encased.EncasedCTBehaviour;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.content.contraptions.energy.battery.QuantumBatteryBlock;
import dev.Cosmos616.technomancy.content.contraptions.energy.cable.CableAttachmentModel;
import dev.Cosmos616.technomancy.content.contraptions.energy.cable.CableBlock;
import dev.Cosmos616.technomancy.content.contraptions.energy.cable.EncasedCableBlock;
import dev.Cosmos616.technomancy.content.contraptions.energy.spectre_coil.SpectreCoilBlock;
import dev.Cosmos616.technomancy.content.contraptions.energy.spectre_coil.SpectreCoilGenerator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.simibubi.create.AllTags.pickaxeOnly;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class TMBlocks {
    private static final CreateRegistrate REGISTRATE = Technomancy.getRegistrate();

    public static final BlockEntry<SpectreCoilBlock> SPECTRE_COIL_BLOCK = REGISTRATE
            .block("spectre_coil", SpectreCoilBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p
                    .sound(SoundType.NETHERITE_BLOCK)
                    .explosionResistance(1200)
                    .noOcclusion())
            .transform(pickaxeOnly())
            .blockstate(new SpectreCoilGenerator()::generate)
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .model(AssetLookup.customBlockItemModel("_", "coil_single"))
            .build()
            .register();

    public static final BlockEntry<QuantumBatteryBlock> QUANTUM_BATTERY_BLOCK = REGISTRATE
            .block("quantum_battery", QuantumBatteryBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK)
                    .explosionResistance(1200))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get(), p.models().getExistingFile(p.modLoc("block/quantum_battery"))))
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .build()
            .register();

    public static final BlockEntry<CableBlock> CABLE_BLOCK = REGISTRATE
            .block("cable", CableBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .blockstate(TMBlockStateGen.cable())
            .onRegister(CreateRegistrate.blockModel(() -> CableAttachmentModel::new))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<EncasedCableBlock> ENCASED_CABLE_BLOCK = REGISTRATE
            .block("encased_cable", EncasedCableBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(TMBlockStateGen.encasedCable())
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(TMSpriteShifts.CABLE_CASING)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, TMSpriteShifts.CABLE_CASING)))
            .onRegister(CreateRegistrate.blockModel(() -> CableAttachmentModel::new))
            .loot((p, b) -> p.dropOther(b, CABLE_BLOCK.get()))
            .register();

    public static final BlockEntry<CasingBlock> CABLE_CASING = REGISTRATE
            .block("cable_casing", CasingBlock::new)
            .transform(BuilderTransformers.casing(() -> TMSpriteShifts.CABLE_CASING))
            .properties(p -> p.sound(SoundType.COPPER))
            .register();

    public static void register() {}
}
