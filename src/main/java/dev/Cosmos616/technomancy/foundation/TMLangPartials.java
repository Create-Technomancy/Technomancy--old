package dev.Cosmos616.technomancy.foundation;

import com.google.gson.JsonElement;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.utility.Lang;
import dev.Cosmos616.technomancy.Technomancy;

import java.util.function.Supplier;

public enum TMLangPartials implements LangPartial {

    ADVANCEMENTS("Advancements", AllAdvancements::provideLangEntries),
    INTERFACE("UI & Messages"),
    SUBTITLES("Subtitles", AllSoundEvents::provideLangEntries),
    TOOLTIPS("Item Descriptions"),
    PONDER("Ponder Content", PonderLocalization::provideLangEntries),

    ;

    private final String displayName;
    private final Supplier<JsonElement> provider;

    private TMLangPartials(String displayName) {
        this.displayName = displayName;
        String fileName = Lang.asId(name());
        this.provider = () -> LangPartial.fromResource(Technomancy.MOD_ID, fileName);
    }

    private TMLangPartials(String displayName, Supplier<JsonElement> provider) {
        this.displayName = displayName;
        this.provider = provider;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public JsonElement provide() {
        return provider.get();
    }

}
