package com.soen6441.warzone.serviceImplTest;

import com.soen6441.warzone.model.*;
import com.soen6441.warzone.service.GameEngineService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * This Class will test business logic of GameEngineService.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 * @version 1.0.0
 * @see com.soen6441.warzone.service.GameEngineService
 * @see com.soen6441.warzone.service.impl.GameEngineServiceImpl
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GameEngineServiceTest {

    @Autowired
    GameEngineService d_gameEngineService;

    @Autowired
    GamePlay d_gamePlay;
    @Autowired
    Player d_player;

    @Autowired
    WarMap d_warMap;

    /**
     * This method is used to load SpringBoot Application Context
     */
    @Test
    public void contextLoads() {

    }

    public GameEngineServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        List<Country> l_countryList = new ArrayList();

        Country l_country = new Country();
        l_country.setD_continentIndex(1);
        l_country.setD_countryIndex(1);
        l_country.setD_countryName("india");
        List<String> l_neighborList = new ArrayList();
        l_neighborList.add("china");

        l_country.setD_neighbourCountries(l_neighborList);
        l_countryList.add(l_country);

        Country l_country1 = new Country();
        l_country1.setD_continentIndex(1);
        l_country1.setD_countryIndex(2);
        l_country1.setD_countryName("china");
        List<String> l_neighborList1 = new ArrayList();
        l_neighborList1.add("india");

        l_country1.setD_neighbourCountries(l_neighborList1);
        l_countryList.add(l_country1);

        Continent l_continent = new Continent();
        l_continent.setD_continentIndex(1);
        l_continent.setD_continentName("asia");
        l_continent.setD_continentValue(5);
        l_continent.setD_countryList(l_countryList);

        d_warMap.setD_mapName("test.map");
        d_warMap.setD_status(true);
        Map<Integer, Continent> l_continentMap = new HashMap<Integer, Continent>();
        l_continentMap.put(1, l_continent);
        d_warMap.setD_continents(l_continentMap);

        d_player.setD_playerName("user");
        d_player.setD_ownedCountries(l_countryList);
        List<Player> l_playerList = new ArrayList<Player>();
        l_playerList.add(d_player);
        d_gamePlay.setPlayerList(l_playerList);
        d_gamePlay.setFileName("test.map");
        d_gamePlay.setD_warMap(d_warMap);

    }

    @After
    public void tearDown() {
    }

    /**
     * This is used to test logic of assign reinforcement logic
     */
    @Test
    public void testAssignReinforcements() {
        GamePlay l_gamePlay = d_gameEngineService.assignReinforcements(d_gamePlay);
        int l_actualnoOfArmies = l_gamePlay.getPlayerList().get(0).getD_noOfArmies();
        int expectednoOfArmies = 8;
        assertEquals(expectednoOfArmies, l_actualnoOfArmies);
    }

}
