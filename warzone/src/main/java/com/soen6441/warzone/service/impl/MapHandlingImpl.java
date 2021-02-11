package com.soen6441.warzone.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import com.soen6441.warzone.service.MapHandlingInterface;
import com.soen6441.warzone.model.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is the implementation class of MapHandlingInterface having business
 * logic of map handling which includes create, edit and validate map etc.
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">Yashkumar Vaghani</a>
 *
 */
@Service
public class MapHandlingImpl implements MapHandlingInterface {

    @Autowired
    private WarMap d_warMap;

    private static int ContinentId = 1;
    private static int CountryId = 1;
    private static int NeighbourId = 1;

    public static final String MAP_DEF_PATH = "src/main/resources/maps/";

    public static final String NAME = "[name]";
    public static final String FILES = "[files]";
    public static final String CONTINENTS = "[continents]";
    public static final String COUNTRIES = "[countries]";
    public static final String BORDERS = "[borders]";

    /**
     * This function is used to check whether string is empty or not
     *
     * @param p_str string passed by user
     * @return true if string is not null
     */
    public boolean isNullOrEmpty(String p_str) {
        if (p_str != null && !p_str.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    /**
     * This method will validate all command entered by user
     *
     * @param p_command contains command string entered by user
     * @return true if command is valid
     */
    public boolean validateCommand(String p_command) {
        boolean l_isValid = false;
        try {
            if (!isNullOrEmpty(p_command)) {
                if (p_command.startsWith("editcontinent")) {
                    checkCommandEditContinent(p_command);
                } else if (p_command.startsWith("editcountry")) {
                    // checkCommandEditCountry(p_command);
                } else if (p_command.startsWith("editneighbor") || p_command.startsWith("editneighbour")) {
                    // checkCommandEditNeighbour(p_command);
                } else if (p_command.startsWith("showmap")) {
                    // show map
                } else if (p_command.startsWith("savemap")) {
                    // save map
                } else if (p_command.startsWith("editmap")) {
                    // edit map
                } else if (p_command.startsWith("validatemap")) {
                    //  

                } else {
                    l_isValid = false;
                }

            } else {
                // show error message "Please enter valid command"
                l_isValid = false;

            }
        } catch (Exception e) {
            e.printStackTrace();
            // show error message "Please enter valid command"
            l_isValid = false;
        }
        return l_isValid;
    }

    /**
     * This method will check edit Continent command, validate and then call
     * perform next operation
     *
     * @param p_editContinentCommand is edit continent command sent from user
     * @return message of result after edit Continent operation
     */
    public String checkCommandEditContinent(String p_editContinentCommand) {
        String l_continentName = "";
        String l_continetValue = "";
        List<String> l_commandString = Arrays.asList(p_editContinentCommand.split(" "));

        for (int l_i = 0; l_i < l_commandString.size(); l_i++) {

            if (l_commandString.get(l_i).equalsIgnoreCase("-add")) {
                l_continentName = l_commandString.get(l_i + 1);
                l_continetValue = l_commandString.get(l_i + 2);
                // match continent name exist or not
                if (validateIOString(l_continentName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$") && validateIOString(l_continetValue, "[1-9][0-9]*")) {
                    boolean l_isValidName = true;
                    for (Map.Entry<Integer, Continent> l_entry : d_warMap.getD_continents().entrySet()) {
                        if (l_entry.getValue() != null && l_continentName.equalsIgnoreCase(l_entry.getValue().getD_continentName())) {
                            // show error message "continent already exists in map file"
                            l_isValidName = false;
                            break;
                        }
                    }
                    if (l_isValidName) {
                        saveContinent(l_continentName, l_continetValue);
                        // show success message "continent saved successfully"
                    }

                } else {
                    // show error message "Please enter valid continent name or value"
                }

            } else if (l_commandString.get(l_i).equalsIgnoreCase("-remove")) {
                l_continentName = l_commandString.get(l_i + 1);
                if (validateIOString(l_continentName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")) {
                    if (deleteContinent(l_continentName)) {
                        // show success message "Continent deleted successfully."
                    } else {
                        // show error message "Continent not found."
                    }
                } else {
                    // show error message "Please enter valid continent name."
                }
            }
        }

//        return l_result.toString();
        return "Continent changes successfully executed.";
    }

    /**
     * This method will return true and break if continent is deleted and this
     * method is common for both terminal and GUI
     *
     * @param p_continentName the name of the continent you want to delete
     * @return true if continent successfully deleted
     */
    public boolean deleteContinent(String p_continentName) {
        boolean l_result = false;
        int l_continentId;

        for (Map.Entry<Integer, Continent> l_entry : d_warMap.getD_continents().entrySet()) {
            if (l_entry.getValue() != null && p_continentName.equalsIgnoreCase(l_entry.getValue().getD_continentName())) {
                l_continentId = l_entry.getKey();
                d_warMap.getD_continents().remove(l_entry.getKey());
                l_result = true;
                break;
            }
        }

        if (l_result) {
            // remove country and neighbours of continent
        }
        return l_result;
    }

    /**
     * This method will save continent for both terminal and GUI
     *
     * @param p_continentName name of continent
     * @param p_value value of Continent
     */
    public void saveContinent(String p_continentName, String p_value) {

        Continent l_continent = new Continent();
        l_continent.setD_continentIndex(ContinentId);
        l_continent.setD_continentName(p_continentName);
        l_continent.setD_continentValue(Integer.parseInt(p_value));
        Map<Integer, Continent> l_continentMap = new HashMap();
        l_continentMap.put(ContinentId, l_continent);
        d_warMap.setD_continents(l_continentMap);
        ContinentId++;

//        loadContinentDetails();
//        loadCountryDetails();
    }

    /**
     * This method will validate the I/O given from GUI or terminal
     *
     * @param p_string string you want to validate
     * @param p_regex regex for validation
     * @return true if string matches with regex
     */
    public boolean validateIOString(String p_string, String p_regex) {
        if (!p_string.isEmpty()) {
            Pattern p = Pattern.compile(p_regex);
            Matcher m = p.matcher(p_string);
            return m.find() && m.group().equals(p_string);
        } else {
            return false;
        }
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
