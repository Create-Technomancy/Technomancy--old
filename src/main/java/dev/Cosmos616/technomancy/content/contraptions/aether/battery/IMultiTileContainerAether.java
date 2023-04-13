package dev.Cosmos616.technomancy.content.contraptions.aether.battery;

import com.simibubi.create.foundation.tileEntity.IMultiTileContainer;
import dev.Cosmos616.technomancy.foundation.energy.IAetherStorage;

public interface IMultiTileContainerAether extends IMultiTileContainer {
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
