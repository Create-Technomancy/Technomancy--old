package dev.Cosmos616.technomancy.content.contraptions.aether.battery;

import dev.Cosmos616.technomancy.foundation.aether.subtypes.AetherProducer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CreativeBatteryBlockEntity extends BatteryBlockEntity implements AetherProducer {
  
  static final int MILLION = 1000000;
  
  public CreativeBatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }
  
  @Override
  public int getProducedAether() {
    return MILLION;
  }
  
  @Override
  public int getMaxStorage() {
    return MILLION;
  }
  
  @Override
  public int getStoredAether() {
    return MILLION;
  }
}
