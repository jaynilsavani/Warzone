package com.soen6441.warzone.serviceImplTest;

import com.soen6441.warzone.model.GamePlay;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.service.GameConfigService;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This Class will test business logic of GameConfigService.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 * @version 1.0.0
 * @see com.soen6441.warzone.service.GameConfigService
 * @see com.soen6441.warzone.service.impl.GameConfigServiceImpl
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GameConfigServiceTest {

    @Autowired
    GameConfigService d_gameConfigService;

    @Autowired
    GamePlay d_gamePlay;

    /**
     * This method is used to load SpringBoot Application Context
     */
    @Test
    public void contextLoads() {

    }

    public GameConfigServiceTest() {
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

    /**
     * This method is used to test whether player is added or not
     *
     */
    @Test
    public void testForUpdatePlayer() {

        Player expectedPlayer = new Player();
        expectedPlayer.setD_playerName("user");
        Player actualPlayer = new Player();

        GamePlay l_gamePlay = d_gameConfigService.updatePlayer(d_gamePlay, "gameplayer -add " + expectedPlayer.getD_playerName());
        if (!l_gamePlay.getPlayerList().isEmpty()) {
            actualPlayer = l_gamePlay.getPlayerList().get(0);
        }
        assertEquals(expectedPlayer, actualPlayer);
    }
}