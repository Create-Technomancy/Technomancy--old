package dev.Cosmos616.technomancy.foundation;

import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.content.contraptions.aether.battery.BatteryBlock;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TMBlockModelGenerator extends BlockModelProvider {
  
  public TMBlockModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
    super(generator, Technomancy.MOD_ID, existingFileHelper);
  }
  
  @Override
  protected void registerModels() {
  
    for (BatteryBlock.Shape shape : BatteryBlock.Shape.values()) {
      for (int i = 0; i < 9; i++) {
        getBuilder("block/battery/" + shape.getSerializedName().toLowerCase() + "_" + i)
            .parent(getExistingFile(Technomancy.asResource("block/battery/" + shape.getSerializedName().toLowerCase())))
            .texture("0", Technomancy.asResource("block/battery/" + i));
      }
      getBuilder("block/battery/" + shape.getSerializedName().toLowerCase() + "_creative")
          .parent(getExistingFile(Technomancy.asResource("block/battery/" + shape.getSerializedName().toLowerCase())))
          .texture("0", Technomancy.asResource("block/battery/creative"));
    }
  
    for (BatteryBlock.Shape shape : BatteryBlock.Shape.values()) {
      getBuilder("block/battery/end/bottom_" + shape.getSerializedName().toLowerCase() + "_creative")
          .parent(getExistingFile(Technomancy.asResource("block/battery/end/bottom_" + shape.getSerializedName().toLowerCase())))
          .texture("1", Technomancy.asResource("block/battery/end_creative"));
      getBuilder("block/battery/end/top_" + shape.getSerializedName().toLowerCase() + "_creative")
          .parent(getExistingFile(Technomancy.asResource("block/battery/end/top_" + shape.getSerializedName().toLowerCase())))
          .texture("1", Technomancy.asResource("block/battery/end_creative"));
    }
  }
  
}
