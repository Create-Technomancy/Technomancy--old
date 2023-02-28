package dev.Cosmos616.technomancy.foundation.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public class TMKeys {
    public static final KeyMapping reload = new KeyMapping("key.technomancy.reload", InputConstants.KEY_R, "key.category.createwarfare");

    public static void register() {
        ClientRegistry.registerKeyBinding(reload);
    }
}
