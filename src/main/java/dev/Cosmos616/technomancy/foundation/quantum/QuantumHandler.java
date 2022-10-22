package dev.Cosmos616.technomancy.foundation.quantum;

import dev.Cosmos616.technomancy.Technomancy;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
/*
//Handler for LevelChunk
public class QuantumHandler implements ICapabilitySerializable<CompoundTag> {

//    public static void tick(Level level) {
//
//    }
//    public static void onChunkUnloaded(ChunkEvent.Unload event) {}
    public static void attach(AttachCapabilitiesEvent<LevelChunk> event) {
        LevelChunk chunk = event.getObject();
        if (chunk == null)
            return;
        QuantumHandler capability = new QuantumHandler();
        ResourceLocation id = Technomancy.TMLoc("quantum_charge");
        event.addCapability(id, capability);
        event.addListener(() -> {
            if (capability.energyCapability.isPresent())
                capability.energyCapability.invalidate();
        });
    }

    private final EnergyStorage handler;
    private final LazyOptional<EnergyStorage> energyCapability;

    public QuantumHandler() {
        handler = new EnergyStorage(100, 100, 100 , 100); // initial energy should be slightly random
        energyCapability = LazyOptional.of(() -> handler);
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side){
        if (capability == CapabilityEnergy.ENERGY)
            return this.energyCapability.cast();
        return LazyOptional.empty();
    }

    public CompoundTag serializeNBT(){
        return (CompoundTag) handler.serializeNBT();
    }

    public void deserializeNBT(CompoundTag nbt){
        handler.deserializeNBT(nbt);
    }
}*/
