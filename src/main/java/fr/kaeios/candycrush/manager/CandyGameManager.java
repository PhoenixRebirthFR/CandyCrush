package fr.kaeios.candycrush.manager;

import fr.kaeios.candycrush.game.CandyGame;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CandyGameManager {

    private final Map<UUID, CandyGame> games = new HashMap<>();

    public void addGame(final CandyGame game){
        games.put(game.getUuid(), game);
    }

    public void removeGame(final UUID uuid){
        games.remove(uuid);
    }

    public CandyGame getGame(final UUID uuid){
        return games.get(uuid);
    }

    public boolean isGame(final UUID uuid){
        return games.containsKey(uuid);
    }

}
