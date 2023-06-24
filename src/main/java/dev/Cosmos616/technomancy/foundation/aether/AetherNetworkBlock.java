package dev.Cosmos616.technomancy.foundation.aether;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Signifies a block that has an associated {@link AetherNetworkElement} block element,
 * Requirement for Cable connection and validating block removal
* */
public interface AetherNetworkBlock {
  
  default void onNetworkBlockRemove(BlockState state, Level world, BlockPos pos) {
    BlockEntity te = world.getBlockEntity(pos);
    if (!(te instanceof AetherNetworkElement element))
      return;
    AetherNetwork network = element.getAetherNetwork();
    if (network != null) {
      network.removeChild(element);
      network.validateIntegrity(world);
    }
  }
  
}
