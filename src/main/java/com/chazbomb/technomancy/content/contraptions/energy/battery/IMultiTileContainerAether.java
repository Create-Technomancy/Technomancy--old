package com.chazbomb.technomancy.content.contraptions.energy.battery;

import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.chazbomb.technomancy.foundation.energy.IAetherStorage;

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
