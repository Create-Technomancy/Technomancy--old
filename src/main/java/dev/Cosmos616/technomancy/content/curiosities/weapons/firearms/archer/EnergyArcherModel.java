package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.archer;
import dev.Cosmos616.technomancy.foundation.item.TechnoCustomRenderedItemModel;
import net.minecraft.client.resources.model.BakedModel;

public class EnergyArcherModel extends TechnoCustomRenderedItemModel {
    public EnergyArcherModel(BakedModel template) {
        super(template, "energy_archer");
        addPartials("cog");
    }
}
