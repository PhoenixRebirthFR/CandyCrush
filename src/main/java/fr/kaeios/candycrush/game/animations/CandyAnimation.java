package fr.kaeios.candycrush.game.animations;

import fr.kaeios.candycrush.CandyCrush;
import fr.kaeios.candycrush.game.CandyGame;
import org.bukkit.Bukkit;

public abstract class CandyAnimation implements Runnable{

    private int tickSpeed = 1;

    protected final CandyGame game;
    private int taskId;

    private boolean stopped = true;

    public CandyAnimation(final CandyGame game){
        this.game = game;
    }

    /**
     * Start animation
     */
    public void start(){
        stopped = false;
        game.setAnimation(this);
        game.setPlayable(false);
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(CandyCrush.getInstance(), this, tickSpeed, tickSpeed);
    }

    /**
     * Stop animation
     */
    public void stop(){
        stopped = true;
        Bukkit.getScheduler().cancelTask(taskId);
        game.setPlayable(true);
    }

    /**
     * Set speed of animation
     * @param tickSpeed speed
     */
    protected void setTickSpeed(final int tickSpeed){
        this.tickSpeed = tickSpeed;
    }

    /**
     * Check if animation should stop
     * @return true if it should stop
     */
    public boolean shouldStop(){
        return game.isStopped();
    }

    /**
     * Check if animation is stopped
     * @return true if it is
     */
    public boolean isStopped() {
        return stopped;
    }

}
