package dev.Cosmos616.technomancy.foundation.aether;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AetherNetwork {

  public static boolean SHOW_NETWORK_ID = true;
  
  //Simulated values, is not used for calculation,
  int production;
  int consumption;
  float fulfillment;
  int storage;
  int maxStorage;
  
  
  List<AetherNetworkElement> networkElements = new ArrayList<>();
  
  //For debugging connectivity
  UUID networkId = UUID.randomUUID();
  
  
  
  public boolean isOverloaded() {
    return consumption > production;
  }
  
  //Connectivity handling
  
  public void validateIntegrity(LevelAccessor accessor) {
    if (networkElements.size() == 0) return;
    
    List<AetherNetworkElement> visited = new ArrayList<>();
    
    seekIntegrity(networkElements.get(0).getBlockPos(), accessor, visited);
    
    List<AetherNetworkElement> safeChildren = new ArrayList<>(networkElements);
  
    safeChildren.forEach(networkBlockEntity -> {
      if (!visited.contains(networkBlockEntity))
        networkBlockEntity.clearNetwork();
    });
    safeChildren.forEach(networkBlockEntity -> {
      if (!visited.contains(networkBlockEntity))
        networkBlockEntity.getOrCreateAetherNetwork();
    });
  
    networkElements = visited;
  }
  
  private void seekIntegrity(BlockPos blockPos, LevelAccessor accessor, List<AetherNetworkElement> visited) {
    for (Direction direction : Direction.values()) {
      
      BlockEntity otherEntity = accessor.getBlockEntity(blockPos.relative(direction));
      
      if (otherEntity instanceof AetherNetworkElement networkBlockEntity && !visited.contains(networkBlockEntity)) {
        visited.add(networkBlockEntity);
        seekIntegrity(blockPos.relative(direction), accessor, visited);
      }
      
    }
  }
  
  public void addChild(AetherNetworkElement element) {
    if (element.hasAetherNetwork())
      element.getAetherNetwork().removeChild(element);
    
    element.setAetherNetwork(this);
    networkElements.add(element);
  }
  
  public void removeChild(AetherNetworkElement element) {
    networkElements.remove(element);
  }
  
}
