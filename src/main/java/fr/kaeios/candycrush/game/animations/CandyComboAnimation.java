package fr.kaeios.candycrush.game.animations;

import fr.kaeios.candycrush.CandyStats;
import fr.kaeios.candycrush.game.CandyGame;
import fr.kaeios.candycrush.game.elements.CandyType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.IntStream;

public class CandyComboAnimation extends CandyAnimation {

    public CandyComboAnimation(final CandyGame game) {
        super(game);
        setTickSpeed(3);
    }

    private int step = 0;

    @Override
    public void run() {
        if(shouldStop()) stop();
        // If no combos are possibles
        if(game.getPossibleCombos().size() == 0){
            // Check lose
            if(!game.hasMoves() && !game.isWin()){
                new CandyLoseAnimation(game).start();
                game.updateStats();
            }
            // Stop animation
            stop();
            return;
        }
        // Make combo bright
        if(step%2 == 0){
            // Get all combos
            game.getPossibleCombos().forEach(combo ->{
                // get all candies in the combo
                combo.getCandies().forEach(slot ->{
                    // Enchant all the candies
                    final ItemStack item = game.getMenu().getItem(slot);
                    final ItemMeta meta = item.getItemMeta();
                    meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.setItemMeta(meta);
                });
                if(combo.getCandies().size() == 5){
                    final ItemStack item = game.getMenu().getItem(combo.getCandies().get(3));
                    final ItemMeta meta = item.getItemMeta();
                    meta.removeEnchant(Enchantment.ARROW_FIRE);
                    meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.setItemMeta(meta);
                }
                // Update player inventory to see enchantment properly
                final Player player = Bukkit.getPlayer(game.getUuid());
                player.updateInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
            });
        }else if(step%2 == 1){
            // Remove bright item
            IntStream.range(0, 54).forEach(slot ->{
                if(game.getMenu().getItem(slot) != null && game.getMenu().getItem(slot).getItemMeta().hasEnchant(Enchantment.ARROW_FIRE)){
                    game.addPoint(CandyType.getTypeOf(game.getMenu().getItem(slot)));
                    game.updateStatsItem();
                    game.getMenu().setItem(slot, null);
                }
            });
            stop();
            // Start fall animation
            new CandyFallAnimation(game).start();
        }
        step++;
    }
}
