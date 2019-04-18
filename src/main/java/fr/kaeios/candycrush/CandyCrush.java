package fr.kaeios.candycrush;

import fr.kaeios.candycrush.commands.CandyCrushCommand;
import fr.kaeios.candycrush.listeners.CandyGameListener;
import fr.kaeios.candycrush.manager.CandyGameManager;
import fr.kaeios.candycrush.manager.CandyLevelManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CandyCrush extends JavaPlugin {

    private static CandyCrush instance;
    private CandyGameManager games;
    private CandyLevelManager levels;

    @Override
    public void onEnable() {
        instance = this;
        // Créer le gestionnaire de parties
        levels = new CandyLevelManager();
        levels.loadLevels();
        games = new CandyGameManager();
        // Register command
        getCommand("candycrush").setExecutor(new CandyCrushCommand());
        // Register listeners
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new CandyGameListener(games, levels), this);
    }

    public static CandyCrush getInstance() {
        return instance;
    }

    /**
     * Get game manager
     * @return CandyGameManager
     */
    public CandyGameManager getGames() {
        return games;
    }

    /**
     * Get levels manager
     * @return CandyLevelManager
     */
    public CandyLevelManager getLevels() {
        return levels;
    }
}
