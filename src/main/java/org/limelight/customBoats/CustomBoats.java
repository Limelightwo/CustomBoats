package org.limelight.customBoats;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


import static org.limelight.customBoats.EventListner.maxDurability;

public final class CustomBoats extends JavaPlugin {





    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = this.getConfig();

        maxDurability = config.getDouble("max-durability");

        if (maxDurability > 0.0d)
            getServer().getPluginManager().registerEvents(new EventListner(this),this);
    }

    @Override
    public void onDisable() {
        Bukkit.getAsyncScheduler().cancelTasks(this);
        Bukkit.getScheduler().cancelTasks(this);
        Bukkit.getGlobalRegionScheduler().cancelTasks(this);
    }

}
