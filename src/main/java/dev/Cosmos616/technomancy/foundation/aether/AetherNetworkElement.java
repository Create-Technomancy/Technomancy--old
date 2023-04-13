package dev.Cosmos616.technomancy.foundation.aether;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AetherNetworkElement extends SmartTileEntity implements IHaveGoggleInformation {
  
  @Nullable
  AetherNetwork aetherNetwork;
  
  public AetherNetworkElement(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }
  
  @Override
  public void tick() {
    if (aetherNetwork == null) {
      aetherNetwork = getOrCreateAetherNetwork();
    }
    
    super.tick();
  }
  
  @Override
  public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
    if (AetherNetwork.SHOW_NETWORK_ID && aetherNetwork != null) {
      Lang.text("Network ID: " + aetherNetwork.networkId.toString()).forGoggles(tooltip);
      return true;
    }
    return false;
  }
  
  /**Will look around for available networks, and resolve issues such as merging networks*/
  public void updateConnectedNetwork() {
    for (Direction direction : Direction.values()) {
  
      BlockPos otherPos = getBlockPos().relative(direction);
      BlockEntity otherEntity = level.getBlockEntity(otherPos);
  
      if (otherEntity instanceof AetherNetworkElement otherElement) {
        if (aetherNetwork == null && otherElement.hasAetherNetwork()) {
          aetherNetwork = otherElement.getAetherNetwork();
        } else if (aetherNetwork != otherElement.getAetherNetwork()) {
          aetherNetwork.addChild(otherElement);
          otherElement.updateConnectedNetwork();
        }
      }
      
    }
  }
  
  @Override
  public void setRemoved() {
    super.setRemoved();
    if (aetherNetwork != null) {
      aetherNetwork.removeChild(this);
      aetherNetwork.validateIntegrity(level);
    }
  }
  
  /**Returns the current network, or will look for a valid one to connect to, or will create one*/
  public AetherNetwork getOrCreateAetherNetwork() {
    if (aetherNetwork == null)
      updateConnectedNetwork();
    if (aetherNetwork == null)
      new AetherNetwork().addChild(this);
    
    return aetherNetwork;
  }
  
  public AetherNetwork getAetherNetwork() {
    return aetherNetwork;
  }
  
  public boolean hasAetherNetwork() {
    return aetherNetwork != null;
  }
  
  public void setAetherNetwork(AetherNetwork aetherNetwork) {
    this.aetherNetwork = aetherNetwork;
  }
  
  /**Nullify the current network*/
  public void clearNetwork() {
    aetherNetwork = null;
  }
  
}
