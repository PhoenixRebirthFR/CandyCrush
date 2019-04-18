package fr.kaeios.candycrush.game.animations;

import fr.kaeios.candycrush.game.CandyGame;

public class CandyFallAnimation extends CandyAnimation{

    private int idle = 0;

    public CandyFallAnimation(final CandyGame game){
        super(game);
        setTickSpeed(3);
    }

    @Override
    public void run() {
        if(shouldStop()) stop();
        // If candy can fall make them fall
        if(game.canCandyFall()){
            game.handleFallCandy();
        }else{
            idle++;
        }
        game.fillTopGaps();
        // If candy don't fall for 2 ticks
        if(idle >= 2){
            // Check win
            if(game.isWin()){
                new CandyWinAnimation(game).start();
                game.setPlayable(false);
                stop();
                return;
            }
            // Start combo animation
            new CandyComboAnimation(game).start();
        }
    }

}
