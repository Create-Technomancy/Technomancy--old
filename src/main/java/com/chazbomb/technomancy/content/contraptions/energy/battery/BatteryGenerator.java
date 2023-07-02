package com.chazbomb.technomancy.content.contraptions.energy.battery;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class BatteryGenerator extends SpecialBlockStateGen {

    private String prefix;

    public BatteryGenerator() {
        this("");
    }

    public BatteryGenerator(String prefix) {
        this.prefix = prefix;
    }

    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return 0;
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                BlockState state) {
        Boolean top = state.getValue(BatteryBlock.TOP);
        Boolean bottom = state.getValue(BatteryBlock.BOTTOM);
        BatteryBlock.Shape shape = state.getValue(BatteryBlock.SHAPE);

        String shapeName = "middle";
        if (top && bottom)
            shapeName = "single";
        else if (top)
            shapeName = "top";
        else if (bottom)
            shapeName = "bottom";

        String modelName = shapeName + (shape == BatteryBlock.Shape.SINGLE ? "" : "_" + shape.getSerializedName());

        if (!prefix.isEmpty())
            return prov.models()
                .withExistingParent(prefix + modelName, prov.modLoc("block/battery/block_" + modelName))
                .texture("0", prov.modLoc("block/battery/" + prefix + "battery"));
//                .texture("particle", new ResourceLocation("create", "block/creative_casing")); // WHY IT'S MOT WORKING??

        return AssetLookup.partialBaseModel(ctx, prov, modelName);
    }

}
