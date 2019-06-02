package fr.kaeios.candycrush;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CandyConfig {

    private final Location cakeLocation;

    public CandyConfig(final JavaPlugin plugin){
        plugin.saveDefaultConfig();
        final FileConfiguration config = plugin.getConfig();
        final String world = config.getString("cake.world");
        final int x = config.getInt("cake.x");
        final int y = config.getInt("cake.y");
        final int z = config.getInt("cake.z");

        this.cakeLocation = new Location(Bukkit.getWorld(world), x, y, z);
    }

    public Location getCakeLocation() {
        return cakeLocation;
    }

}
