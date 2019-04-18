package fr.kaeios.candycrush.listeners;

import fr.kaeios.candycrush.game.CandyGame;
import fr.kaeios.candycrush.game.animations.CandyComboAnimation;
import fr.kaeios.candycrush.game.elements.CandyLevel;
import fr.kaeios.candycrush.manager.CandyGameManager;
import fr.kaeios.candycrush.manager.CandyLevelManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class CandyGameListener implements Listener {

    private final CandyGameManager games;
    private final CandyLevelManager levels;

    public CandyGameListener(final CandyGameManager games, CandyLevelManager levels) {
        this.games = games;
        this.levels = levels;
    }

    @EventHandler
    public void onQuiInventory(final InventoryCloseEvent event){
        // If player is in game & game is not stopped stop it
        if(!games.isGame(event.getPlayer().getUniqueId())) return;
        if(games.getGame(event.getPlayer().getUniqueId()).isStopped()) return;
        games.getGame(event.getPlayer().getUniqueId()).stop();
    }

    @EventHandler
    public void onClickInventory(final InventoryClickEvent event){
        // If player is in game & click something cancel action
        final Inventory menu = event.getInventory();
        if(menu == null) return;
        final Player player = (Player) event.getWhoClicked();
        if(!games.isGame(player.getUniqueId())) return;
        final ItemStack clicked = event.getCurrentItem();
        if(clicked == null) return;
        event.setCancelled(true);
        // Only allow click on top inventory
        if(!event.getClickedInventory().equals(player.getOpenInventory().getTopInventory())) return;
        final CandyGame game = games.getGame(player.getUniqueId());
        // Check if player can play
        if(!game.isPlayable()) return;
        // Check if game need to swap or to save the current slot
        final int slot = event.getSlot();
        // If player need to swap save clicked slot
        if(game.getSlotToSwap() == -1){
            game.setSlotToSwap(slot);
        }else{
            // Else try top swap
            // If player can swap add move & play animation
            if(game.canSwap(slot)){
                game.swap(slot);
                game.addMove();
                new CandyComboAnimation(game).start();
            }
            // Remove saved slot
            game.setSlotToSwap(-1);
        }
    }

    @EventHandler
    public void onClickLevelMenu(final InventoryClickEvent event){
        // Check if menu is level menu
        final Inventory menu = event.getClickedInventory();
        if(menu == null) return;
        final ItemStack item = event.getCurrentItem();
        if(item == null) return;
        if(!menu.getName().equalsIgnoreCase("§cLevels §e- §cCandyCrush")) return;
        // Cancel action
        event.setCancelled(true);
        // Get level number and launch it if exist
        final int level = item.getAmount();
        if(!levels.isLevel(level)) return;
        final CandyLevel candyLevel = levels.getCandyLevel(level);
        // Close inventory to prevent InventoryCloseEvent to stop the game
        event.getWhoClicked().closeInventory();
        new CandyGame(event.getWhoClicked().getUniqueId(), candyLevel).start();
    }

}
