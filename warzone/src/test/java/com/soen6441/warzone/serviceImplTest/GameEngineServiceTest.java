package com.soen6441.warzone.serviceImplTest;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.*;
import com.soen6441.warzone.service.GameEngineService;
import com.soen6441.warzone.service.OrderProcessor;
import com.soen6441.warzone.state.IssueOrderPhase;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

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
    OrderProcessor d_orderProcessor;

    @Autowired
    GameData d_gameData;

    @Autowired
    Player d_player;

    @Autowired
    WarMap d_warMap;

    @Autowired
    GameEngine d_gameEngine;

    public GameEngineServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * This method is used to load SpringBoot Application Context
     */
    @Test
    public void contextLoads() {

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
        l_country1.setD_noOfArmies(10);
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
        d_gameData.setD_playerList(l_playerList);
        d_gameData.setD_fileName("test.map");
        d_gameData.setD_warMap(d_warMap);

    }

    @After
    public void tearDown() {
    }

    /**
     * This method is used to test logic of assign reinforcement
     */
    @Test
    public void testAssignReinforcements() {
        IssueOrderPhase l_issueOrder = new IssueOrderPhase(d_gameEngine);
        l_issueOrder.d_gameData = d_gameData;
        l_issueOrder.assignReinforcements();
        d_gameData = l_issueOrder.d_gameData;
        //GameData l_gameData = d_gameEngineService.assignReinforcements(d_gameData);
        int l_actualnoOfArmies = d_gameData.getD_playerList().get(0).getD_noOfArmies();
        int l_expectednoOfArmies = 8;
        System.out.println(l_expectednoOfArmies + " and " + l_actualnoOfArmies);
        assertEquals(l_expectednoOfArmies, l_actualnoOfArmies);
    }


//    /**
//     * Test to check Deploy Command
//     */
//    @Test
//    public void testExecuteOrder() {
//        d_gameData.getD_playerList().get(0).setD_noOfArmies(6);
//        d_gameData.getD_playerList().get(0).setD_currentToCountry("china");
//        d_gameData.getD_playerList().get(0).setD_currentNoOfArmiesToMove(234);
//        d_gameData.getD_playerList().get(0).setD_commandtype(1);
//        d_gameData.getD_playerList().get(0).issue_order();
//        Order l_order = d_gameData.getD_playerList().get(0).getD_orders().get(0);
//        ((DeployOrder) l_order).setD_player(d_gameData.getD_playerList().get(0));
//        d_gameData.getD_playerList().remove(0);
//        d_gameData.getD_playerList().add(0, ((DeployOrder) l_order).getD_player());
//        boolean l_check = l_order.executeOrder();
//        assertEquals(true, l_check);
//        int l_actualArmiesinPlayer = d_gameData.getD_playerList().get(0).getD_noOfArmies();
//        assertEquals(0, l_actualArmiesinPlayer);
//    }
    
    /**
     * Test to check Bomb Command
     */
    @Test
    public void testBombCommand() {
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
        d_player.setD_ownedCountries(l_countryList);
     
        d_orderProcessor.processOrder("boMb china".trim(), d_gameData);
        d_gameData.getD_playerList().get(0).issue_order();
        Order d_order = d_gameData.getD_playerList().get(0).next_order();
        assertEquals(true, d_order.executeOrder());

        //cheking number of armies after using bomb card
        for (Map.Entry<Integer, Continent> l_continent : d_gameData.getD_warMap().getD_continents().entrySet()) {
            for (Country l_countryName : l_continent.getValue().getD_countryList()) {
                if (l_countryName.getD_countryName().equals("china")) {
                    assertEquals(5, l_countryName.getD_noOfArmies());
                }
            }
        }

    }

}
