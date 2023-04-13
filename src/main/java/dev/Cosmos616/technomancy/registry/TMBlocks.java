package dev.Cosmos616.technomancy.registry;

import com.simibubi.create.content.contraptions.base.CasingBlock;
import com.simibubi.create.content.contraptions.relays.encased.EncasedCTBehaviour;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.content.contraptions.components.laser.LaserBlock;
import dev.Cosmos616.technomancy.content.contraptions.aether.accumulator.SoulAccumulatorBlock;
import dev.Cosmos616.technomancy.content.contraptions.aether.battery.BatteryBlock;
import dev.Cosmos616.technomancy.content.contraptions.aether.battery.BatteryGenerator;
import dev.Cosmos616.technomancy.content.contraptions.aether.battery.BatteryItem;
import dev.Cosmos616.technomancy.content.contraptions.aether.cable.CableAttachmentModel;
import dev.Cosmos616.technomancy.content.contraptions.aether.cable.CableBlock;
import dev.Cosmos616.technomancy.content.contraptions.aether.cable.EncasedCableBlock;
import dev.Cosmos616.technomancy.content.contraptions.components.combustor.SoulBurnerBlock;
import dev.Cosmos616.technomancy.content.contraptions.aether.ionized_bulb.IonizedBulbBlock;
import dev.Cosmos616.technomancy.content.contraptions.aether.tesla_coil.coil_topload.CoilToploadBlock;
import dev.Cosmos616.technomancy.content.contraptions.aether.tesla_coil.sparkgap.SparkGapBlock;
import dev.Cosmos616.technomancy.content.contraptions.aether.tesla_coil.tesla_primary.TeslaPrimaryBlock;
import dev.Cosmos616.technomancy.content.contraptions.aether.tesla_coil.teslastalk.TeslaStalkBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import static com.simibubi.create.AllTags.pickaxeOnly;
import static com.simibubi.create.foundation.data.BlockStateGen.simpleCubeAll;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class TMBlocks {
    private static final CreateRegistrate REGISTRATE = Technomancy.getRegistrate()
            .creativeModeTab(() -> TMItemGroups.MAIN_GROUP);

//    public static final BlockEntry<SpectreCoilBlock> SPECTRE_COIL_BLOCK = REGISTRATE
//            .block("spectre_coil", SpectreCoilBlock::new)
//            .initialProperties(SharedProperties::softMetal)
//            .properties(p -> p
//                    .sound(SoundType.NETHERITE_BLOCK)
//                    .explosionResistance(1200)
//                    .noOcclusion())
//            .transform(pickaxeOnly())
//            .blockstate(new SpectreCoilGenerator()::generate)
//            .addLayer(() -> RenderType::cutoutMipped)
//            .item()
//            .model(AssetLookup.customBlockItemModel("_", "coil_single"))
//            .build()
//            .register();

    public static final BlockEntry<BatteryBlock> BATTERY_BLOCK = REGISTRATE
            .block("battery", BatteryBlock::regular)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p
                    .sound(SoundType.NETHERITE_BLOCK)
                    .isRedstoneConductor((p1, p2, p3) -> true))
            .transform(pickaxeOnly())
            .blockstate(new BatteryGenerator()::generate)
            .addLayer(() -> RenderType::cutoutMipped)
            .item(BatteryItem::new)
            .model(AssetLookup.customBlockItemModel("_", "block_single"))
            .build()
            .register();

    public static final BlockEntry<BatteryBlock> CREATIVE_BATTERY_BLOCK = REGISTRATE
            .block("creative_battery", BatteryBlock::creative)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p
                    .sound(SoundType.NETHERITE_BLOCK)
                    .isRedstoneConductor((p1, p2, p3) -> true))
            .transform(pickaxeOnly())
            .blockstate(new BatteryGenerator("creative_")::generate)
            .addLayer(() -> RenderType::cutoutMipped)
            .item(BatteryItem::new)
            .properties(p -> p.rarity(Rarity.EPIC))
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/battery/block_single"))
                    .texture("0", p.modLoc("block/battery/creative_battery")))
//                    .texture("particle", new ResourceLocation("create", "block/creative_casing"))) //is this even needed?
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
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(TMSpriteShifts.ZIRCONIUM_CASING)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, TMSpriteShifts.ZIRCONIUM_CASING)))
            .onRegister(CreateRegistrate.blockModel(() -> CableAttachmentModel::new))
            .loot((p, b) -> p.dropOther(b, CABLE_BLOCK.get()))
            .register();

    public static final BlockEntry<CasingBlock> ZIRCONIUM_CASING = REGISTRATE
            .block("zirconium_casing", CasingBlock::new)
            .transform(BuilderTransformers.casing(() -> TMSpriteShifts.ZIRCONIUM_CASING))
            .properties(p -> p.sound(SoundType.COPPER))
            .register();

    public static final BlockEntry<CasingBlock> CERAMIC_CASING = REGISTRATE
            .block("ceramic_casing", CasingBlock::new)
            .transform(BuilderTransformers.casing(() -> TMSpriteShifts.CERAMIC_CASING))
            .properties(p -> p.sound(SoundType.METAL))
            .register();


    public static final BlockEntry<LaserBlock> LASER = REGISTRATE
            .block("laser", LaserBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties((p) -> p.sound(SoundType.NETHERITE_BLOCK))
            .blockstate((c, p) -> p.directionalBlock(c.get(), AssetLookup.partialBaseModel(c, p, "base")))
            .item()
            .transform(customItemModel("_","block_base"))
            .register();
    
    public static final BlockEntry<SoulAccumulatorBlock> SOUL_ACCUMULATOR = REGISTRATE
            .block("soul_accumulator", SoulAccumulatorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .simpleItem()
            .register();

    public static final BlockEntry<SparkGapBlock> SPARK_GAP = REGISTRATE
            .block("spark_gap",SparkGapBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .blockstate((ctx, prov) -> BlockStateGen.simpleBlock(ctx,prov,blockState -> prov.models().getExistingFile(prov.modLoc("block/"+ ctx.getName() +"/spark_gap"))))
            .simpleItem()
            .register();
    public static final BlockEntry<SoulBurnerBlock> SOUL_BURNER = REGISTRATE
            .block("soul_burner", SoulBurnerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> BlockStateGen.simpleBlock(ctx,prov,blockState -> prov.models().getExistingFile(prov.modLoc("block/soul_burner/burner_base"))))
//            .item().transform(customItemModel("soul_burner", "item"))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> SOUL_BURNER_EXTENSION = REGISTRATE //SoulBurnerExtension
            .block("soul_burner_extension", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .blockstate((ctx, prov) -> BlockStateGen.simpleBlock(ctx,prov,blockState -> prov.models().getExistingFile(prov.modLoc("block/soul_burner/burner_extension"))))
            .addLayer(() -> RenderType::cutoutMipped)
            .simpleItem()
            .register();

    public static final BlockEntry<TeslaPrimaryBlock> TESLA_PRIMARY = REGISTRATE
            .block("tesla_primary", TeslaPrimaryBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .simpleItem()
            .register();

    public static final BlockEntry<TeslaStalkBlock> TESLA_STALK = REGISTRATE
            .block("tesla_stalk", TeslaStalkBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .simpleItem()
            .register();

    public static final BlockEntry<CoilToploadBlock> COIL_TOPLOAD = REGISTRATE
            .block("coil_topload", CoilToploadBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> BlockStateGen.simpleBlock(ctx,prov,blockState -> prov.models().getExistingFile(prov.modLoc("coil_topload"))))
            .simpleItem()
            .register();

    public static final BlockEntry<IonizedBulbBlock> IONIZED_BULB = REGISTRATE
            .block("ionized_bulb", IonizedBulbBlock::new)
            .initialProperties(SharedProperties::softMetal)
           .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .addLayer(() -> RenderType::translucent)
            .simpleItem()
            .register();

    public static final BlockEntry<Block> ZIRCON_BLOCK = REGISTRATE
            .block("zircon_block", Block::new)
            .initialProperties(Material.AMETHYST)
            .blockstate(simpleCubeAll("zircon_block"))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> ZIRCONIUM_BLOCK = REGISTRATE
            .block("zirconium_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .blockstate(simpleCubeAll("zirconium_block"))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> ZIRCONIUM_OXIDIZED_BLOCK = REGISTRATE
            .block("zirconium_oxidized_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_BLACK))
            .properties(p -> p.requiresCorrectToolForDrops())
            .blockstate(simpleCubeAll("zirconium_oxidized_block"))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> ZIRCALOY_BLOCK = REGISTRATE
            .block("zircaloy_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN))
            .properties(p -> p.requiresCorrectToolForDrops())
            .blockstate(simpleCubeAll("zircaloy_block"))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> SILVER_BLOCK = REGISTRATE
            .block("silver_block", Block::new)
            .properties(p -> p.sound(SoundType.METAL))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> ENERGIZED_GOLD_BLOCK = REGISTRATE
            .block("energized_gold_block", Block::new)
       .properties(p -> p.sound(SoundType.METAL))
      .simpleItem()
            .register();


    public static void register() {}
}
