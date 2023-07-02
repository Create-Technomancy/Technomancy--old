package com.chazbomb.technomancy.registry;

import com.chazbomb.technomancy.Technomancy;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum TMGuiTextures implements ScreenElement {
    ARCHER_MAX_POWER("techno_widgets", 0, 195, 182, 5),
    ARCHER_MAX_POWER_POINTER("techno_widgets", 0, 209, 6, 9),
    ARCHER_BG("techno_widgets", 0, 190, 182, 5),
    ARCHER_FRAME("techno_widgets", 0, 200, 186, 7),
    ARCHER_POWER("techno_widgets", 0, 185, 182, 5);

    public final ResourceLocation location;
    public int width, height;
    public int startX, startY;

    private TMGuiTextures(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    private TMGuiTextures(int startX, int startY) {
        this("icons", startX * 16, startY * 16, 16, 16);
    }

    private TMGuiTextures(String location, int startX, int startY, int width, int height) {
        this(Technomancy.MOD_ID, location, startX, startY, width, height);
    }

    private TMGuiTextures(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    @OnlyIn(Dist.CLIENT)
    public void bind() {
        RenderSystem.setShaderTexture(0, location);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PoseStack ms, int x, int y) {
        bind();
        GuiComponent.blit(ms, x, y, 0, startX, startY, width, height, 256, 256);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack ms, int x, int y, GuiComponent component) {
        bind();
        component.blit(ms, x, y, startX, startY, width, height);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack ms, int x, int y, Color c) {
        bind();
        UIRenderHelper.drawColoredTexture(ms, c, x, y, startX, startY, width, height);
    }

}
