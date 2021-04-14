package com.soen6441.warzone.serviceImplTest;

import com.soen6441.warzone.model.Continent;
import com.soen6441.warzone.model.Country;
import com.soen6441.warzone.model.WarMap;
import com.soen6441.warzone.service.GeneralUtil;
import com.soen6441.warzone.service.impl.MapHandlingImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.Test;
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
    
    
    @Autowired
    GeneralUtil d_generalUtil;

    public MapHandlingImplTest() {
    }

    /**
     * This method is used to load Springboot Application Context
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
    public void setUp() throws IOException {
        d_mapHandlingImpl.checkCommandEditMap("editmap test.map");
    }

    @After
    public void tearDown() throws IOException {
    }

    /**
     * Test to check WarMap object is successfully write to file
     * @throws java.io.IOException
     */
    @Test
    public void testForWriteMapToFile() throws IOException {
        try {
            d_warMap = d_generalUtil.readMapByType("test.map");
            d_warMap.setD_mapName("ftftf");
        } catch (IOException ex) {
            Logger.getLogger(MapHandlingImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(d_generalUtil.writeMapByType(d_warMap,false), true);

    }

    /**
     * Test for check true WarMap object is return from readMap method
     */
    @Test
    public void testForReadMapFromFile() {
        try {
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

            assertThat(d_warMap.equals(d_generalUtil.readMapByType("test.map"))).isTrue();
        } catch (IOException ex) {
            Logger.getLogger(MapHandlingImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test to check map is valid
     *
     * @throws IOException Indicates error in reading file
     */
    @Test
    public void testForValidMap() throws IOException {
        d_warMap = d_generalUtil.readMapByType("test.map");
        assertEquals(d_mapHandlingImpl.validateMap(d_warMap), true);
    }

    /**
     * Test to check map is invalid
     *
     * @throws IOException Indicates error in reading file
     */
    @Test
    public void testForInValidMap() throws IOException {
        d_warMap = d_generalUtil.readMapByType("invalid.map");
        assertEquals(d_mapHandlingImpl.validateMap(d_warMap), false);
    }

    /**
     * Test to check editmap command
     */
    @Test
    public void testForCheckCommandEditMap() {
        assertEquals(true, (d_mapHandlingImpl.checkCommandEditMap("editmap test.map")).isD_isValid());
    }

    /**
     * Test to check delete continent operation
     */
    @Test
    public void testForDeleteContinent() {
        assertEquals(true, d_mapHandlingImpl.deleteContinent("asia"));
    }

    /**
     * Test to check delete country operation
     */
    @Test
    public void testForDeleteCountry() {
        assertEquals(true, d_mapHandlingImpl.deleteCountry("india").isD_isValid());
    }

    /**
     * Test to check delete neighbor operation
     */
    @Test
    public void testForDeleteNeighbour() {
        assertEquals(true, d_mapHandlingImpl.deleteNeighbour("india", "china").isD_isValid());
    }

    /**
     * Test to check save neighbor operation
     */
    @Test
    public void testForSaveNeighbour() {
        assertEquals(false, d_mapHandlingImpl.saveNeighbour(1, 2).isD_isValid());
    }

    /**
     * Test to check save country operation
     */
    @Test
    public void testForSaveCountry() {
        boolean l_status = false;
        d_mapHandlingImpl.saveCountry("japan", 1);
        d_warMap = d_mapHandlingImpl.getWarMapObject();
        for (Map.Entry<Integer, Continent> l_entry : d_warMap.getD_continents().entrySet()) {
            Continent l_currentContinent = l_entry.getValue();
            for (Country l_currentCountry : l_currentContinent.getD_countryList()) {
                if (l_currentCountry.getD_countryName().equals("japan")) {
                    l_status = true;
                }
            }
        }
        assertEquals(true, l_status);
    }

    /**
     * Test to check save continent operation
     */
    @Test
    public void testForSaveContinent() {
        boolean l_status = false;
        d_mapHandlingImpl.saveContinent("africa", "15");
        d_warMap = d_mapHandlingImpl.getWarMapObject();
        for (Map.Entry<Integer, Continent> l_entry : d_warMap.getD_continents().entrySet()) {
            Continent l_currentContinent = l_entry.getValue();
            if (l_currentContinent.getD_continentName().equals("africa")) {
                l_status = true;
            }
        }
        assertEquals(true, l_status);
    }

}
