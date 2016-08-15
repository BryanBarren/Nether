package me.Bryan.Nether;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerHeightCheckTask extends BukkitRunnable{
	private Main plugin;
    public static final String X_CORD = "%X_CORD%";
    public static final String Y_CORD = "%Y_CORD%";
    public static final String Z_CORD = "%Z_CORD%";
    public static final String PLAYER_NAME = "%PLAYER_NAME%";
    private String configString;
    private Boolean overrideTeleport;
    private String overrideCommand;
    private String spawnWorld;
    private int maxHeight;

    public PlayerHeightCheckTask(Main netherRoofProtect) {
        this.plugin = netherRoofProtect;
        this.configString = this.plugin.getConfig().getString("teleportMessage");
        this.overrideTeleport = this.plugin.getConfig().getBoolean("overrideTeleport");
        this.overrideCommand = this.plugin.getConfig().getString("overrideCommand");
        this.maxHeight = this.plugin.getConfig().getInt("maxNetherHeight");
        this.spawnWorld = this.plugin.teleportWorldName;
    }

    public void run() {
        for (World world : this.plugin.getServer().getWorlds()) {
            if (world == null || !world.getEnvironment().equals((Object)World.Environment.NETHER)) continue;
            for (Player player : world.getPlayers()) {
                if (player.getLocation().getY() <= (double)this.maxHeight || player.hasPermission("netherroofprotect.allow")) continue;
                this.sendMessage(this.configString, player, (int)player.getLocation().getX(), (int)player.getLocation().getY(), (int)player.getLocation().getZ());
                if (!this.overrideTeleport.booleanValue()) {
                    player.teleport(this.plugin.getServer().getWorld(this.spawnWorld).getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                    continue;
                }
                this.plugin.getLogger().info("Running command on player...");
                Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), this.processString(this.overrideCommand, player, (int)player.getLocation().getX(), (int)player.getLocation().getY(), (int)player.getLocation().getZ()));
            }
        }
    }

    private String processString(String string, Player player, int x, int y, int z) {
        return string.replace("%PLAYER_NAME%", player.getName()).replace("%X_CORD%", String.valueOf(x)).replace("%Y_CORD%", String.valueOf(y)).replace("%Z_CORD%", String.valueOf(z));
    }

    private void sendMessage(String string, Player player, int x, int y, int z) {
        player.sendMessage(this.processString(string, player, x, y, z).split("\n"));
    }


}
