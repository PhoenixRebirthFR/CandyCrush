package fr.kaeios.candycrush.manager;

import fr.kaeios.candycrush.game.CandyGame;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CandyGameManager {

    private final Map<UUID, CandyGame> games = new HashMap<>();

    /**
     * Add a game
     * @param game game to add
     */
    public void addGame(final CandyGame game){
        games.put(game.getUuid(), game);
    }

    /**
     * Remove a game
     * @param uuid uuid of the player
     */
    public void removeGame(final UUID uuid){
        games.remove(uuid);
    }

    /**
     * Get game player is currently in.
     * @param uuid uuid of the player
     * @return CandyGame currently playing.
     */
    public CandyGame getGame(final UUID uuid){
        return games.get(uuid);
    }

    /**
     * Check if player is in game
     * @param uuid UUID of the player
     * @return true if it is
     */
    public boolean isGame(final UUID uuid){
        return games.containsKey(uuid);
    }

}
