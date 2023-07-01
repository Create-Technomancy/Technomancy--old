package com.chazbomb.technomancy.registry;

import com.chazbomb.technomancy.Technomancy;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;

import static com.simibubi.create.foundation.block.connected.AllCTTypes.OMNIDIRECTIONAL;

public class TMSpriteShifts {

    public static final CTSpriteShiftEntry
            ZIRCONIUM_CASING = omni("zirconium_casing"),
            CERAMIC_CASING = omni("ceramic_casing")
    ;

    private static CTSpriteShiftEntry omni(String name) {
        return getCT(OMNIDIRECTIONAL, name);
    }

    public static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, Technomancy.asResource("block/" + blockTextureName), Technomancy.asResource("block/" + connectedTextureName + "_connected"));
    }

    public static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }

}
