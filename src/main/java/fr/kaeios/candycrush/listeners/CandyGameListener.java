package fr.kaeios.candycrush.listeners;

import fr.kaeios.candycrush.game.CandyGame;
import fr.kaeios.candycrush.game.animations.CandyComboAnimation;
import fr.kaeios.candycrush.manager.CandyGameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class CandyGameListener implements Listener {

    private final CandyGameManager games;

    public CandyGameListener(final CandyGameManager games) {
        this.games = games;
    }

    @EventHandler
    public void onQuiInventory(final InventoryCloseEvent event){
        if(!games.isGame(event.getPlayer().getUniqueId())) return;
        if(games.getGame(event.getPlayer().getUniqueId()).isStopped()) return;
        games.getGame(event.getPlayer().getUniqueId()).stop();
    }

    @EventHandler
    public void onClickInventory(final InventoryClickEvent event){
        final Inventory menu = event.getInventory();
        if(menu == null) return;
        final Player player = (Player) event.getWhoClicked();
        if(!games.isGame(player.getUniqueId())) return;
        final ItemStack clicked = event.getCurrentItem();
        if(clicked == null) return;
        event.setCancelled(true);
        if(!event.getClickedInventory().equals(player.getOpenInventory().getTopInventory())) return;
        final CandyGame game = games.getGame(player.getUniqueId());
        if(!game.isPlayable()) return;
        final int slot = event.getSlot();
        if(game.getSlotToSwap() == -1){
            game.setSlotToSwap(slot);
        }else{
            if(game.canSwap(slot)){
                game.swap(slot);
                game.addMove();
                new CandyComboAnimation(game).start();
            }
            game.setSlotToSwap(-1);
        }

    }

}
