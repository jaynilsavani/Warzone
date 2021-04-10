package com.soen6441.warzone.adapterpattenTest;

import com.soen6441.warzone.model.Continent;
import com.soen6441.warzone.model.Country;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.model.WarMap;
import com.soen6441.warzone.service.GeneralUtil;
import com.soen6441.warzone.service.impl.MapHandlingImpl;
import com.soen6441.warzone.serviceImplTest.MapHandlingImplTest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author <a href="mailto:manthan.p.moradiya@gmail.com">Manthan Moradiya</a>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class adapterPatrrernTest {

    @Autowired
    WarMap d_warMap;

    @Autowired
    MapHandlingImpl d_mapHandlingImpl;

    @Autowired
    GeneralUtil d_generalUtil;

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
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test for check true WarMap object is return while reading from conquest
     * map file
     */
    @Test
    public void testForReadMapFromFile() throws IOException {
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
        d_warMap.setD_mapName("conquesttestmap.map");
        d_warMap.setD_status(true);
        Map<Integer, Continent> l_continentMap = new HashMap<Integer, Continent>();
        l_continentMap.put(1, l_continent);
        d_warMap.setD_continents(l_continentMap);
        assertThat(d_warMap.equals(d_generalUtil.readMapByType("conquesttestmap.map"))).isTrue();
    }

    /**
     * Test to check WarMap object is successfully write to conquest map file
     *
     * @throws java.io.IOException
     */
    @Test
    public void testForWriteMapToFile() throws IOException {
        try {
            d_warMap = d_generalUtil.readMapByType("conquesttestmap.map");
            d_warMap.setD_mapName("conquesttestmap1");
        } catch (IOException ex) {
            Logger.getLogger(MapHandlingImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(d_generalUtil.writeMapByType(d_warMap, true), true);
    }
}
