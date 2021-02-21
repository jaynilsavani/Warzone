package com.soen6441.warzone;

import com.soen6441.warzone.serviceImplTest.MapHandlingImplTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This is the main suite class for textcases. All testcases files for different
 * services will be mentioned here. From here we can run the test case files.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 * @version 1.0.0
 * @see com.soen6441.warzone.service
 * @see com.soen6441.warzone.service.impl
 */
@SpringBootTest
@RunWith(Suite.class)
@SuiteClasses({MapHandlingImplTest.class})
public class WarzoneApplicationTests {

    /**
     * This method is used to load Springboot Apllication Context
     */
    @Test
    public void contextLoads() {

    }

}
