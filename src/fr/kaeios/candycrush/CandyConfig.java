package fr.kaeios.candycrush;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CandyConfig {

    private final FileConfiguration config;
    private Location cakeLocation;

    public CandyConfig(final JavaPlugin plugin){
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
        final String world = config.getString("cake.world");
        final int x = config.getInt("cake.x");
        final int y = config.getInt("cake.y");
        final int z = config.getInt("cake.z");

        this.cakeLocation = new Location(Bukkit.getWorld(world), x, y, z);
    }

    /**
     * Get location of the cake
     * @return cake location
     */
    public Location getCakeLocation() {
        return cakeLocation;
    }

    /**
     * Set location of the cake
     * @param location location of the cake
     */
    public void setCakeLocation(final Location location) {
        this.cakeLocation = location;
        saveConfig();
    }

    /**
     * Save configuration
     */
    private void saveConfig(){
        config.set("cake.world", cakeLocation.getWorld().getName());
        config.set("cake.x", cakeLocation.getBlockX());
        config.set("cake.y", cakeLocation.getBlockY());
        config.set("cake.z", cakeLocation.getBlockZ());
        CandyCrush.getInstance().saveConfig();
    }

}
