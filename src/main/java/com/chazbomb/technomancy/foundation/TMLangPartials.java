package com.chazbomb.technomancy.foundation;

import com.chazbomb.technomancy.Technomancy;
import com.google.gson.JsonElement;
import com.simibubi.create.foundation.data.LangPartial;
import com.simibubi.create.foundation.utility.Lang;

import java.util.function.Supplier;

public enum TMLangPartials implements LangPartial {

    //ADVANCEMENTS("Advancements"),
    INTERFACE("UI & Messages"),
    //SUBTITLES("Subtitles"),
    TOOLTIPS("Item Descriptions"),
    //PONDER("Ponder Content"),

    ;

    private final String displayName;
    private final Supplier<JsonElement> provider;

    TMLangPartials(String displayName) {
        this.displayName = displayName;
        String fileName = Lang.asId(name());
        this.provider = () -> LangPartial.fromResource(Technomancy.MOD_ID, fileName);
    }

    TMLangPartials(String displayName, Supplier<JsonElement> provider) {
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
