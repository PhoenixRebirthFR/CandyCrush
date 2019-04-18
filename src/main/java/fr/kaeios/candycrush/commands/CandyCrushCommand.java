package fr.kaeios.candycrush.commands;

import fr.kaeios.candycrush.CandyCrush;
import fr.kaeios.candycrush.CandyStats;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class CandyCrushCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            // Create levels menu
            final Inventory inventory = Bukkit.createInventory(null, 54, "§cLevels §e- §cCandyCrush");
            final CandyStats stats = CandyStats.getStats(((Player) sender).getUniqueId());
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
                inventory.setItem(level.getLevel()-1, new ItemStack(Material.STAINED_CLAY, level.getLevel(), (short) (stats.getCurrent() >= level.getLevel() ? 5 : 14)));
            });
            // Open inventory
            ((Player) sender).openInventory(inventory);
        }
        return false;
    }

}
