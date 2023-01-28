package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.revolver;

import dev.Cosmos616.technomancy.foundation.item.TechnoCustomRenderedItemModel;
import net.minecraft.client.resources.model.BakedModel;

public class EnergyRevolverModel extends TechnoCustomRenderedItemModel{
    public EnergyRevolverModel(BakedModel template) {
        super(template, "energy_revolver");
        addPartials("cog");
    }
}
