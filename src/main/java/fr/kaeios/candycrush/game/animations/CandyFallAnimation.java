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
        if(game.canCandyFall()){
            game.handleFallCandy();
        }else{
            idle++;
        }
        game.fillTopGaps();
        if(idle >= 2){
            if(game.isWin()){
                new CandyWinAnimation(game).start();
                game.setPlayable(false);
                stop();
                return;
            }
            stop();
            new CandyComboAnimation(game).start();
        }
    }

}
