package com.soen6441.warzone.adapterpattenTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * This is the test suite for adapter pattern test
 *
 * @author <a href="mailto:manthan.p.moradiya@gmail.com">Manthan Moradiya</a>
 */
@SpringBootTest
@RunWith(Suite.class)
@Suite.SuiteClasses({adapterPatrrernTest.class})
public class adapterPatternTestSuite {

    /**
     * This method is used to load Springboot Application Context
     */
    @Test
    public void contextLoads() {

    }
}
