package dev.Cosmos616.technomancy.content.curiosities.weapons.firearms.repeater;

import dev.Cosmos616.technomancy.foundation.item.TechnoCustomRenderedItemModel;
import net.minecraft.client.resources.model.BakedModel;

public class EnergyRepeaterModel extends TechnoCustomRenderedItemModel {
	public EnergyRepeaterModel(BakedModel template) {
		super(template, "energy_repeater");
		addPartials("cog");
		addPartials("rails");
	}
}
