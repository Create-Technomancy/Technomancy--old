package com.chazbomb.technomancy.foundation.keys;

import com.chazbomb.technomancy.Technomancy;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class TMKeys {
    public static final KeyMapping reload = new KeyMapping("key.technomancy.reload", InputConstants.KEY_R, "key.category.createwarfare");

    public static void register() {
        Technomancy.LOGGER.info("Registering keys!");
        ClientRegistry.registerKeyBinding(reload);
    }
}
