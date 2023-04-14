package dev.Cosmos616.technomancy.foundation.aether.subtypes;

import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.foundation.aether.AetherNetworkElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AetherAccumulator extends AetherNetworkElement {
  
  int storedAether = 0;
  
  public AetherAccumulator(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }
  
  public abstract int getMaxStorage();
  
  public int getStoredAether() {
    return storedAether;
  }
  
  @Override
  public void tick() {
    super.tick();
    Technomancy.LOGGER.info(getAetherNetwork().getAccumulatorDelta());
    int contentDifference = Math.min(Math.max(getAetherNetwork().getAccumulatorDelta(), getStoredAether() - getMaxStorage()), getStoredAether());
    
    if (contentDifference > 0) {// Push, positive contentDifference
      storedAether -= contentDifference;
      getAetherNetwork().pushRequiredAccumulatorAether(contentDifference);
    } else if (contentDifference < 0) {// Pull, negative contentDifference
      int oldStored = getStoredAether();
      storedAether -= contentDifference;
      getAetherNetwork().pullAccumulatorAether(getStoredAether() - oldStored);
    }
    
  }
  
}
