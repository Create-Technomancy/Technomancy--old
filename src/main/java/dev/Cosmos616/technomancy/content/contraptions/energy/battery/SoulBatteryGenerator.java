package dev.Cosmos616.technomancy.content.contraptions.energy.battery;

import com.simibubi.create.content.contraptions.fluids.tank.FluidTankBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class SoulBatteryGenerator extends SpecialBlockStateGen {

    private String prefix;

    public SoulBatteryGenerator() {
        this("");
    }

    public SoulBatteryGenerator(String prefix) {
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
        Boolean top = state.getValue(SoulBatteryBlock.TOP);
        Boolean bottom = state.getValue(SoulBatteryBlock.BOTTOM);

        String modelName = "single";
//        String modelName = "middle";
//        if (top && bottom)
//            modelName = "single";
//        else if (top)
//            modelName = "top";
//        else if (bottom)
//            modelName = "bottom";

        if (!prefix.isEmpty())
            modelName = prefix + modelName;
//            return prov.models()
//                    .withExistingParent(prefix + modelName, prov.modLoc("block/soul_battery/block_" + modelName))
//                    .texture("0", prov.modLoc("block/" + prefix + "casing"))
//                    .texture("1", prov.modLoc("block/" + prefix + "fluid_tank"))
//                    .texture("3", prov.modLoc("block/" + prefix + "fluid_tank_window"))
//                    .texture("4", prov.modLoc("block/" + prefix + "casing"))
//                    .texture("5", prov.modLoc("block/" + prefix + "fluid_tank_window_single"))
//                    .texture("particle", prov.modLoc("block/" + prefix + "fluid_tank"));

        return AssetLookup.partialBaseModel(ctx, prov, modelName);
    }

}
