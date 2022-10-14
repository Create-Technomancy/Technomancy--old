package dev.Cosmos616.technomancy.registry;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import dev.Cosmos616.technomancy.Technomancy;

import static com.simibubi.create.foundation.block.connected.AllCTTypes.OMNIDIRECTIONAL;

public class TMSpriteShifts {

    public static final CTSpriteShiftEntry
            CABLE_CASING = omni("cable_casing");

    private static CTSpriteShiftEntry omni(String name) {
        return getCT(OMNIDIRECTIONAL, name);
    }

    public static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, Technomancy.TMLoc("block/" + blockTextureName), Technomancy.TMLoc("block/" + connectedTextureName + "_connected"));
    }

    public static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }

}
