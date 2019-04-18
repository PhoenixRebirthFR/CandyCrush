package fr.kaeios.candycrush.game.sounds;

import org.bukkit.Sound;

public enum Notes {

    C(6),
    D(8),
    E(10),
    F(11),
    G(13),
    A(15);

    private final float pitch;

    Notes(final float pitch){
        this.pitch = pitch;
    }


    public float getPitch() {
        return pitch;
    }
}
