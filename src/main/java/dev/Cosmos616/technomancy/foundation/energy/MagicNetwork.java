package dev.Cosmos616.technomancy.foundation.energy;

import dev.Cosmos616.technomancy.content.contraptions.base.TechnomaticTileEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MagicNetwork {

    public Long id;
    public boolean initialized;
    public Map<TechnomaticTileEntity, Float> sources;
    public Map<TechnomaticTileEntity, Float> members;

    private float currentCapacity;
    private float currentLoad;
    private float unloadedCapacity;
    private float unloadedLoad;
    private int unloadedMembers;

    public MagicNetwork() {
        sources = new HashMap<>();
        members = new HashMap<>();
    }

    public void initFromTE(float maxLoad, float currentLoad, int members) {
        unloadedCapacity = maxLoad;
        unloadedLoad = currentLoad;
        unloadedMembers = members;
        initialized = true;
        updateLoad();
        updateCapacity();
    }

    public void addSilently(TechnomaticTileEntity te, float lastCapacity, float lastLoad) {
        if (members.containsKey(te))
            return;
        if (te.isSource()) {
            unloadedCapacity -= lastCapacity * getLoadMultiplierForEmission(te.getGeneratedEmission());
            float addedCapacity = te.calculateAddedLoadCapacity();
            sources.put(te, addedCapacity);
        }

        unloadedLoad -= lastLoad * getLoadMultiplierForEmission(te.getTheoreticalEmission());
        float LoadApplied = te.calculateLoadApplied();
        members.put(te, LoadApplied);

        unloadedMembers--;
        if (unloadedMembers < 0)
            unloadedMembers = 0;
        if (unloadedCapacity < 0)
            unloadedCapacity = 0;
        if (unloadedLoad < 0)
            unloadedLoad = 0;
    }

    public void add(TechnomaticTileEntity te) {
        if (members.containsKey(te))
            return;
        if (te.isSource())
            sources.put(te, te.calculateAddedLoadCapacity());
        members.put(te, te.calculateLoadApplied());
        updateFromNetwork(te);
        te.networkDirty = true;
    }

    public void updateCapacityFor(TechnomaticTileEntity te, float capacity) {
        sources.put(te, capacity);
        updateCapacity();
    }

    public void updateLoadFor(TechnomaticTileEntity te, float Load) {
        members.put(te, Load);
        updateLoad();
    }

    public void remove(TechnomaticTileEntity te) {
        if (!members.containsKey(te))
            return;
        if (te.isSource())
            sources.remove(te);
        members.remove(te);
        te.updateFromNetwork(0, 0, 0);

        if (members.isEmpty()) {
            QuantaPropagator.networks.get(te.getLevel())
                    .remove(this.id);
            return;
        }

        members.keySet()
                .stream()
                .findFirst()
                .map(member -> member.networkDirty = true);
    }

    public void sync() {
        for (TechnomaticTileEntity te : members.keySet())
            updateFromNetwork(te);
    }

    private void updateFromNetwork(TechnomaticTileEntity te) {
        te.updateFromNetwork(currentCapacity, currentLoad, getSize());
    }

    public void updateCapacity() {
        float newMaxLoad = calculateCapacity();
        if (currentCapacity != newMaxLoad) {
            currentCapacity = newMaxLoad;
            sync();
        }
    }

    public void updateLoad() {
        float newLoad = calculateLoad();
        if (currentLoad != newLoad) {
            currentLoad = newLoad;
            sync();
        }
    }

    public void updateNetwork() {
        float newLoad = calculateLoad();
        float newMaxLoad = calculateCapacity();
        if (currentLoad != newLoad || currentCapacity != newMaxLoad) {
            currentLoad = newLoad;
            currentCapacity = newMaxLoad;
            sync();
        }
    }

    public float calculateCapacity() {
        float presentCapacity = 0;
        for (Iterator<TechnomaticTileEntity> iterator = sources.keySet().iterator(); iterator.hasNext();) {
            TechnomaticTileEntity te = iterator.next();
            if (te.getLevel().getBlockEntity(te.getBlockPos()) != te) {
                iterator.remove();
                continue;
            }
            presentCapacity += getActualCapacityOf(te);
        }
        return presentCapacity + unloadedCapacity;
    }

    public float calculateLoad() {
        float presentLoad = 0;
        for (Iterator<TechnomaticTileEntity> iterator = members.keySet().iterator(); iterator.hasNext();) {
            TechnomaticTileEntity te = iterator.next();
            if (te.getLevel().getBlockEntity(te.getBlockPos()) != te) {
                iterator.remove();
                continue;
            }
            presentLoad += getActualLoadOf(te);
        }
        return presentLoad + unloadedLoad;
    }

    public float getActualCapacityOf(TechnomaticTileEntity te) {
        return sources.get(te) * getLoadMultiplierForEmission(te.getGeneratedEmission());
    }

    public float getActualLoadOf(TechnomaticTileEntity te) {
        return members.get(te) * getLoadMultiplierForEmission(te.getTheoreticalEmission());
    }

    private static float getLoadMultiplierForEmission(float emission) {
        return Math.abs(emission);
    }

    public int getSize() {
        return unloadedMembers + members.size();
    }

}
