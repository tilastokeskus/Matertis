package com.github.tilastokeskus.matertis.core;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tilastokeskus
 */
public class GameHandlerTest {
    
    private static final Logger LOGGER =
            Logger.getLogger(GameHandlerTest.class.getName());
    
    private GameHandler handler;
    
    public GameHandlerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        Game game = new Game(10, 20);
        ScoreHandler scoreHandler = new ScoreHandler();
        
        this.handler = new GameHandler(game, scoreHandler);
        CommandHandler commandHandler = new CommandHandler(
                CommandHandler.DEFAULT_COMMANDS, handler);
        this.handler.registerCommandHandler(commandHandler);
    }
    
    @After
    public void tearDown() {
//        handler.terminateGame();
    }

    @Test
    public void method_executeCommand_shouldReturnTrueWhenCommandExists() {
        assertTrue(handler.executeCommand(KeyEvent.VK_LEFT));
        assertTrue(handler.executeCommand(KeyEvent.VK_RIGHT));
        assertTrue(handler.executeCommand(KeyEvent.VK_DOWN));
        assertTrue(handler.executeCommand(KeyEvent.VK_UP));
        assertTrue(handler.executeCommand(KeyEvent.VK_SPACE));
    }

    @Test
    public void method_executeCommand_shouldReturnFalseWhenCommandDoesNotExist() {
        assertFalse(handler.executeCommand(KeyEvent.VK_0));
        assertFalse(handler.executeCommand(KeyEvent.VK_A));
        assertFalse(handler.executeCommand(KeyEvent.VK_I));
        assertFalse(handler.executeCommand(KeyEvent.VK_PLUS));
    }

    @Test
    public void method_executeCommand_shouldReturnFalseWhenGameIsPaused() {
        handler.togglePause();
        assertFalse(handler.executeCommand(KeyEvent.VK_LEFT));
        assertFalse(handler.executeCommand(KeyEvent.VK_RIGHT));
        assertFalse(handler.executeCommand(KeyEvent.VK_DOWN));
        assertFalse(handler.executeCommand(KeyEvent.VK_UP));
        assertFalse(handler.executeCommand(KeyEvent.VK_SPACE));
    }

    @Test
    public void method_executeCommand_shouldNotifyObservers() {
        MockObserver obs = new MockObserver();
        handler.addObserver(obs);
        handler.executeCommand(KeyEvent.VK_DOWN);
        
        assertTrue(obs.isUpdated);
    }
    
    @Test (timeout = 20)
    public void method_startGame_shouldScheduleRound()
            throws InterruptedException {
        class Obs implements Observer {
            int rounds = 0;
            @Override
            public void update(Observable o, Object arg) {
                switch ((String) arg) {
                    case "start":
                    case "stop":
                        break;
                    case "next":
                        rounds++;
                        break;
                    default:
                        fail("Incorrect message received:" + arg);
                }
            }            
        };
        
        setGameHandlerBaseRefreshRate(1);
        
        Obs obs = new Obs();
        handler.addObserver(obs);
        handler.startGame();
        
        while (obs.rounds < 2) {
            Thread.sleep(10);
        }
    }
    
    @Test
    public void method_startGame_shouldNotifyObserversWithStartMessage() {
        MockObserver obs = new MockObserver();
        handler.addObserver(obs);
        handler.startGame();
        assertEquals("start", obs.message);
    }
    
    @Test
    public void method_terminateGame_shouldShutdownScheduledExecutor() {
        handler.terminateGame();
        assertTrue(handler.getRoundExecutor().isShutdown());
    }
    
    @Test
    public void method_terminateGame_shouldSendStopMessageToObservers() {
        MockObserver obs = new MockObserver();
        handler.addObserver(obs);
        handler.terminateGame();
        assertEquals("stop", obs.message);
    }
    
    @Test
    public void method_togglePause_shouldTogglePauseState() {
        assertFalse(handler.isPaused());
        handler.togglePause();
        assertTrue(handler.isPaused());
        handler.togglePause();
        assertFalse(handler.isPaused());        
    }
    
    @Test
    public void method_togglePause_shouldSendPauseMessageToObserversWhenPaused() {
        MockObserver obs = new MockObserver();
        handler.addObserver(obs);
        handler.togglePause();
        assertEquals("pause", obs.message);
    }
    
    @Test
    public void method_togglePause_shouldSendResumeMessageToObserversWhenResumed() {
        MockObserver obs = new MockObserver();
        handler.addObserver(obs);
        handler.togglePause();
        handler.togglePause();
        assertEquals("resume", obs.message);
    }
    
    private void setGameHandlerBaseRefreshRate(long rate) {
        try {
            Field f = handler.getClass().getDeclaredField("baseRefreshRate");
            f.setAccessible(true);
            f.set(handler, rate);
        } catch (IllegalArgumentException | IllegalAccessException
                | NoSuchFieldException | SecurityException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    class MockObserver implements Observer {
        boolean isUpdated = false;
        String message = "";
        
        @Override
        public void update(Observable o, Object arg) {
            this.isUpdated = true;
            this.message = (String) arg;
        }
    }
}