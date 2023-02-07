package dev.Cosmos616.technomancy.foundation.block;

import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlockLoadDefaults {

    public static final int FORCED_UPDATE_VERSION = 2;

    public static final Map<ResourceLocation, Double> DEFAULT_LOADS = new HashMap<>();
    public static final Map<ResourceLocation, Double> DEFAULT_CAPACITIES = new HashMap<>();
    public static final Map<ResourceLocation, Supplier<Couple<Integer>>> GENERATOR_EMISSIONS = new HashMap<>();

    public static void setDefaultLoad(ResourceLocation blockId, double load) {
        DEFAULT_LOADS.put(blockId, load);
    }

    public static void setDefaultCapacity(ResourceLocation blockId, double potential) {
        DEFAULT_CAPACITIES.put(blockId, potential);
    }

    public static void setGeneratorEmission(ResourceLocation blockId, Supplier<Couple<Integer>> provider) {
        GENERATOR_EMISSIONS.put(blockId, provider);
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setNoLoad() {
        return setLoad(0);
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setLoad(double load) {
        return b -> {
            setDefaultLoad(new ResourceLocation(b.getOwner()
                    .getModid(), b.getName()), load);
            return b;
        };
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setCapacity(double capacity) {
        return b -> {
            setDefaultCapacity(new ResourceLocation(b.getOwner()
                    .getModid(), b.getName()), capacity);
            return b;
        };
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setGeneratorEmission(
            Supplier<Couple<Integer>> provider) {
        return b -> {
            setGeneratorEmission(new ResourceLocation(b.getOwner()
                    .getModid(), b.getName()), provider);
            return b;
        };
    }

}
