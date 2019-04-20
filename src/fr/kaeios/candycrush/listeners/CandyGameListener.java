package fr.kaeios.candycrush.listeners;

import fr.kaeios.candycrush.game.CandyGame;
import fr.kaeios.candycrush.game.animations.CandyComboAnimation;
import fr.kaeios.candycrush.game.elements.CandyLevel;
import fr.kaeios.candycrush.manager.CandyGameManager;
import fr.kaeios.candycrush.manager.CandyLevelManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.IntStream;

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
        if(!event.getClickedInventory().equals(player.getOpenInventory().getTopInventory())) return;
        // Only allow click on top inventory
        final CandyGame game = games.getGame(player.getUniqueId());
        final int slot = event.getSlot();
        if(clicked.getItemMeta().hasEnchant(Enchantment.DAMAGE_ALL)){
            final ItemMeta meta = clicked.getItemMeta();
            meta.removeEnchant(Enchantment.DAMAGE_ALL);
            clicked.setItemMeta(meta);
            final int column = slot%9;
            IntStream.range(0, 6).forEach(row -> menu.setItem(row*9+column, clicked));
            new CandyComboAnimation(game).start();
            return;
        }
        // Check if player can play
        if(!game.isPlayable()) return;
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
        final Player player = (Player) event.getWhoClicked();
        if(!event.getClickedInventory().equals(player.getOpenInventory().getTopInventory())) return;
        // Cancel action
        event.setCancelled(true);
        // Get level number and launch it if exist
        final int level = item.getAmount();
        if(!levels.isLevel(level) || item.getDurability() != 5) return;
        final CandyLevel candyLevel = levels.getCandyLevel(level);
        // Close inventory to prevent InventoryCloseEvent to stop the game
        event.getWhoClicked().closeInventory();
        new CandyGame(player.getUniqueId(), candyLevel).start();
    }

}
