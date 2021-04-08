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
 * This Class is used for Reading and Writing Conquest Map File. This is Adaptee
 * Class in Adapter Pattern
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class ConquestMapReader {

    /**
     * This method will read Conquest map file and convert data into WarMap
     * model object
     *
     * @param p_fileName fileName to read Map
     * @return WarMap model
     * @throws java.io.IOException throws input/output exception
     */
    public WarMap readConquestMap(String p_fileName) throws IOException {

        String l_fileLine = "";
        boolean l_isMap = false;
        boolean l_isContinents = false;
        boolean l_isCountries = false;
        boolean l_isBorders = false;
        WarMap l_warMap = new WarMap();

        try (BufferedReader l_bufferedreader = new BufferedReader(new FileReader(MAP_DEF_PATH + p_fileName))) {

            Map<Integer, Continent> l_continentMap = new HashMap();
            Continent l_continent = null;
            Country l_country = null;
            int l_continentCounter = 1;
            int l_countryCounter = 1;

            //while loop read each line from file and process accordingly
            while ((l_fileLine = l_bufferedreader.readLine()) != null) {
                if (l_fileLine != null && !l_fileLine.isEmpty()) {

                    if (l_fileLine.startsWith(";")) {
                        continue;
                    }

                    l_warMap.setD_mapName(p_fileName);
                    l_warMap.setD_status(true);
                    if (l_fileLine.equalsIgnoreCase(MAP)) {
                        l_isMap = true;
                        continue;
                    }
                    if (l_isMap) {
                        //files of map
                    }
                    if (l_fileLine.equalsIgnoreCase(CONTINENTS)) {
                        l_isMap = false;
                        l_isContinents = true;
                        continue;
                    }
                    //this if condition read all the contients from file and set into continent model
                    if (l_isContinents && !l_fileLine.equalsIgnoreCase(TERRITORIES)) {
                        l_continent = new Continent();
                        String[] l_continentArray = l_fileLine.split("=");
                        l_continent.setD_continentName(l_continentArray[0]);
                        l_continent.setD_continentValue(Integer.parseInt(l_continentArray[1]));
                        l_continent.setD_continentIndex(l_continentCounter);
                        l_continentMap.put(l_continentCounter, l_continent);
                        l_continentCounter++;
                    }
                    if (l_fileLine.equalsIgnoreCase(TERRITORIES)) {
                        l_isContinents = false;
                        l_isCountries = true;
                        continue;
                    }
                    //this if condtion read all the countries from file and set into country model
                    if (l_isCountries) {

                        String[] l_countries = l_fileLine.split(",");

                        int l_continentIndex = getContinentIdByContinentName(l_continentMap, l_countries[3]);
                        Continent l_currentcontinent = l_continentMap.get(l_continentIndex);

                        l_country = new Country();
                        l_country.setD_countryName(l_countries[0]);
                        l_country.setD_countryIndex(l_countryCounter);
                        l_country.setD_continentIndex(l_continentIndex);

                        List<String> l_neighbourName = new ArrayList<String>();
                        for (int i = 4; i < l_countries.length; i++) {
                            l_neighbourName.add(l_countries[i]);
                        }
                        l_country.setD_neighbourCountries(l_neighbourName);

                        if (l_currentcontinent.getD_countryList() == null) {
                            List<Country> l_countryList = new ArrayList();
                            l_countryList.add(l_country);
                            l_currentcontinent.setD_countryList(l_countryList);
                        } else {
                            l_currentcontinent.getD_countryList().add(l_country);
                        }
                        l_continentMap.put(l_continentIndex, l_currentcontinent);
                        l_countryCounter++;
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
     *
     * @param p_warMap Warmap file object to write
     * @return Whether Map file is being saved or not
     */
    public boolean writeConquestMap(WarMap p_warMap) {
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
            StringBuilder l_countryStringBuilder = new StringBuilder(TERRITORIES).append(System.lineSeparator());

            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(MAP_DEF_PATH + l_fileName), "utf-8")));) {

                Map<Integer, Continent> l_continentMap = p_warMap.getD_continents();
                //for storing continent of the map

                for (Map.Entry<Integer, Continent> l_entry : l_continentMap.entrySet()) {
                    Continent l_currentContinent = l_entry.getValue();

                    //here all continets will store into the l_continentStringBuilder
                    l_continentStringBuilder.append(l_currentContinent.getD_continentName() + "=" + l_currentContinent.getD_continentValue()).append(System.lineSeparator());
                    if (l_currentContinent.getD_countryList() != null) {
                        List<Country> l_countryList = l_currentContinent.getD_countryList();
                        for (Country l_country : l_countryList) {

                            //here all countries will store into the l_countryStringBuilder
                            l_countryStringBuilder.append(l_country.getD_countryName() + ",0,0," + l_currentContinent.getD_continentName());

                            if (l_country.getD_neighbourCountries() != null) {
                                List<String> l_neighborList = l_country.getD_neighbourCountries();
                                if (!l_neighborList.isEmpty() && l_neighborList != null) {
                                    for (String l_neighborName : l_neighborList) {
                                        //here all neighbors will store into the l_neighborStringBuilder
                                        l_countryStringBuilder.append("," + l_neighborName);
                                    }
                                    l_countryStringBuilder.append(System.lineSeparator());
                                }
                            }
                        }
                    }
                    l_countryStringBuilder.append(System.lineSeparator());
                }
                writer.println("[Map]");
                writer.println();
                writer.println("name " + p_warMap.getD_mapName());
                writer.println();
                writer.println(l_continentStringBuilder.toString());
                writer.println(l_countryStringBuilder.toString());
                l_status = true;
            }
        } catch (Exception e) {
            l_status = false;
        }
        return l_status;
    }

    /**
     * This method return continent index by continent name
     *
     * @param p_continentMap : continent map
     * @param p_continentName : name of the continent
     * @return Index of the continent
     */
    private int getContinentIdByContinentName(Map<Integer, Continent> p_continentMap, String p_continentName) {
        Continent l_continent = null;
        int l_continentId = 0;
        for (Map.Entry<Integer, Continent> entry : p_continentMap.entrySet()) {
            l_continent = entry.getValue();
            if (l_continent != null) {
                if (l_continent.getD_continentName().equalsIgnoreCase(p_continentName)) {
                    l_continentId = l_continent.getD_continentIndex();
                }
            }

        }
        return l_continentId;
    }

}
