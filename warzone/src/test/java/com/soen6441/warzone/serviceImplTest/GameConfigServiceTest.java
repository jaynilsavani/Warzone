package com.soen6441.warzone.serviceImplTest;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.service.GameConfigService;
import com.soen6441.warzone.service.GeneralUtil;
import com.soen6441.warzone.service.MapHandlingInterface;
import com.soen6441.warzone.service.impl.MapHandlingImpl;
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

import java.io.IOException;
import java.util.*;

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
    GameData d_gameData;
    
    @Autowired
    GeneralUtil d_generalUtil;

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
        d_gameData = new GameData();
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

        Player l_expectedPlayer = new Player();
        l_expectedPlayer.setD_playerName("user");
        Player l_actualPlayer = new Player();

        Map.Entry<GameData, CommandResponse> l_gamePlayCommandResponseEntry = d_gameConfigService.updatePlayer(d_gameData, "gameplayer -add " + l_expectedPlayer.getD_playerName()+" human");
        if (l_gamePlayCommandResponseEntry.getValue().isD_isValid()) {
            GameData l_gameData = l_gamePlayCommandResponseEntry.getKey();
            if (!l_gameData.getD_playerList().isEmpty()) {
                l_actualPlayer = l_gameData.getD_playerList().get(0);
            }
        }
        assertEquals(l_expectedPlayer, l_actualPlayer);
    }

    /**
     * This method is used to test whether player is removed or not
     *
     */
    @Test
    public void testForRemovePlayer() {

        Player l_playerToRemove = new Player();
        l_playerToRemove.setD_playerName("user");
        Player l_extraPlayer = new Player();
        l_extraPlayer.setD_playerName("user1");
        List<Player> l_player = new ArrayList<>();
        l_player.add(l_playerToRemove);
        l_player.add(l_extraPlayer);
        d_gameData.setD_playerList(l_player);
        Map.Entry<GameData, CommandResponse> l_gamePlayCommandResponseEntry = d_gameConfigService.updatePlayer(d_gameData, "gameplayer -remove " + l_playerToRemove.getD_playerName());
        assertTrue(l_gamePlayCommandResponseEntry.getValue().isD_isValid());
        assertFalse(d_gameData.getD_playerList().contains(l_playerToRemove));
        assertTrue(d_gameData.getD_playerList().contains(l_extraPlayer));
    }

    /**
     * This method is used to test whether countries are assigned to player or
     * not
     *
     */
    @Test
    public void testAssignCountries() throws IOException {
        MapHandlingInterface l_mapHandlingImpl = new MapHandlingImpl();
        Player l_player1 = new Player();
        l_player1.setD_playerName("user1");
        Player l_player2 = new Player();
        l_player2.setD_playerName("user2");
        List<Player> l_player = new ArrayList<>();
        l_player.add(l_player1);
        l_player.add(l_player2);
        d_gameData.setD_playerList(l_player);
        d_gameData.setD_warMap(d_generalUtil.readMapByType("asia.map"));
        CommandResponse l_result = d_gameConfigService.assignCountries(d_gameData);
        assertTrue(l_result.isD_isValid());
        for (Player l_p : d_gameData.getD_playerList()) {
            assertTrue(l_p.getD_ownedCountries().size() > 0);
        }
    }
}
