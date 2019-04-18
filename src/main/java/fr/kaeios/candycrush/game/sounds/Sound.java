package fr.kaeios.candycrush.game.sounds;

public class Sound {

    private final Notes note;
    private final int duration;

    public Sound(Notes note, int duration){
        this.note = note;
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public Notes getNote() {
        return note;
    }

}
