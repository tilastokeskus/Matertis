
package com.github.tilastokeskus.matertis.action;

import com.github.tilastokeskus.matertis.SettingsManager;
import com.github.tilastokeskus.matertis.core.CommandHandler;
import com.github.tilastokeskus.matertis.core.DefaultScoreHandler;
import com.github.tilastokeskus.matertis.core.GameHandler;
import com.github.tilastokeskus.matertis.core.Game;
import com.github.tilastokeskus.matertis.core.ScoreHandler;
import com.github.tilastokeskus.matertis.ui.GameUI;
import com.github.tilastokeskus.matertis.ui.UI;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

/**
 *
 * @author tilastokeskus
 */
public class StartGameAction extends AbstractAction {

    private final UI parent;
    
    /**
     * Constructs a StartGameAction with given name and parent UI. Name will be
     * set as a button's label, if applied on a button.
     * 
     * @param name   Name of the action.
     * @param parent UI object to be closed after game has started.
     */
    public StartGameAction(String name, UI parent) {
        super(name);
        this.parent = parent;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Game game = new Game(10, 20);
        ScoreHandler scoreHandler = new DefaultScoreHandler();
        CommandHandler commandHandler = SettingsManager.getCommandHandler();
        
        GameHandler handler = new GameHandler(game,
                                              scoreHandler,
                                              commandHandler);
        
        GameUI ui = new GameUI("New Game", handler);        
        
        SwingUtilities.invokeLater(ui);
        handler.addObserver(ui);
        handler.startGame();
        
        if (this.parent != null) {
            this.parent.close();
        }
    }

}
