package com.soen6441.warzone.strategyPatternTest;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * This is the test suite for strategy pattern test
 *
 * @author <a href="mailto:manthan.p.moradiya@gmail.com">Manthan Moradiya</a>
 */
@SpringBootTest
@RunWith(Suite.class)
@Suite.SuiteClasses({StrategyPatternTest.class})
public class StrategyPatternTestSuite {
    /**
     * This method is used to load Springboot Application Context
     */
    @Test
    public void contextLoads() {

    }
}