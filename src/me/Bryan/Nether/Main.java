package me.Bryan.Nether;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.Metrics;

public class Main extends JavaPlugin{
	private int scheduleDelay = 10;
    public String teleportWorldName;
    Main main;
    public void onEnable() {
        try {
            Metrics metrics = new Metrics();
            metrics.start();
        }
        catch (IOException e) {
            this.getLogger().warning("Metrics was not able to initialize");
        }
        File configF = new File(this.getDataFolder() + "/config.yml");
        if (!configF.exists()) {
            this.getLogger().info("Generating a new configuration file...");
            this.saveDefaultConfig();
            this.getLogger().info("Done.");
        }
        this.teleportWorldName = this.getConfig().getString("teleportWorldName");
        if (this.getServer().getWorld(this.teleportWorldName) == null) {
            this.getLogger().severe("WORLD TO TELEPORT TO WAS NOT FOUND! You must supply a world even if you are not teleporting players. Check your config file to make sure everything is correct. Plugin disabled.");
            this.getServer().getPluginManager().disablePlugin((Plugin)this);
        } else {
            this.scheduleDelay = this.getConfig().getInt("checkDelay");
            if (this.scheduleDelay < 5) {
                this.scheduleDelay = 5;
            }
            this.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)((Object)new PlayerHeightCheckTask(this)), (long)this.scheduleDelay, (long)this.scheduleDelay);
        }
    }

    public void onDisable() {
    }
}

