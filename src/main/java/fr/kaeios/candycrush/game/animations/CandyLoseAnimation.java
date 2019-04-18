package fr.kaeios.candycrush.game.animations;

import fr.kaeios.candycrush.game.CandyGame;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.stream.IntStream;

public class CandyLoseAnimation extends CandyAnimation{

    public CandyLoseAnimation(final CandyGame game) {
        super(game);
        setTickSpeed(3);
    }

    boolean red = false;
    int step = 0;

    @Override
    public void run() {
        if(shouldStop()) stop();
        // Blink the gui red and white
        step++;
        red = !red;
        final ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) (red ? 14 : 0));
        IntStream.range(0, 54).forEach(slot -> game.getMenu().setItem(slot, item));
        if(step == 20){
            // Stop the game after 20 frames
            game.stop();
        }
    }
}
