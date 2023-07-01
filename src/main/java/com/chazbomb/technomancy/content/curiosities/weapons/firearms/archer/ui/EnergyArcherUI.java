package com.chazbomb.technomancy.content.curiosities.weapons.firearms.archer.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.chazbomb.technomancy.registry.TMGuiTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class EnergyArcherUI {
    public static final IIngameOverlay OVERLAY = EnergyArcherUI::renderOverlay;


    public static LerpedFloat displayedSpeed = LerpedFloat.linear();
    public static LerpedFloat displayedThrottle = LerpedFloat.linear();


    static Double editedThrottle = 1d;

    public static int charge=0;

    public static boolean charged=false;

    public static boolean toRender = true;





    public static void tick() {



        displayedThrottle.chase(editedThrottle != null ? editedThrottle : 1, .75f, LerpedFloat.Chaser.EXP);
        displayedThrottle.tickChaser();



        displayedSpeed.chase(displayedThrottle.getValue()*charge, .03f, LerpedFloat.Chaser.LINEAR);
        displayedSpeed.tickChaser();

        if(displayedSpeed.getValue()>=displayedThrottle.getValue()){
            charged=true;
        } else {
            charged=false;
        }


    }



    public static void renderOverlay(ForgeIngameGui gui, PoseStack poseStack, float partialTicks, int width,
                                     int height) {

        if(!toRender)
            return;


        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        Entity cameraEntity = Minecraft.getInstance()
                .getCameraEntity();
        if (cameraEntity == null)
            return;


        poseStack.pushPose();
        poseStack.translate(width / 2 - 91, height - 29, 0);

        // Speed, Throttle

        TMGuiTextures.ARCHER_FRAME.render(poseStack, -2, 1);
        TMGuiTextures.ARCHER_BG.render(poseStack, 0, 0);

        int w = (int) (TMGuiTextures.ARCHER_POWER.width * displayedSpeed.getValue(partialTicks));
        int h = TMGuiTextures.ARCHER_POWER.height;

        TMGuiTextures.ARCHER_POWER.bind();
        GuiComponent.blit(poseStack, 0, 0, 0, TMGuiTextures.ARCHER_POWER.startX,
                TMGuiTextures.ARCHER_POWER.startY, w, h, 256, 256);




        w = (int) (TMGuiTextures.ARCHER_MAX_POWER.width * (1 - displayedThrottle.getValue(partialTicks)));
        TMGuiTextures.ARCHER_MAX_POWER.bind();
        int invW = TMGuiTextures.ARCHER_MAX_POWER.width - w;
        GuiComponent.blit(poseStack, invW, 0, 0, TMGuiTextures.ARCHER_MAX_POWER.startX + invW,
                TMGuiTextures.ARCHER_MAX_POWER.startY, w, h, 256, 256);
        TMGuiTextures.ARCHER_MAX_POWER_POINTER.render(poseStack,
                Math.max(1, TMGuiTextures.ARCHER_MAX_POWER.width - w) - 3, -2);



        poseStack.popPose();
    }

    public static boolean onScroll(double delta) {


        double prevThrottle = editedThrottle == null ? 1 : editedThrottle;
        editedThrottle = Mth.clamp(prevThrottle + (delta > 0 ? 1 : -1) / 18f, 1 / 18f, 1);
        return true;
    }

}
