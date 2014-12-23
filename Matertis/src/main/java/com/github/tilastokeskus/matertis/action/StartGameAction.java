
package com.github.tilastokeskus.matertis.action;

import com.github.tilastokeskus.matertis.core.DefaultScoreHandler;
import com.github.tilastokeskus.matertis.core.GameHandler;
import com.github.tilastokeskus.matertis.core.Game;
import com.github.tilastokeskus.matertis.core.ScoreHandler;
import com.github.tilastokeskus.matertis.ui.GameUI;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

/**
 *
 * @author tilastokeskus
 */
public class StartGameAction extends AbstractAction {

    public StartGameAction(String text) {
        super(text);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Game game = new Game(10, 20);
        ScoreHandler scoreHandler = new DefaultScoreHandler();
        GameHandler handler = new GameHandler(game, scoreHandler);
        GameUI ui = new GameUI("New Game", handler);        
        
        SwingUtilities.invokeLater(ui);
        handler.addObserver(ui);
        handler.startGame();
    }

}
