package com.chazbomb.technomancy.events;


import com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer.ui.EnergyArcherUI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class TMInputEvents {
    @SubscribeEvent
    public static void onMouseScrolled(InputEvent.MouseScrollEvent event) {
        if (Minecraft.getInstance().screen != null)
            return;

        double delta = event.getScrollDelta();
        Minecraft mc = Minecraft.getInstance();
        if(mc.player.isShiftKeyDown()&& EnergyArcherUI.toRender) {
            boolean cancelled = EnergyArcherUI.onScroll(delta);
            event.setCanceled(cancelled);
        }
    }
}
