package dev.Cosmos616.technomancy.foundation.item;

import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import dev.Cosmos616.technomancy.Technomancy;
import net.minecraft.client.resources.model.BakedModel;

public abstract class TechnoCustomRenderedItemModel extends CustomRenderedItemModel {
	public TechnoCustomRenderedItemModel(BakedModel template, String basePath) {
		super(template, Technomancy.MOD_ID, basePath);
	}
}
