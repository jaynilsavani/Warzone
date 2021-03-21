package com.soen6441.warzone.serviceImplTest;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.*;
import com.soen6441.warzone.service.GameConfigService;
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
import static org.junit.Assert.assertTrue;

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
        l_countryList.add(l_country1);

        //added neighbour of country 
        l_country1.setD_neighbourCountries(l_neighborList1);

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
        List<Country> l_countryList1 = new ArrayList<>();
        for (Country country : l_countryList) {
            country.setD_countryIndex(0);
            l_countryList1.add(country);
        }

        d_player.setD_ownedCountries(l_countryList1);

        List<Player> l_playerList = new ArrayList<Player>();
        l_playerList.add(d_player);
        d_gameData.setD_playerList(l_playerList);
        d_gameData.setD_fileName("test.map");
        d_gameData.setD_warMap(d_warMap);

        //creating a new country object
        Country l_country2 = new Country();
        l_country2.setD_continentIndex(1);
        l_country2.setD_countryIndex(3);
        l_country2.setD_countryName("nepal");
        List<String> l_neighborList2 = new ArrayList();
        l_neighborList2.add("india");
        l_country2.setD_neighbourCountries(l_neighborList2);
        List<Country> l_countrylist2=new ArrayList<>();
        l_countrylist2.add(l_country2);
        Player l_player2=new Player();
        l_player2.setD_playerName("User2");
        l_player2.setD_ownedCountries(l_countrylist2);
        d_gameData.getD_playerList().add(l_player2);


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

    /**
     * Test to check Deploy Command
     */
    @Test
    public void testDeployCommand() {
        d_gameData.getD_playerList().get(0).setD_noOfArmies(10);
        d_orderProcessor.processOrder("deploy india 6",d_gameData);
        d_gameData.getD_playerList().get(0).issue_order();
        Order l_order=d_gameData.getD_playerList().get(0).next_order();
        boolean l_check = l_order.executeOrder();
        assertEquals(true, l_check);
        int l_countryArmies=d_gameData.getD_playerList().get(0).getD_ownedCountries().get(0).getD_noOfArmies();
        assertEquals(6,l_countryArmies);
        int l_actualArmiesinPlayer = d_gameData.getD_playerList().get(0).getD_noOfArmies();
        assertEquals(4, l_actualArmiesinPlayer);
    }

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
        Order l_order = d_gameData.getD_playerList().get(0).next_order();
        assertEquals(true, l_order.executeOrder());

        //checking number of armies after using bomb card
        for (Map.Entry<Integer, Continent> l_continent : d_gameData.getD_warMap().getD_continents().entrySet()) {
            for (Country l_countryName : l_continent.getValue().getD_countryList()) {
                if (l_countryName.getD_countryName().equals("china")) {
                    assertEquals(5, l_countryName.getD_noOfArmies());
                }
            }
        }
    }

    /**
     * Test to check Blockade Command
     */
    @Test
    public void testBlockadeCommand() {
        d_orderProcessor.processOrder("blockade china".trim(), d_gameData);
        d_gameData.getD_playerList().get(0).issue_order();
        Order l_order = d_gameData.getD_playerList().get(0).next_order();

        assertEquals(true, l_order.executeOrder());

        //checking that targeted country become neutral
        for (Player l_player : l_order.d_gameData.getD_playerList()) {
            if (l_player.getD_playerName().equals("user")) {
                assertEquals(false, l_player.getD_ownedCountries().contains("china"));
            }
        }

        //checking that number of arimes of targeted country become tripal
        for (Map.Entry<Integer, Continent> l_continent : d_gameData.getD_warMap().getD_continents().entrySet()) {
            for (Country l_countryName : l_continent.getValue().getD_countryList()) {
                if (l_countryName.getD_countryName().equals("china")) {
                    assertEquals(30, l_countryName.getD_noOfArmies());
                }
            }
        }
    }
        /*
     * Test diplomacy(Negotiate) command
     */
    @Test
    public void testNegotiateCommand() {
        Player l_player = new Player();
        l_player.setD_playerName("user2");
        l_player.setD_negotiatePlayer(d_player.getD_playerName());
        d_gameData.getD_playerList().add(l_player);

        d_orderProcessor.processOrder("negotiate user2".trim(), d_gameData);
        d_gameData.getD_playerList().get(0).issue_order();
        Order l_order = d_gameData.getD_playerList().get(0).next_order();
        assertEquals(true, l_order.executeOrder());

        for (Player l_gamePlayer: d_gameData.getD_playerList()){
            if(l_gamePlayer.getD_playerName() == l_player.getD_playerName()){
                assertTrue(l_player.getD_negotiatePlayerList().contains(d_player));
            }
            if(l_gamePlayer.getD_playerName() == d_player.getD_playerName()){
                assertTrue(d_player.getD_negotiatePlayerList().contains(l_player));
            }
        }
    }

    /**
     * Test diplomacy(advance) command
     */
    @Test
    public void testAdvanceCommand() {



        d_gameData.getD_playerList().get(0).getD_ownedCountries().get(0).getD_neighbourCountries().add("nepal");
        d_gameData.getD_playerList().get(0).getD_ownedCountries().get(0).setD_noOfArmies(7);
        d_gameData.getD_playerList().get(1).getD_ownedCountries().get(0).setD_noOfArmies(3);
        System.out.println(d_gameData.getD_playerList().get(0).getD_ownedCountries().size());
        System.out.println(d_gameData.getD_playerList().get(1).getD_ownedCountries().size());
        d_orderProcessor.processOrder("advance india nepal 5".trim(), d_gameData);
        d_gameData.getD_playerList().get(0).issue_order();
        Order l_order = d_gameData.getD_playerList().get(0).next_order();
        assertEquals(true, l_order.executeOrder());
        assertEquals(0,d_gameData.getD_playerList().get(1).getD_ownedCountries().size());
        assertEquals(3,d_gameData.getD_playerList().get(0).getD_ownedCountries().size());
        assertEquals(3,d_gameData.getD_playerList().get(0).getD_ownedCountries().get(1).getD_noOfArmies());
        assertEquals(2,d_gameData.getD_playerList().get(0).getD_ownedCountries().get(0).getD_noOfArmies());
    }

    /**
     * Test diplomacy(airlift) command
     * can advance the attack if country wherre attack should occur is not neigbour to the attacking country
     */
    @Test
    public void testAirliftCommand() {
        d_gameData.getD_playerList().get(0).getD_ownedCountries().get(0).setD_noOfArmies(7);
        d_gameData.getD_playerList().get(1).getD_ownedCountries().get(0).setD_noOfArmies(3);

        d_orderProcessor.processOrder("airlift india nepal 6".trim(), d_gameData);
        d_gameData.getD_playerList().get(0).issue_order();
        Order l_order = d_gameData.getD_playerList().get(0).next_order();
        assertEquals(true, l_order.executeOrder());

        assertEquals(0,d_gameData.getD_playerList().get(1).getD_ownedCountries().size());
        assertEquals(3,d_gameData.getD_playerList().get(0).getD_ownedCountries().size());
        assertEquals(4,d_gameData.getD_playerList().get(0).getD_ownedCountries().get(1).getD_noOfArmies());
        assertEquals(1,d_gameData.getD_playerList().get(0).getD_ownedCountries().get(0).getD_noOfArmies());
    }
}
