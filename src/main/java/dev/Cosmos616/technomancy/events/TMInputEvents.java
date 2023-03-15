package dev.Cosmos616.technomancy.events;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.components.structureMovement.interaction.controls.TrainHUD;
import com.simibubi.create.foundation.tileEntity.behaviour.filtering.FilteringHandler;
import com.simibubi.create.foundation.tileEntity.behaviour.scrollvalue.ScrollValueHandler;
import dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.archer.ui.EnergyArcherUI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.archer.ui.EnergyArcherUI.toRender;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class TMInputEvents {
    @SubscribeEvent
    public static void onMouseScrolled(InputEvent.MouseScrollEvent event) {
        if (Minecraft.getInstance().screen != null)
            return;

        double delta = event.getScrollDelta();
        Minecraft mc = Minecraft.getInstance();
        if(mc.player.isShiftKeyDown()&&toRender) {
            boolean cancelled = EnergyArcherUI.onScroll(delta);
            event.setCanceled(cancelled);
        }
    }
}
