package org.limelight.customBoats;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


import static org.limelight.customBoats.EventListner.maxDurability;

public final class CustomBoats extends JavaPlugin {





    @Override
    public void onEnable() {
        FileConfiguration config = this.getConfig();
        if (config.getDouble("maxDurability") == 0.0) {
            config.set("maxDurability", 250.0);
            saveConfig();
        }
        maxDurability = config.getDouble("maxDurability");
        getServer().getPluginManager().registerEvents(new EventListner(),this);
    }

    @Override
    public void onDisable() {

    }

}
