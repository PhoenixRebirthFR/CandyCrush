package fr.kaeios.candycrush.commands;

import fr.kaeios.candycrush.CandyCrush;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CandyCrushCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(sender instanceof Player)
            CandyCrush.getInstance().openLevelMenu((Player) sender);
        return false;
    }

}
