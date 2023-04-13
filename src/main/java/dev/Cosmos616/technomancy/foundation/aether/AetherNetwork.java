package dev.Cosmos616.technomancy.foundation.aether;

import java.util.ArrayList;
import java.util.UUID;

public class AetherNetwork {

  public static boolean SHOW_NETWORK_ID = true;
  
  ArrayList<AetherNetworkElement> networkElements = new ArrayList<>();
  
  //For debugging connectivity
  UUID networkId = UUID.randomUUID();
  
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
