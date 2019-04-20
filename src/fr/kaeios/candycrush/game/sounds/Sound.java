package fr.kaeios.candycrush.game.sounds;

public class Sound {

    private final Notes note;
    private final int duration;

    public Sound(Notes note, int duration){
        this.note = note;
        this.duration = duration;
    }

    /**
     * Get duration of this note
     * @return duration of this sound
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Get note of this sound
     * @return Notes value
     */
    public Notes getNote() {
        return note;
    }

}
