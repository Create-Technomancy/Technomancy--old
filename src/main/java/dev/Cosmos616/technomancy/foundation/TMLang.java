package dev.Cosmos616.technomancy.foundation;

import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import dev.Cosmos616.technomancy.Technomancy;
import net.minecraft.network.chat.TextComponent;

public class TMLang {
	public static LangBuilder lang() { return Lang.builder(Technomancy.MOD_ID); }
	public static LangBuilder translate(String key, Object... args) { return lang().translate(key, args); }
	
	public static TextComponent translateText(String key) { return new TextComponent(Technomancy.MOD_ID + key); }
}
