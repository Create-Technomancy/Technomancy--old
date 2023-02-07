package dev.Cosmos616.technomancy.foundation.energy;

import com.simibubi.create.foundation.utility.WorldHelper;
import dev.Cosmos616.technomancy.Technomancy;
import dev.Cosmos616.technomancy.content.contraptions.base.TechnomaticTileEntity;
import net.minecraft.world.level.LevelAccessor;

import java.util.HashMap;
import java.util.Map;

public class QuantaPropagator {

    static Map<LevelAccessor, Map<Long, MagicNetwork>> networks = new HashMap<>();

    public void onLoadWorld(LevelAccessor world) {
        networks.put(world, new HashMap<>());
        Technomancy.LOGGER.debug("Prepared Magic Network Space for " + WorldHelper.getDimensionID(world));
    }

    public void onUnloadWorld(LevelAccessor world) {
        networks.remove(world);
        Technomancy.LOGGER.debug("Removed Magic Network Space for " + WorldHelper.getDimensionID(world));
    }

    public MagicNetwork getOrCreateNetworkFor(TechnomaticTileEntity te) {
        Long id = te.network;
        MagicNetwork network;
        Map<Long, MagicNetwork> map = networks.computeIfAbsent(te.getLevel(), $ -> new HashMap<>());
        if (id == null)
            return null;

        if (!map.containsKey(id)) {
            network = new MagicNetwork();
            network.id = te.network;
            map.put(id, network);
        }
        network = map.get(id);
        return network;
    }

}
