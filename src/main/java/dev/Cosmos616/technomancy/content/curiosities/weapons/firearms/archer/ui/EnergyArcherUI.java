package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.archer.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.components.structureMovement.interaction.controls.*;
import com.simibubi.create.content.logistics.trains.entity.Carriage;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.networking.AllPackets;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.ControlsUtil;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.foundation.utility.placement.PlacementHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class EnergyArcherUI {
    public static final IIngameOverlay OVERLAY = EnergyArcherUI::renderOverlay;


    public static LerpedFloat displayedSpeed = LerpedFloat.linear();
    public static LerpedFloat displayedThrottle = LerpedFloat.linear();


    static Double editedThrottle = 5d;

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

        AllGuiTextures.TRAIN_HUD_FRAME.render(poseStack, -2, 1);
        AllGuiTextures.TRAIN_HUD_SPEED_BG.render(poseStack, 0, 0);

        int w = (int) (AllGuiTextures.TRAIN_HUD_SPEED.width * displayedSpeed.getValue(partialTicks));
        int h = AllGuiTextures.TRAIN_HUD_SPEED.height;

        AllGuiTextures.TRAIN_HUD_SPEED.bind();
        GuiComponent.blit(poseStack, 0, 0, 0, AllGuiTextures.TRAIN_HUD_SPEED.startX,
                AllGuiTextures.TRAIN_HUD_SPEED.startY, w, h, 256, 256);




        w = (int) (AllGuiTextures.TRAIN_HUD_THROTTLE.width * (1 - displayedThrottle.getValue(partialTicks)));
        AllGuiTextures.TRAIN_HUD_THROTTLE.bind();
        int invW = AllGuiTextures.TRAIN_HUD_THROTTLE.width - w;
        GuiComponent.blit(poseStack, invW, 0, 0, AllGuiTextures.TRAIN_HUD_THROTTLE.startX + invW,
                AllGuiTextures.TRAIN_HUD_THROTTLE.startY, w, h, 256, 256);
        AllGuiTextures.TRAIN_HUD_THROTTLE_POINTER.render(poseStack,
                Math.max(1, AllGuiTextures.TRAIN_HUD_THROTTLE.width - w) - 3, -2);



        poseStack.popPose();
    }

    public static boolean onScroll(double delta) {


        double prevThrottle = editedThrottle == null ? 1 : editedThrottle;
        editedThrottle = Mth.clamp(prevThrottle + (delta > 0 ? 1 : -1) / 18f, 1 / 18f, 1);
        return true;
    }

}
