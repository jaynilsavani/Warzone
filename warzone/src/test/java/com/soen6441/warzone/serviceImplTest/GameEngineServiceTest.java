package com.soen6441.warzone.serviceImplTest;

import com.soen6441.warzone.model.*;
import com.soen6441.warzone.service.GameEngineService;
import org.junit.After;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

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

        //creating a new country object
        Country l_country = new Country();
        l_country.setD_continentIndex(1);
        l_country.setD_countryIndex(1);
        l_country.setD_countryName("india");
        List<String> l_neighborList = new ArrayList();
        l_neighborList.add("china");

        //added neighbour of country 
        l_country.setD_neighbourCountries(l_neighborList);
        l_countryList.add(l_country);

        //creating a new country object
        Country l_country1 = new Country();
        l_country1.setD_continentIndex(1);
        l_country1.setD_countryIndex(2);
        l_country1.setD_countryName("china");
        List<String> l_neighborList1 = new ArrayList();
        l_neighborList1.add("india");

        //added neighbour of country 
        l_country1.setD_neighbourCountries(l_neighborList1);
        l_countryList.add(l_country1);

        //creating a new continent object
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
        d_gamePlay.setD_playerList(l_playerList);
        d_gamePlay.setD_fileName("test.map");
        d_gamePlay.setD_warMap(d_warMap);

    }

    @After
    public void tearDown() {
    }

    /**
     * This method is used to test logic of assign reinforcement
     */
    @Test
    public void testAssignReinforcements() {
        GamePlay l_gamePlay = d_gameEngineService.assignReinforcements(d_gamePlay);
        int l_actualnoOfArmies = l_gamePlay.getD_playerList().get(0).getD_noOfArmies();
        int l_expectednoOfArmies = 8;
        assertEquals(l_expectednoOfArmies, l_actualnoOfArmies);
    }

    @Test
    public void testExecuteOrder()
    {
        d_gamePlay.getD_playerList().get(0).setD_noOfArmies(6);
        d_gamePlay.getD_playerList().get(0).setD_currentToCountry("india");
        d_gamePlay.getD_playerList().get(0).setD_currentNoOfArmiesToMove(9);
        d_gamePlay.getD_playerList().get(0).issue_order();
        Order l_order=d_gamePlay.getD_playerList().get(0).getD_orders().get(0);
        ((DeployOrder) l_order).setD_player(d_gamePlay.getD_playerList().get(0));
        boolean l_actual=l_order.executeOrder();
        assertEquals(true,l_actual);
    }

}
