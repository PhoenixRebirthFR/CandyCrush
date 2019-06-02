package fr.kaeios.candycrush.commands;

import fr.kaeios.candycrush.CandyCrush;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public final class CandyCrushCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(!(sender instanceof Player)) return true;
        if(args.length == 0 || !sender.hasPermission("candycrush.setcake")){
            CandyCrush.getInstance().openLevelMenu((Player) sender);
        }else if(args.length == 1 && args[0].equalsIgnoreCase("setcake")){
            final BlockIterator blockRange = new BlockIterator((Player) sender, 5);
            while (blockRange.hasNext()){
                final Block block = blockRange.next();
                if(block == null || block.getType().equals(Material.AIR)) continue;
                if(block.getType().equals(Material.CAKE_BLOCK)){
                    CandyCrush.getInstance().getCandyConfig().setCakeLocation(block.getLocation());
                    sender.sendMessage("§8[§eCandyCrush§8] §cLe cake a bien été placé !");
                    return true;
                }
            }
            sender.sendMessage("§8[§eCandyCrush§8] §cVous devez regarder un gateau pour faire cette commande.");
        }else{
            sender.sendMessage("§8[§eCandyCrush§8] §c/candycrush setcake &epour placer le cake.");
        }
        return true;
    }

}
