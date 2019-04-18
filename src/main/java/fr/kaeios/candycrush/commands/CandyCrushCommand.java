package fr.kaeios.candycrush.commands;

import fr.kaeios.candycrush.CandyCrush;
import fr.kaeios.candycrush.game.CandyGame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CandyCrushCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            new CandyGame(((Player) sender).getUniqueId(), CandyCrush.getInstance().getLevels().getCandyLevel(1)).start();
        }
        return false;
    }

}
