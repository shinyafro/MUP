package org.gr1m.mc.mup;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class MupConfig {

    private Configuration config;

    public boolean mc4;
    public boolean mc2025;
    public boolean mc9568;
    public boolean mc54026;
    public boolean mc118710;
    public boolean mc119971;

    public void init(File file) {
        if (!file.exists()) {
            mc4 = true;
            mc2025 = true;
            mc9568 = true;
            mc54026 = true;
            mc118710 = true;
            mc119971 = true;
            return;
        }
        if (config == null) {
            config = new Configuration(file);
            reload();
        }
    }

    public void reload() {
        mc4      = config.get("fixes", "mc4",  true).getBoolean();
        mc2025   = config.get("fixes", "mc2025",  true).getBoolean();
        mc9568   = config.get("fixes", "mc9568",  true).getBoolean();
        mc54026  = config.get("fixes", "mc54026", true).getBoolean();
        mc118710 = config.get("fixes", "mc118710", true).getBoolean();
        mc119971 = config.get("fixes", "mc119971", true).getBoolean();
    }
}