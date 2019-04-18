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
        if(step > 60) game.stop();
        if(step > 54) return;
        IntStream.range(0, step).forEach(slot -> game.getMenu().setItem(slot, getRandomGlass()));
    }

    private ItemStack getRandomGlass(){
        return new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) new Random().nextInt(7));
    }

}
