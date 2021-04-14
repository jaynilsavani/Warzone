package com.soen6441.warzone.strategyPatternTest;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.*;
import com.soen6441.warzone.strategy.AggressiveStrategy;
import com.soen6441.warzone.strategy.BenevolentStrategy;
import com.soen6441.warzone.strategy.CheaterStrategy;
import com.soen6441.warzone.strategy.RandomStrategy;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * This file contains strategy pattern tests
 *
 * @author <a href="mailto:manthan.p.moradiya@gmail.com">Manthan Moradiya</a>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class StrategyPatternTest {

    @Autowired
    GameData d_gameData;

    @Autowired
    Player d_player;

    @Autowired
    WarMap d_warMap;

    @Autowired
    GameEngine d_gameEngine;
    /**
     * This method is used to load SpringBoot Application Context
     */
    @Test
    public void contextLoads() {

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        d_gameData = d_gameEngine.loadGame("latest.txt");
        d_player = d_gameData.getD_playerList().get(0);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test to check aggressive test
     *
     */
    @Test
    public void testAggressiveStrategy()  {
        AggressiveStrategy l_aggressiveStrategy = new AggressiveStrategy();

        assertTrue(l_aggressiveStrategy.d_allowedOrders.contains(OrderTypes.DEPLOY));
        assertTrue(l_aggressiveStrategy.d_allowedOrders.contains(OrderTypes.ADVANCE));
        assertTrue(l_aggressiveStrategy.d_allowedOrders.contains(OrderTypes.BOMB));
        assertTrue(l_aggressiveStrategy.d_allowedOrders.contains(OrderTypes.AIRLIFT));

      //  System.out.println(l_aggressiveStrategy.createOrder());
    }

    /**
     * Test to check random strategy test
     *
     */
    @Test
    public void testRandomStrategy()  {
        RandomStrategy l_randomStrategy = new RandomStrategy();

        assertTrue(l_randomStrategy.d_allowedOrders.contains(OrderTypes.DEPLOY));
        assertTrue(l_randomStrategy.d_allowedOrders.contains(OrderTypes.ADVANCE));
        assertTrue(l_randomStrategy.d_allowedOrders.contains(OrderTypes.BOMB));
        assertTrue(l_randomStrategy.d_allowedOrders.contains(OrderTypes.AIRLIFT));
        assertTrue(l_randomStrategy.d_allowedOrders.contains(OrderTypes.BLOCKADE));
        assertTrue(l_randomStrategy.d_allowedOrders.contains(OrderTypes.DIPLOMACY));

        //  System.out.println(l_aggressiveStrategy.createOrder());
    }

    /**
     * Test to check benevolant strategy test
     *
     */
    @Test
    public void testBenevolantStrategy()  {
        BenevolentStrategy l_benevolentStrategy = new BenevolentStrategy();

        assertTrue(l_benevolentStrategy.d_allowedOrders.contains(OrderTypes.DEPLOY));
        assertTrue(l_benevolentStrategy.d_allowedOrders.contains(OrderTypes.ADVANCE));
        assertTrue(l_benevolentStrategy.d_allowedOrders.contains(OrderTypes.AIRLIFT));
        assertTrue(l_benevolentStrategy.d_allowedOrders.contains(OrderTypes.BLOCKADE));
        assertTrue(l_benevolentStrategy.d_allowedOrders.contains(OrderTypes.DIPLOMACY));

        //  System.out.println(l_aggressiveStrategy.createOrder());
    }

    /**
     * Test to check cheater strategy test
     *
     */
    @Test
    public void testCheaterStrategy()  {
        CheaterStrategy l_cheaterStrategy = new CheaterStrategy(d_gameData,d_player);

        assertTrue(l_cheaterStrategy.d_allowedOrders.contains(OrderTypes.DEPLOY));
        assertTrue(l_cheaterStrategy.d_allowedOrders.contains(OrderTypes.ADVANCE));
        assertTrue(l_cheaterStrategy.d_allowedOrders.contains(OrderTypes.AIRLIFT));
        assertTrue(l_cheaterStrategy.d_allowedOrders.contains(OrderTypes.BOMB));


        //  System.out.println(l_aggressiveStrategy.createOrder());
    }
}
