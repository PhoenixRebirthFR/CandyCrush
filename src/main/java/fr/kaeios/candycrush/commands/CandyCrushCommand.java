package fr.kaeios.candycrush.commands;

import fr.kaeios.candycrush.CandyCrush;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class CandyCrushCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            final Inventory inventory = Bukkit.createInventory(null, 54, "§cLevels §e- §cCandyCrush");
            CandyCrush.getInstance().getLevels().getLevels().forEach(level ->{
                inventory.setItem(level.getLevel()-1, new ItemStack(Material.STAINED_CLAY, level.getLevel(), (short) 14));
            });
            ((Player) sender).openInventory(inventory);
        }
        return false;
    }

}
