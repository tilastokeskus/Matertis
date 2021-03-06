
package com.github.tilastokeskus.matertis.core.command;

import com.github.tilastokeskus.matertis.core.GameHandler;

/**
 * Command to toggle the pause state of a game. Command is not executed if game
 * is over.
 * 
 * @author tilastokeskus
 * @see    com.github.tilastokeskus.matertis.core.Tetromino
 * @see    com.github.tilastokeskus.matertis.core.GameHandler
 */
public class PauseCommand implements Command {

    private final GameHandler gameHandler;
    
    /**
     * Constructs a pause command with the given game handler.
     * @param gameHandler game handler to be paused and resumed.
     */
    public PauseCommand(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }
    
    /**
     * {@inheritDoc Command}
     * <p>
     * Toggles the game handler's pause state.
     */
    @Override
    public void execute() {
        if (!gameHandler.getGame().isGameOver()) {
            gameHandler.togglePause();
        }
    }

}
