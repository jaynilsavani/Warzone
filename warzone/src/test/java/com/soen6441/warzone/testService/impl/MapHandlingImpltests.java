package com.soen6441.warzone.testService.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.soen6441.warzone.model.WarMap;
import com.soen6441.warzone.model.Continent;
import com.soen6441.warzone.model.Country;
import com.soen6441.warzone.service.impl.MapHandlingImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.util.AssertionErrors.assertTrue;
/**
 *
 * @author <a href="mailto:manthan.p.moradiya@gmail.com">Manthan Moradiya</a>
 */
public class MapHandlingImpltests {
    WarMap d_warMap= new WarMap();
   
    private MapHandlingImpl d_mapHandlingImpl = new MapHandlingImpl();
    
    /**
     * Test for check true object is return from readMap method
     */
    @Test
    void testForReadMapFromFile(){
        List<Country> l_countryList = new ArrayList();
        
        Country l_country = new Country();
        l_country.setD_continentIndex(1);
        l_country.setD_countryIndex(1);
        l_country.setD_countryName("aa");
        List<String> l_neighborList = new ArrayList();
        l_neighborList.add("bb");
          
        l_country.setD_neighbourCountries(l_neighborList);
        l_countryList.add(l_country);
        
        Country l_country1 = new Country();
        l_country1.setD_continentIndex(1);
        l_country1.setD_countryIndex(2);
        l_country1.setD_countryName("bb");
        List<String> l_neighborList1 = new ArrayList();
        l_neighborList1.add("aa");
        
        l_country1.setD_neighbourCountries(l_neighborList1);
        l_countryList.add(l_country1);
        
        Continent l_continent = new Continent();
        l_continent.setD_continentIndex(1);
        l_continent.setD_continentName("abc");
        l_continent.setD_continentValue(5);
        l_continent.setD_countryList(l_countryList);
        
        d_warMap.setD_mapName("test Map");
        Map<Integer, Continent> l_continentMap = new HashMap<Integer,Continent>();
        l_continentMap.put(1,l_continent);
        d_warMap.setD_continents(l_continentMap);
       WarMap obj = new WarMap();
       obj = d_mapHandlingImpl.readMap("test.map");
      
      assertThat(d_warMap.equals(d_mapHandlingImpl.readMap("test.map"))).isTrue();   
    }
    
    /**
     * Test to check WarMap object is successfully write to file
     */
    @Test
    void testForWriteMapToFile(){
        d_warMap = d_mapHandlingImpl.readMap("test.map");
        assertEquals(d_mapHandlingImpl.writeMapToFile(d_warMap),true);
        
    }
}
