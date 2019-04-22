package fr.kaeios.candycrush.game.sounds;

public enum Notes {

    // Each notes playable
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

    /**
     * Get pitch needed to play this note
     * @return pitch of the note
     */
    public float getPitch() {
        return pitch;
    }
}
