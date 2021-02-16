package com.soen6441.warzone.serviceImplTest;

import com.soen6441.warzone.model.Continent;
import com.soen6441.warzone.model.Country;
import com.soen6441.warzone.model.WarMap;
import com.soen6441.warzone.service.impl.MapHandlingImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This test method will test business logic of MapHandlingImpl. This class will
 * check map validity and other functionalities related to map Handling file
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 * @version 1.0.0
 * @see com.soen6441.warzone.service.MapHandlingInterface
 * @see com.soen6441.warzone.service.impl.MapHandlingImpl
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MapHandlingImplTest {

    @Autowired
    WarMap d_warMap;

    @Autowired
    MapHandlingImpl d_mapHandlingImpl;

    public MapHandlingImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test to check WarMap object is successfully write to file
     */
    @Test
    void testForWriteMapToFile() {
        d_warMap = d_mapHandlingImpl.readMap("test.map");
        assertEquals(d_mapHandlingImpl.writeMapToFile(d_warMap), true);

    }

    /**
     * Test for check true object is return from readMap method
     */
    @Test
    void testForReadMapFromFile() {
        List<Country> l_countryList = new ArrayList();

        Country l_country = new Country();
        l_country.setD_continentIndex(1);
        l_country.setD_countryIndex(1);
        l_country.setD_countryName("india");
        List<String> l_neighborList = new ArrayList();
        l_neighborList.add("japan");

        l_country.setD_neighbourCountries(l_neighborList);
        l_countryList.add(l_country);

        Country l_country1 = new Country();
        l_country1.setD_continentIndex(1);
        l_country1.setD_countryIndex(2);
        l_country1.setD_countryName("japan");
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
        WarMap obj = new WarMap();
        obj = d_mapHandlingImpl.readMap("test.map");

        assertThat(d_warMap.equals(d_mapHandlingImpl.readMap("test.map"))).isTrue();
    }

}
