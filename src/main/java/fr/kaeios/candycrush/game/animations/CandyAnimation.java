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

    public void start(){
        stopped = false;
        game.setPlayable(false);
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(CandyCrush.getInstance(), this, tickSpeed, tickSpeed);
        game.setAnimation(this);
    }

    public void stop(){
        stopped = true;
        Bukkit.getScheduler().cancelTask(taskId);
        game.setPlayable(true);
    }

    protected void setTickSpeed(final int tickSpeed){
        this.tickSpeed = tickSpeed;
    }

    public boolean shouldStop(){
        return game.isStopped();
    }

    public boolean isStopped() {
        return stopped;
    }

}
