package dev.Cosmos616.technomancy.content.contraptions.energy.battery;

import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import dev.Cosmos616.technomancy.foundation.energy.IAetherStorage;

public interface IMultiTileContainerAether extends IMultiBlockEntityContainer {
    default boolean hasAether() {
        return false;
    }

    default int getAetherSize() {
        return 0;
    }

    default void setAetherSize(int blocks) {}

    default IAetherStorage getAetherStorage() {
        return null;
    }
}
