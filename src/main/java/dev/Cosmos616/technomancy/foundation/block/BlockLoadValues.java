package dev.Cosmos616.technomancy.foundation.block;

import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class BlockLoadValues {

    private static final Map<String, ILoadValueProvider> PROVIDERS = new HashMap<>();

    public static void registerProvider(String namespace, ILoadValueProvider provider) {
        PROVIDERS.put(namespace, provider);
    }

    @Nullable
    public static ILoadValueProvider getProvider(String namespace) {
        return PROVIDERS.get(namespace);
    }

    @Nullable
    public static ILoadValueProvider getProvider(Block block) {
        return getProvider(RegisteredObjects.getKeyOrThrow(block)
                .getNamespace());
    }

    public static double getLoad(Block block) {
        ResourceLocation blockId = RegisteredObjects.getKeyOrThrow(block);
        ILoadValueProvider provider = getProvider(blockId.getNamespace());
        if (provider != null) {
            return provider.getLoad(block);
        }
        Double defaultLoad = BlockLoadDefaults.DEFAULT_LOADS.get(blockId);
        if (defaultLoad != null) {
            return defaultLoad;
        }
        return 0;
    }

    public static double getCapacity(Block block) {
        ResourceLocation blockId = RegisteredObjects.getKeyOrThrow(block);
        ILoadValueProvider provider = getProvider(blockId.getNamespace());
        if (provider != null) {
            return provider.getCapacity(block);
        }
        Double defaultCapacity = BlockLoadDefaults.DEFAULT_CAPACITIES.get(blockId);
        if (defaultCapacity != null) {
            return defaultCapacity;
        }
        return 0;
    }

    public static boolean hasLoad(Block block) {
        ResourceLocation blockId = RegisteredObjects.getKeyOrThrow(block);
        ILoadValueProvider provider = getProvider(blockId.getNamespace());
        if (provider != null) {
            return provider.hasLoad(block);
        }
        return BlockLoadDefaults.DEFAULT_LOADS.containsKey(blockId);
    }

    public static boolean hasCapacity(Block block) {
        ResourceLocation blockId = RegisteredObjects.getKeyOrThrow(block);
        ILoadValueProvider provider = getProvider(blockId.getNamespace());
        if (provider != null) {
            return provider.hasCapacity(block);
        }
        return BlockLoadDefaults.DEFAULT_CAPACITIES.containsKey(blockId);
    }

    public interface ILoadValueProvider {
        /**
         * Gets the energy load of a block.
         *
         * @param block The block.
         * @return the energy load value of the block, or 0 if it does not have one.
         */
        double getLoad(Block block);

        /**
         * Gets the energy capacity of a block.
         *
         * @param block The block.
         * @return the energy capacity value of the block, or 0 if it does not have one.
         */
        double getCapacity(Block block);

        boolean hasLoad(Block block);

        boolean hasCapacity(Block block);

        /**
         *
         * @param block ???
         * @return min, max generated Current; null if block does not have energy capacity
         */
        @Nullable
        Couple<Integer> getGeneratedFlow(Block block);
    }

}
