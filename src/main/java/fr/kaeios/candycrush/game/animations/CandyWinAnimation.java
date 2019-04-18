package fr.kaeios.candycrush.game.animations;

import fr.kaeios.candycrush.game.CandyGame;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.stream.IntStream;

public class CandyWinAnimation extends CandyAnimation{

    public CandyWinAnimation(final CandyGame game) {
        super(game);
        setTickSpeed(1);
    }

    private int step = 0;

    @Override
    public void run() {
        if(shouldStop()) stop();
        step++;
        // Stop game after 65 ticks
        if(step > 65) game.stop();
        // stop filling inventory after slot 53
        if(step > 54) return;
        // Fill inventory progressively with rainbow glasses
        IntStream.range(0, step).forEach(slot -> game.getMenu().setItem(slot, getRandomGlass()));
    }

    // Get a random colored glass
    private ItemStack getRandomGlass(){
        return new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) new Random().nextInt(7));
    }

}
