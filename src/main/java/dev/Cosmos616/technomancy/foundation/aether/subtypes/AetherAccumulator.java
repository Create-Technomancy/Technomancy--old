package dev.Cosmos616.technomancy.foundation.aether.subtypes;

import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.foundation.aether.AetherNetworkElement;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
    
    int accumulatorDischarge = getAetherNetwork().getAccumulatorDischarge();
    
    if (accumulatorDischarge > 0) {
      
      int discharge = Math.min(accumulatorDischarge, getStoredAether());
  
      storedAether -= discharge;
      onContentChange(-discharge);
      getAetherNetwork().pushRequiredAccumulatorAether(discharge);
      
    } else if (accumulatorDischarge < 0) {
      
      int oldStored = getStoredAether();
      
      int charge = Math.min(-accumulatorDischarge, getMaxStorage() - getStoredAether());
      
      storedAether += charge;
      onContentChange(charge);
      getAetherNetwork().pullAccumulatorAether(oldStored - getStoredAether());
      
    }
    
  }
  
  @Override
  protected void write(CompoundTag tag, boolean clientPacket) {
    
    tag.putInt("StoredAether", getStoredAether());
    
    super.write(tag, clientPacket);
  }
  
  @Override
  protected void read(CompoundTag tag, boolean clientPacket) {
    
    storedAether = tag.getInt("StoredAether");
    
    super.read(tag, clientPacket);
  }
  
  protected void onContentChange(int change) { }
  
}
