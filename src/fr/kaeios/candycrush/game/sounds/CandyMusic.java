package fr.kaeios.candycrush.game.sounds;

import fr.kaeios.candycrush.CandyCrush;
import fr.kaeios.candycrush.game.CandyGame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CandyMusic implements Runnable{

    private int tickSpeed = 1;
    private int taskId;
    private CandyGame game;

    private List<Sound> sounds = new ArrayList<>();
    private int wait = 1;
    private int index = 0;

    public CandyMusic(final CandyGame game){
        this.game = game;
        // Create the song
        sounds.add(new Sound(Notes.C, 3));
        sounds.add(new Sound(Notes.C, 3));
        sounds.add(new Sound(Notes.G, 3));
        sounds.add(new Sound(Notes.G, 3));
        sounds.add(new Sound(Notes.A, 3));
        sounds.add(new Sound(Notes.A, 3));
        sounds.add(new Sound(Notes.G, 6));
        sounds.add(new Sound(Notes.F, 3));
        sounds.add(new Sound(Notes.F, 3));
        sounds.add(new Sound(Notes.E, 3));
        sounds.add(new Sound(Notes.E, 3));
        sounds.add(new Sound(Notes.D, 3));
        sounds.add(new Sound(Notes.D, 3));
        sounds.add(new Sound(Notes.C, 6));
    }

    /**
     * Start the song
     */
    public void start(){
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(CandyCrush.getInstance(), this, tickSpeed, tickSpeed);
    }

    /**
     * Stop the song
     */
    public void stop(){
        Bukkit.getScheduler().cancelTask(taskId);
    }

    // Play the song
    @Override
    public void run() {
        wait--;
        // Wait for previous note duration
        if(wait == 0){
            // Get next sound and play it
            final Sound sound = sounds.get(index);
            final Player player = Bukkit.getPlayer(game.getUuid());
            player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BASEDRUM, 1, sound.getNote().getPitch()/100);
            wait = sound.getDuration();
            index++;
            // Play sound only once
            if(index >= sounds.size()) stop();
        }
    }
}
