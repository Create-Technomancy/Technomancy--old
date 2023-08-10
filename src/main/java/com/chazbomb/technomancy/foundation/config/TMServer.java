package com.chazbomb.technomancy.foundation.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class TMServer extends ConfigBase {

    @Override
    public String getName() {
        return "server";
    }

    private static class Comments {
        static String magics = "Parameters and abilities of Technomancy's magi-tech mechanisms";
    }
}
