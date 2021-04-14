package com.soen6441.warzone.adapterpattern;

import static com.soen6441.warzone.config.WarzoneConstants.*;

import com.soen6441.warzone.model.Continent;
import com.soen6441.warzone.model.Country;
import com.soen6441.warzone.model.WarMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * This Class is used for Reading and Writing Domination Map File. This is the Target
 * Class in Adapter Pattern. Three annotations (Getter,Setter, NoArgsConstructor),
 * you can see on the top of the class are lombok dependencies to automatically
 * generate getter, setter method and default Constructor in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class DominationMapReader {

    /**
     * This method will read map file and convert data into WarMap model object
     *
     * @param p_fileName fileName to read Map
     * @return WarMap model
     * @throws java.io.IOException throws input/output exception
     */
    public WarMap readMap(String p_fileName) throws IOException {

        String l_fileLine = "";
        boolean l_isFiles = false;
        boolean l_isContinents = false;
        boolean l_isCountries = false;
        boolean l_isBorders = false;
        WarMap l_warMap = new WarMap();

        try (BufferedReader l_bufferedreader = new BufferedReader(new FileReader(MAP_DEF_PATH + p_fileName))) {

            Map<Integer, Continent> l_continentMap = new HashMap();
            Continent l_continent = null;
            Country l_country = null;
            int l_continentCounter = 1;

            //while loop read each line from file and process accordingly
            while ((l_fileLine = l_bufferedreader.readLine()) != null) {
                if (l_fileLine != null && !l_fileLine.isEmpty()) {

                    if (l_fileLine.startsWith(";")) {
                        continue;
                    }

                    l_warMap.setD_mapName(p_fileName);
                    l_warMap.setD_status(true);
                    if (l_fileLine.equalsIgnoreCase(MAP)) {
                        l_isFiles = true;
                        continue;
                    }
                    if (l_isFiles) {
                        //files of map
                    }
                    if (l_fileLine.equalsIgnoreCase(CONTINENTS)) {
                        l_isFiles = false;
                        l_isContinents = true;
                        continue;
                    }
                    //this if condition read all the continents from file and set into continent model
                    if (l_isContinents && !l_fileLine.equalsIgnoreCase(COUNTRIES)) {
                        l_continent = new Continent();
                        String[] l_continentArray = l_fileLine.split(" ");
                        l_continent.setD_continentName(l_continentArray[0]);
                        l_continent.setD_continentValue(Integer.parseInt(l_continentArray[1]));
                        l_continent.setD_continentIndex(l_continentCounter);
                        l_continentMap.put(l_continentCounter, l_continent);
                        l_continentCounter++;
                    }
                    if (l_fileLine.equalsIgnoreCase(COUNTRIES)) {
                        l_isContinents = false;
                        l_isCountries = true;
                        continue;
                    }
                    //this if condition read all the countries from file and set into country model
                    if (l_isCountries && !l_fileLine.equalsIgnoreCase(BORDERS)) {

                        String[] l_countries = l_fileLine.split(" ");

                        int l_continentIndex = Integer.parseInt(l_countries[2]);
                        Continent l_currentcontinent = l_continentMap.get(l_continentIndex);

                        l_country = new Country();
                        l_country.setD_countryName(l_countries[1]);
                        l_country.setD_countryIndex(Integer.parseInt(l_countries[0]));
                        l_country.setD_continentIndex(l_continentIndex);
                        if (l_currentcontinent.getD_countryList() == null) {
                            List<Country> l_countryList = new ArrayList();
                            l_countryList.add(l_country);
                            l_currentcontinent.setD_countryList(l_countryList);
                        } else {
                            l_currentcontinent.getD_countryList().add(l_country);
                        }
                        l_continentMap.put(l_continentIndex, l_currentcontinent);
                    }
                    if (l_fileLine.equalsIgnoreCase(BORDERS)) {
                        l_isCountries = false;
                        l_isBorders = true;
                        continue;
                    }
                    //this if condition read neighbors of each country and set into neighborlist of coutey model
                    if (l_isBorders) {

                        String[] l_neighbourArray = l_fileLine.split(" ");

                        Continent l_currentContinent = getContinentByCountryId(l_continentMap, Integer.parseInt(l_neighbourArray[0]));

                        List<String> l_neighbourName = new ArrayList<String>();
                        for (int l_i = 1; l_i < l_neighbourArray.length; l_i++) {
                            l_neighbourName
                                    .add(getCountryNamebyCountryId(l_continentMap, Integer.parseInt(l_neighbourArray[l_i])));
                        }

                        for (int l_i = 0; l_i < l_currentContinent.getD_countryList().size(); l_i++) {
                            Country l_currentCountry = l_currentContinent.getD_countryList().get( l_i );
                            if (l_currentCountry.getD_countryIndex() == Integer.parseInt(l_neighbourArray[0])) {
                                l_currentCountry.setD_neighbourCountries(l_neighbourName);
                                l_currentContinent.getD_countryList().set( l_i, l_currentCountry);
                            }
                        }
                        l_continentMap.put(l_currentContinent.getD_continentIndex(), l_currentContinent);
                    }
                }
            }
            l_warMap.setD_continents(l_continentMap);
        } catch (IOException e) {
            throw e;
        }
        return l_warMap;
    }

    /**
     * This method is used to check whether the WarMap object is successfully
     * saved into Domination map File or not.
     *
     * @param p_warMap Warmap Object to Write
     * @return Whether File is being Saved or not
     */
    public boolean writeMap(WarMap p_warMap) {
        String l_fileName = p_warMap.getD_mapName();
        // for managing the .map extension of the file
        if (l_fileName.contains(".")) {
            String l_fileNameSplit = l_fileName.split("\\.")[1];
            if (!l_fileNameSplit.equals("map")) {
                l_fileName = l_fileName.concat(".map");
            }
        } else {
            l_fileName = l_fileName.concat(".map");
        }
        boolean l_status;
        try {
            //creation of content to write into file
            StringBuilder l_continentStringBuilder = new StringBuilder(CONTINENTS).append(System.lineSeparator());
            StringBuilder l_countryStringBuilder = new StringBuilder(COUNTRIES).append(System.lineSeparator());
            StringBuilder l_neighborStringBuilder = new StringBuilder(BORDERS).append(System.lineSeparator());

            try (PrintWriter l_writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(MAP_DEF_PATH + l_fileName), "utf-8")));) {

                Map<Integer, Continent> l_continentMap = p_warMap.getD_continents();
                //for storing continent of the map

                for (Map.Entry<Integer, Continent> l_entry : l_continentMap.entrySet()) {
                    Continent l_currentContinent = l_entry.getValue();

                    //here all continets will store into the l_continentStringBuilder
                    l_continentStringBuilder.append(l_currentContinent.getD_continentName() + " " + l_currentContinent.getD_continentValue()).append(System.lineSeparator());
                    if (l_currentContinent.getD_countryList() != null) {
                        List<Country> l_countryList = l_currentContinent.getD_countryList();
                        for (Country l_country : l_countryList) {

                            //here all countries will store into the l_countryStringBuilder
                            l_countryStringBuilder.append(l_country.getD_countryIndex() + " " + l_country.getD_countryName() + " " + l_country.getD_continentIndex() + " 0 0")
                                    .append(System.lineSeparator());

                            if (l_country.getD_neighbourCountries() != null) {
                                List<String> l_neighborList = l_country.getD_neighbourCountries();
                                if (!l_neighborList.isEmpty() && l_neighborList != null) {
                                    l_neighborStringBuilder.append(l_country.getD_countryIndex());
                                    for (String l_neighborName : l_neighborList) {

                                        //here all neighbors will store into the l_neighborStringBuilder
                                        l_neighborStringBuilder.append(" " + getCountryIndexByCountryName(p_warMap, l_neighborName));
                                    }
                                    l_neighborStringBuilder.append(System.lineSeparator());
                                }
                            }
                        }
                    }
                }
                l_writer.println("; map: " + p_warMap.getD_mapName());
                l_writer.println("; map made with the map maker");
                l_writer.println("; yura.net Risk 1.0.9.5\n");
                l_writer.println("name " + p_warMap.getD_mapName());
                l_writer.println();
                l_writer.println(FILES);
                l_writer.println("pic warzone_pic.png");
                l_writer.println();
                l_writer.println(l_continentStringBuilder.toString());
                l_writer.println(l_countryStringBuilder.toString());
                l_writer.println(l_neighborStringBuilder.toString());
                l_status = true;
            }
        } catch (Exception e) {
            l_status = false;
        }
        return l_status;
    }

    /**
     * This method will return index from name
     *
     * @param p_warMap is object of WarMap model
     * @param p_countryName is the name of
     * @return index of country
     */
    private int getCountryIndexByCountryName(WarMap p_warMap, String p_countryName) {
        int l_countryIndex = 0;
        Map<Integer, Continent> l_continentMap = p_warMap.getD_continents();

        for (Map.Entry<Integer, Continent> l_entry : l_continentMap.entrySet()) {
            Continent l_currentContinent = l_entry.getValue();
            //getting the  list
            if (l_currentContinent.getD_countryList() != null) {
                List<Country> l_countryList = l_currentContinent.getD_countryList();
                if (l_countryList != null) {
                    for (Country l_country : l_countryList) {
                        //Comparing  name with give  name
                        if (l_country != null) {
                            if (l_country.getD_countryName() == p_countryName) {
                                l_countryIndex = l_country.getD_countryIndex();
                                break;
                            }
                        }
                    }
                }
            }
        }
        return l_countryIndex;
    }

    /**
     * This method will return Continent model from given countryId
     *
     * @param p_continentMap is map of continents
     * @param p_countryIndex is countryId for that you want to find continent
     * @return Continent model
     */
    private Continent getContinentByCountryId(Map<Integer, Continent> p_continentMap, int p_countryIndex) {
        Continent l_continent = null;
        for (Map.Entry<Integer, Continent> l_entry : p_continentMap.entrySet()) {

            l_continent = l_entry.getValue();
            //getting country list 
            List<Country> l_countryList = l_continent.getD_countryList();
            if (l_countryList != null) {
                for (Country l_country : l_countryList) {

                    if (l_country != null) {
                        //comparing index with country's which we want to find
                        if (l_country.getD_countryIndex() == p_countryIndex) {

                            return l_continent;
                        }
                    }
                }
            }
        }

        return l_continent;
    }

    /**
     * This method will return neighbor name by given Index
     *
     * @param p_continentMap is a map of continents
     * @param p_countryIndex is neighbor index
     * @return neighbor name
     */
    private String getCountryNamebyCountryId(Map<Integer, Continent> p_continentMap, int p_countryIndex) {

        String l_neighbourName = "";

        for (Map.Entry<Integer, Continent> l_entry : p_continentMap.entrySet()) {
            //getting country list 
            Continent l_continent = l_entry.getValue();

            List<Country> l_countryList = l_continent.getD_countryList();
            if (l_countryList != null) {
                for (Country l_country : l_countryList) {

                    if (l_country != null) {
                        //comparing index with country's which we want to find
                        if (p_countryIndex == l_country.getD_countryIndex()) {
                            l_neighbourName = l_country.getD_countryName();
                            break;
                        }
                    }
                }
            }
        }

        return l_neighbourName;
    }
}
