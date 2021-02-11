package com.soen6441.warzone.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.soen6441.warzone.service.MapHandlingInterface;
import com.soen6441.warzone.model.WarMap;
import com.soen6441.warzone.model.Continent;
import com.soen6441.warzone.model.Country;

/**
 * This is the implementation class of MapHandlingInterface having business
 * logic of map management including create map, edit map and validate map etc.
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">Yashkumar Vaghani</a>
 *
 */
public class MapHandlingImpl implements MapHandlingInterface {

    public static final String MAP_DEF_PATH = "src/main/resources/maps";

    public static final String NAME = "[name]";
    public static final String FILES = "[files]";
    public static final String CONTINENTS = "[continents]";
    public static final String COUNTRIES = "[countries]";
    public static final String BORDERS = "[borders]";

    @Override
    public boolean validateCommand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return WarMap object
     */
    @Override
    public WarMap readMap() {

        String l_fileLine = "";
        boolean l_isContinents = false;
        boolean l_isCountries = false;
        boolean l_isBorders = false;
        WarMap warMap = new WarMap();

        try (BufferedReader l_bufferedreader = new BufferedReader(new FileReader(MAP_DEF_PATH + "asia.map"))) {

            Map<Integer, Continent> l_continentMap = new HashMap();
            Continent continent = null;
            Country country = null;
            int l_continentCounter = 1;

            //while loop read each line from file and process accordingly
            while ((l_fileLine = l_bufferedreader.readLine()) != null) {

                if (l_fileLine.startsWith(";")) {
                    continue;
                }
                if (l_fileLine.startsWith(NAME)) {
                    String l_name = l_fileLine.substring(5);
                    warMap.setD_mapName(l_name);
                }
                if (l_fileLine.equalsIgnoreCase(CONTINENTS)) {
                    l_isContinents = true;
                    continue;
                }
                //this if condition read all the contients from file and set into continent model
                if (l_isContinents && !l_fileLine.equalsIgnoreCase(COUNTRIES)) {
                    continent = new Continent();
                    String[] l_continentArray = l_fileLine.split(" ");
                    continent.setD_continentName(l_continentArray[0]);
                    continent.setD_continentValue(Integer.parseInt(l_continentArray[1]));
                    continent.setD_continentIndex(l_continentCounter);
                    l_continentMap.put(l_continentCounter, continent);
                    l_continentCounter++;
                }
                if (l_fileLine.equalsIgnoreCase(COUNTRIES)) {
                    l_isContinents = false;
                    l_isCountries = true;
                    continue;
                }
                //this if condtion read all the countries from file and set into country model
                if (l_isCountries && !l_fileLine.equalsIgnoreCase(BORDERS)) {

                    String[] l_countryArray = l_fileLine.split(" ");

                    int l_continentIndex = Integer.parseInt(l_countryArray[2]);
                    Continent currentcontinent = l_continentMap.get(l_continentIndex);

                    country = new Country();
                    country.setD_countryName(l_countryArray[1]);
                    country.setD_countryIndex(Integer.parseInt(l_countryArray[0]));
                    country.setD_continentIndex(l_continentIndex);

                    currentcontinent.getD_countryList().add(country);
                    l_continentMap.put(l_continentIndex, currentcontinent);
                }
                if (l_fileLine.equalsIgnoreCase(BORDERS)) {
                    l_isCountries = false;
                    l_isBorders = true;
                    continue;
                }
                //this if condition read neighbors of each country and set into neighborlist of coutey model
                if (l_isBorders) {

                    String[] l_neighbourArray = l_fileLine.split(" ");

                    Continent currentContinent = getContinentByCountryId(l_continentMap, Integer.parseInt(l_neighbourArray[0]));

                    List<String> l_neighbourName = new ArrayList<String>();
                    for (int i = 1; i < l_neighbourArray.length; i++) {
                        l_neighbourName
                                .add(getNeighbourNamebyIndex(l_continentMap, Integer.parseInt(l_neighbourArray[i])));
                    }

                    for (int i = 0; i < currentContinent.getD_countryList().size(); i++) {
                        Country currentcountry = currentContinent.getD_countryList().get(i);
                        if (currentcountry.getD_countryIndex() == Integer.parseInt(l_neighbourArray[0])) {
                            currentcountry.setD_neighbourCountries(l_neighbourName);
                            currentContinent.getD_countryList().set(i, currentcountry);
                        }
                    }
                    l_continentMap.put(currentContinent.getD_continentIndex(), currentContinent);
                }
            }
            warMap.setD_continents(l_continentMap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return warMap;
    }

    /**
     * This method will return Continent model from given countryId
     *
     * @param p_continentMap is map of continents
     * @param p_countryIndex is countryId for that you want to find continent
     * @return Continent model
     */
    private Continent getContinentByCountryId(Map<Integer, Continent> p_continentMap, int p_countryIndex) {
        Continent continent = null;
        for (Map.Entry<Integer, Continent> entry : p_continentMap.entrySet()) {

            continent = entry.getValue();

            List<Country> l_countryList = continent.getD_countryList();
            for (Country country : l_countryList) {

                if (country != null) {

                    if (country.getD_continentIndex() == p_countryIndex) {

                        return continent;
                    }
                }
            }
        }
        return continent;

    }

    /**
     * This method will return neighbor name by given Index
     *
     * @param p_continentMap is a amp of continents
     * @param p_countryIndex is neighbor index
     * @return neighbor name
     */
    private String getNeighbourNamebyIndex(Map<Integer, Continent> p_continentMap, int p_countryIndex) {

        String neighbourName = "";

        for (Map.Entry<Integer, Continent> entry : p_continentMap.entrySet()) {

            Continent continent = entry.getValue();

            List<Country> l_countryList = continent.getD_countryList();
            for (Country country : l_countryList) {

                if (country != null) {

                    if (p_countryIndex == country.getD_countryIndex()) {
                        neighbourName = country.getD_countryName();
                        break;
                    }
                }
            }
        }
        return neighbourName;

    }

}
