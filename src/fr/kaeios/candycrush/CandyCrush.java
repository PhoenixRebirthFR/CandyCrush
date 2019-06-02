package fr.kaeios.candycrush;

import fr.kaeios.candycrush.commands.CandyCrushCommand;
import fr.kaeios.candycrush.listeners.CandyGameListener;
import fr.kaeios.candycrush.manager.CandyGameManager;
import fr.kaeios.candycrush.manager.CandyLevelManager;
import fr.kaeios.candycrush.manager.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class CandyCrush extends JavaPlugin {

    private static CandyCrush instance;
    private CandyGameManager games;
    private CandyLevelManager levels;
    private final FileManager fileManager = new FileManager();
    private CandyConfig config;

    @Override
    public void onEnable() {
        instance = this;
        // Load configuration
        config = new CandyConfig(this);
        // Create game manager
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

    /**
     * Get file manager
     * @return FileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * Get configuration
     * @return CandyConfig
     */
    public CandyConfig getCandyConfig() {
        return config;
    }

    /**
     * Open level selector menu to a player
     * @param player Open menu to this player
     */
    public void openLevelMenu(final Player player){
        // Create levels menu
        final Inventory inventory = Bukkit.createInventory(null, 54, "§cLevels §e- §cCandyCrush");
        final CandyStats stats = CandyStats.getStats(player.getUniqueId());
        final ItemStack stat = new ItemStack(Material.BOOK_AND_QUILL, 1);
        final ItemMeta meta = stat.getItemMeta();
        meta.setDisplayName("§cStatistiques");

        final List<String> lore =new ArrayList<>();
        lore.add("§7Parties: §e"+ stats.getPlayed());
        lore.add("§7Victoires: §a"+ stats.getWin());
        lore.add("§7Défaites: §c"+ stats.getLose());
        lore.add("§7Meilleur Score: §e"+ stats.getBest());
        lore.add("§7Total: §e"+ stats.getTotal());

        meta.setLore(lore);
        stat.setItemMeta(meta);
        inventory.setItem(49, stat);
        // Fill inventory with levels icon
        CandyCrush.getInstance().getLevels().getLevels().forEach(level ->{
            final ItemStack icon = new ItemStack(Material.STAINED_CLAY, level.getLevel(), (short) (stats.getCurrent() >= level.getLevel() ? 5 : 14));
            final ItemMeta iconMeta = icon.getItemMeta();
            iconMeta.setDisplayName(((icon.getDurability() == 5) ? ChatColor.GREEN : ChatColor.RED) + "Niveau "+ level.getLevel());
            icon.setItemMeta(iconMeta);
            inventory.setItem(level.getLevel()-1, icon);
        });

        player.openInventory(inventory);
    }

}
