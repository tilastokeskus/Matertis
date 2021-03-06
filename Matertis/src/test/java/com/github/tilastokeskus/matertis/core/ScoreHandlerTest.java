package com.github.tilastokeskus.matertis.core;

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
public class ScoreHandlerTest {
    
    public ScoreHandlerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void constructor_shouldSetScoreToZero() {
        AbstractScoreHandler handler = new ScoreHandler();
        assertEquals(0, handler.getScore());
    }

    @Test
    public void method_notifyLinesCleared_shouldUpdateScoreAndLevelCorrectly() {
        ScoreHandler handler = new ScoreHandler();
        int score = 0;
        
        for (int i = 1; i <= 100; i++) {            
            assertEquals(i - 1, handler.getLevel());
            
            for (int cleared = 1; cleared <= 4; cleared++) {
                handler.notifyLinesCleared(cleared);
            }
            
            score += i * (1 + 3 + 5 + 8);
            assertEquals(score, handler.getScore());
            
            handler.levelUp();
        }
        
    }
}
