package com.soen6441.warzone;

import com.soen6441.warzone.adapterpattenTest.adapterPatternTestSuite;
import com.soen6441.warzone.serviceImplTest.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This is the main suite class for testcases. All testcases files for different
 * services will be mentioned here. From here we can run the test case files.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 * @version 1.0.0
 * @see com.soen6441.warzone.service
 * @see com.soen6441.warzone.service.impl
 */
@SpringBootTest
@RunWith(Suite.class)
@SuiteClasses({MapHandlingTestSuite.class, GameConfigTestSuite.class, GameEngineTestSuite.class, adapterPatternTestSuite.class})
public class WarzoneApplicationTests {

    /**
     * This method is used to load Springboot Application Context
     */
    @Test
    public void contextLoads() {

    }

}
