package com.soen6441.warzone.service.impl;

import static com.soen6441.warzone.config.WarzoneConstants.*;

import java.io.BufferedReader;
import java.io.FileReader;

import com.soen6441.warzone.service.MapHandlingInterface;
import com.soen6441.warzone.model.*;
import com.soen6441.warzone.service.GeneralUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is the implementation class of MapHandlingInterface having business
 * logic of map handling which includes create, edit and validate map etc.
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">Yashkumar Vaghani</a>
 */
@Service
public class MapHandlingImpl implements MapHandlingInterface {

    @Autowired
    private WarMap d_warMap;

    @Autowired
    private GeneralUtil d_generalUtil;

    private static int ContinentId = 1;
    private static int CountryId = 1;
    private static int NeighbourId = 1;

    /**
     * {@inheritDoc }
     */
    @Override
    public CommandResponse validateCommand(String p_command) {
        boolean l_isValid = false;
        try {
            if (!d_generalUtil.isNullOrEmpty(p_command)) {

                //This Condition is used to check editcontinent command
                if (p_command.startsWith("editcontinent")) {
                    return checkCommandEditContinent(p_command);
                } //This Condition is used to check editcountry command
                else if (p_command.startsWith("editcountry")) {
                    return checkCommandEditCountry(p_command);
                } //This Condition is used to check editneighbor command
                else if (p_command.startsWith("editneighbour")) {
                    return checkCommandEditNeighbours(p_command);
                } //This Condition is used to check showmap command
                else if (p_command.equalsIgnoreCase("showmap")) {
                    return showMap(d_warMap);
                } //This Condition is used to check savemap command 
                else if (p_command.startsWith("savemap")) {
                    return checkCommandSaveMap(p_command);
                } //This Condition is used to check editmap command
                else if (p_command.startsWith("editmap")) {
                    return checkCommandEditMap(p_command);
                } //This Condition is used to check validatemap command
                else if (p_command.equalsIgnoreCase("validatemap")) {
                    if (validateMap(d_warMap)) {
                        d_generalUtil.prepareResponse(true, "Map is valid");
                    } else {
                        d_generalUtil.prepareResponse(false, "Map is invalid or not selected");
                    }
                } else {
                    d_generalUtil.prepareResponse(false, "Please enter valid command");
                    l_isValid = false;
                }

            } else {
                d_generalUtil.prepareResponse(false, "Please enter valid command");
                l_isValid = false;

            }
        } catch (Exception e) {
            e.printStackTrace();
            d_generalUtil.prepareResponse(false, "Please enter valid command");
            l_isValid = false;
        }

        return d_generalUtil.getResponse();
    }

    /**
     * This method will check edit Continent command, validate and then call
     * perform next operation
     *
     * @param p_editContinentCommand is edit continent command sent from user
     * @return message of result after edit Continent operation
     */
    public CommandResponse checkCommandEditContinent(String p_editContinentCommand) {
        if (!d_warMap.isD_status()) {
            d_generalUtil.prepareResponse(false, "Map is not selected");
            return d_generalUtil.getResponse();
        }
        String l_continentName = "";
        String l_continetValue = "";
        List<String> l_commandString = Arrays.asList(p_editContinentCommand.split(" "));
        //validate the edit continent command
        if (d_generalUtil.validateIOString(p_editContinentCommand, "editcontinent((\\s-add\\s[a-z|A-Z]+\\s[0-9]+)|(\\s-remove\\s[a-z|A-Z]+))+")) {
            List<String> l_continentNames = getAvailableContinentName(d_warMap);
            for (int l_i = 0; l_i < l_commandString.size(); l_i++) {
                //check for remove command
                if (l_commandString.get(l_i).equalsIgnoreCase("-remove")) {
                    if (!l_continentNames.contains(l_commandString.get(l_i + 1).toLowerCase())) {
                        d_generalUtil.prepareResponse(false, l_commandString.get(l_i + 1) + " is not present in the map!!!");
                        return d_generalUtil.getResponse();
                    }
                    //check for add command
                } else if (l_commandString.get(l_i).equalsIgnoreCase("-add")) {
                    if (l_continentNames.contains(l_commandString.get(l_i + 1).toLowerCase())) {
                        d_generalUtil.prepareResponse(false, l_commandString.get(l_i + 1) + " is already  present in the map!!!");
                        return d_generalUtil.getResponse();
                    }
                }
            }

        } else {
            d_generalUtil.prepareResponse(false, "Invalid Command!!!"); //prepare response if invalid edit continent command
            return d_generalUtil.getResponse();
        }
        for (int l_i = 0; l_i < l_commandString.size(); l_i++) {

            if (l_commandString.get(l_i).equalsIgnoreCase("-add")) {
                l_continentName = l_commandString.get(l_i + 1);
                l_continetValue = l_commandString.get(l_i + 2);
                // match continent name exist or not
                if (d_generalUtil.validateIOString(l_continentName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$") && d_generalUtil.validateIOString(l_continetValue, "[1-9][0-9]*")) {
                    boolean l_isValidName = true;

                    if (d_warMap.getD_continents() != null) {
                        //Iterate to check whether continent existed in the map or not
                        for (Map.Entry<Integer, Continent> l_entry : d_warMap.getD_continents().entrySet()) {
                            if (l_entry.getValue() != null && l_continentName.equalsIgnoreCase(l_entry.getValue().getD_continentName())) {
                                d_generalUtil.prepareResponse(false, "Continent already exists in map file");
                                l_isValidName = false;
                                break;
                            }
                        }
                    }
                    //for saving continent and send validity responde
                    if (l_isValidName) {
                        saveContinent(l_continentName, l_continetValue);
                        d_generalUtil.prepareResponse(true, "Continent saved successfully");
                    }

                } else {
                    d_generalUtil.prepareResponse(false, "Please enter valid continent name or value");
                }

            } //For removal of the continent
            else if (l_commandString.get(l_i).equalsIgnoreCase("-remove")) {
                l_continentName = l_commandString.get(l_i + 1);
                if (d_generalUtil.validateIOString(l_continentName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")) {
                    //to delete the continent
                    if (deleteContinent(l_continentName)) {
                        d_generalUtil.prepareResponse(true, "Continent deleted successfully");
                    } else {
                        d_generalUtil.prepareResponse(true, "Continent not found");
                    }
                } else {
                    d_generalUtil.prepareResponse(true, "Please enter valid continent name");
                }
            }
        }

        return d_generalUtil.getResponse();
    }

    /**
     * This method will check edit Continent command, validate and then call
     * perform next operation
     *
     * @param p_editCountryCommand is edit command sent from user
     * @return message of result after edit Country command execution
     */
    public CommandResponse checkCommandEditCountry(String p_editCountryCommand) {
        if (!d_warMap.isD_status()) {
            d_generalUtil.prepareResponse(false, "Map is not selected");
            return d_generalUtil.getResponse();
        }
        String l_countryName = "";
        String l_continentName = "";
        List<String> l_editCountryCommandString = Arrays.asList(p_editCountryCommand.split(" "));
        //validate the edit country command
        if (d_generalUtil.validateIOString(p_editCountryCommand, "editcountry((\\s-add\\s[a-z|A-Z]+\\s[a-z|A-Z]+)|(\\s-remove\\s[a-z|A-Z]+))+")) {
            List<String> l_continentNames = getAvailableContinentName(d_warMap);
            List<String> l_cName = getAvailableCountryName(d_warMap);
            for (int l_i = 0; l_i < l_editCountryCommandString.size(); l_i++) {
                //check for add command
                if (l_editCountryCommandString.get(l_i).equalsIgnoreCase("-add")) {
                    if (!l_continentNames.contains(l_editCountryCommandString.get(l_i + 2).toLowerCase())) {
                        d_generalUtil.prepareResponse(false, l_editCountryCommandString.get(l_i + 2) + " is not present in the map");
                        return d_generalUtil.getResponse();
                    }
                    if (l_cName.contains(l_editCountryCommandString.get(l_i + 1).toLowerCase())) {
                        d_generalUtil.prepareResponse(false, l_editCountryCommandString.get(l_i + 1) + " is already present in the map");
                        return d_generalUtil.getResponse();
                    }

                }
                //check for remove command
                if (l_editCountryCommandString.get(l_i).equalsIgnoreCase("-remove")) {
                    if (!l_cName.contains(l_editCountryCommandString.get(l_i + 1).toLowerCase())) {
                        d_generalUtil.prepareResponse(false, l_editCountryCommandString.get(l_i + 1) + " is not present in the map");
                        return d_generalUtil.getResponse();
                    }
                }
            }

        } else {
            d_generalUtil.prepareResponse(false, "Invalid Command!!!"); //Invalid edit country command
            return d_generalUtil.getResponse();
        }
        for (int i = 0; i < l_editCountryCommandString.size(); i++) {
            if (l_editCountryCommandString.get(i).equalsIgnoreCase("-add")) {
                l_countryName = l_editCountryCommandString.get(i + 1);
                l_continentName = l_editCountryCommandString.get(i + 2);
                //check whether entered country name and continent is valid or not
                if (d_generalUtil.validateIOString(l_countryName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")
                        && d_generalUtil.validateIOString(l_continentName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")) {

                    // prepare country list of continent entered by user
                    ArrayList<Country> l_countryList = getAvailableCountries(d_warMap);
                    int l_continentIndex = 1;
                    boolean l_isValidContinent = false;
                    if (d_warMap.getD_continents() != null) {
                        for (Map.Entry<Integer, Continent> l_entry : d_warMap.getD_continents().entrySet()) {
                            // check if continent exists or not
                            if (l_continentName.equalsIgnoreCase(l_entry.getValue().getD_continentName())) {
                                l_continentIndex = l_entry.getKey();
                                l_isValidContinent = true;
                                break;
                            }
                        }
                    } else {
                        d_generalUtil.prepareResponse(false, "Continent not found");
                    }

                    if (l_isValidContinent) {
                        if (!l_countryList.isEmpty()) {
                            // if continent already have countries
                            List<String> l_availableCountries = getAvailableCountryName(d_warMap);
                            if (!l_availableCountries.contains(l_countryName.toLowerCase())) {
                                saveCountry(l_countryName, l_continentIndex);
                                d_generalUtil.prepareResponse(true, "Country saved successfully");
                            } else {
                                d_generalUtil.prepareResponse(false, "Country already exists");
                            }

                        } else {
                            // if continent doesn't have any country then add it
                            saveCountry(l_countryName, l_continentIndex);
                            d_generalUtil.prepareResponse(true, "Country saved successfully");
                        }
                    } else {
                        d_generalUtil.prepareResponse(false, "Continent is not valid");
                    }

                } else {
                    d_generalUtil.prepareResponse(false, "Please enter valid country name or continent name");
                }

            }
            //remove country from the list
            else if (l_editCountryCommandString.get(i).equalsIgnoreCase("-remove")) {
                l_countryName = l_editCountryCommandString.get(i + 1);

                if (d_generalUtil.validateIOString(l_countryName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")) {
                    CommandResponse l_resp = deleteCountry(l_countryName);
                    d_generalUtil.prepareResponse(l_resp.isD_isValid(), l_resp.getD_responseString());  //Response string for deleted country

                } else {
                    d_generalUtil.prepareResponse(false, "Please enter valid country Name");
                }
            }

        }

        return d_generalUtil.getResponse();
    }

    /**
     * This method is used to validate the neighbour command and calls add or
     * remove as per the user command
     *
     * @param p_neighbour is the command to add neighbour in specific country's
     * neighbour list
     * @return CommandResponse object
     */
    public CommandResponse checkCommandEditNeighbours(String p_neighbour) {
        //This is used to check whether map is selected or not
        if (!d_warMap.isD_status()) {
            d_generalUtil.prepareResponse(false, "Map is not selected");
            return d_generalUtil.getResponse();
        }
        String l_countryName = "";
        String l_neighbourCountryName = "";
        List<String> l_commandString = Arrays.asList(p_neighbour.split(" "));
        //validate the map command
        if (d_generalUtil.validateIOString(p_neighbour, "editneighbour((\\s-add\\s[a-z|A-Z]+\\s[a-z|A-Z]+)|(\\s-remove\\s[a-z|A-Z]+\\s[a-z|A-Z]+))+")) {
            List<String> l_cName = getAvailableCountryName(d_warMap);
            //Iterate for add and remove command
            for (int l_i = 0; l_i < l_commandString.size(); l_i++) {
                if (l_commandString.get(l_i).equalsIgnoreCase("-add") || l_commandString.get(l_i).equalsIgnoreCase("-remove")) {
                    if ((!l_cName.contains(l_commandString.get(l_i + 1).toLowerCase())) || (!l_cName.contains(l_commandString.get(l_i + 2).toLowerCase()))) {
                        d_generalUtil.prepareResponse(false, l_commandString.get(l_i + 1) + " OR " + l_commandString.get(l_i + 2) + " is not present in the map");
                        return d_generalUtil.getResponse();
                    }
                }

            }

        } else {
            d_generalUtil.prepareResponse(false, "Invalid Command!!!");
            return d_generalUtil.getResponse();
        }
        //loop iterate over command segments
        for (int l_i = 0; l_i < (l_commandString.size() - 2); l_i++) {
            l_countryName = l_commandString.get(l_i + 1);
            l_neighbourCountryName = l_commandString.get(l_i + 2);
            if (d_generalUtil.validateIOString(l_countryName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$") && d_generalUtil.validateIOString(l_neighbourCountryName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")) {
                //For addition of the neighbour
                if (l_commandString.get(l_i).equalsIgnoreCase("-add")) {
                    if (d_warMap.getD_continents() != null) {
                        int l_countryId = getCountryIndexByCountryName(d_warMap.getD_continents(), l_countryName);
                        int l_neighbourCountryId = getCountryIndexByCountryName(d_warMap.getD_continents(), l_neighbourCountryName);
                        CommandResponse l_addneighbour = saveNeighbour(l_countryId, l_neighbourCountryId);
                        d_generalUtil.prepareResponse(l_addneighbour.isD_isValid(), l_addneighbour.getD_responseString());
                    }
                } //For removal of the neighbour
                else if (l_commandString.get(l_i).equalsIgnoreCase("-remove")) {
                    CommandResponse l_deleteNeighbour = deleteNeighbour(l_countryName, l_neighbourCountryName);
                    d_generalUtil.prepareResponse(l_deleteNeighbour.isD_isValid(), l_deleteNeighbour.getD_responseString());
                }
            } else {
                d_generalUtil.prepareResponse(false, "Invalid Command!!");
                return d_generalUtil.getResponse();
            }
        }

        return d_generalUtil.getResponse();
    }

    /**
     * This method will check edit map command and if file is already exist then
     * read the data of existing map file otherwise it will create new map file
     *
     * @param p_editMapCommand is command to edit a existing map or create new
     * map
     * @return Response of execution of command
     */
    public CommandResponse checkCommandEditMap(String p_editMapCommand) {
        if (Arrays.asList(p_editMapCommand.split(" ")).size() <= 1) {
            d_generalUtil.prepareResponse(false, "Invalid Command");
            return d_generalUtil.getResponse();
        }
        String l_fileName = Arrays.asList(p_editMapCommand.split(" ")).get(1);
        //validate the map file name
        if (d_generalUtil.validateIOString(l_fileName, "[a-zA-Z]+.?[a-zA-Z]+")) {
            List<String> l_mapFileNameList;
            try {
                l_mapFileNameList = d_generalUtil.getAvailableMapFiles();

                String l_fullName;
                int index = l_fileName.lastIndexOf('.');
                l_fullName = index > 0
                        ? l_fileName.toLowerCase() : l_fileName.toLowerCase() + ".map";

                //if map is available in system or not
                if (l_mapFileNameList.contains(l_fullName)) {
                    try {
                        //read the map from the system and set to the cuurent warmap object
                        d_warMap = readMap(l_fullName);
                        if (validateMap(d_warMap)) {
                            d_generalUtil.prepareResponse(true, "Map loaded successfully! Do not forget to save map file after editing");
                        } else {
                            d_warMap = new WarMap();
                            d_generalUtil.prepareResponse(false, "Invalid Map, Please select another Map");
                        }
                    } catch (Exception e) {
                        d_generalUtil.prepareResponse(false, "Exception in EditMap, Invalid Map Please correct Map");
                    }
                } else {
                    // reset warmap object if user first edit existing map and then try to edit new map
                    d_warMap = new WarMap();
                    // Set status and map file name 
                    d_warMap.setD_status(true);
                    d_warMap.setD_mapName(l_fullName);
                    d_generalUtil.prepareResponse(true, "Map not found in system, new map is created. Pleaase do not forget to save map file after editing");
                }
            } catch (IOException ex) {
                d_generalUtil.prepareResponse(false, "Exception in editmap");
            }
        } else {
            d_generalUtil.prepareResponse(false, "Please enter valid file name for editMap command");
        }

        return d_generalUtil.getResponse();
    }

    /**
     * This method will check savemap command and if it is valid then save map
     * to file
     *
     * @param p_command is a command to store map file
     * @return object of commandResponse
     */
    public CommandResponse checkCommandSaveMap(String p_command) {
        //To check whether map is empty or not
        if (d_warMap.getD_continents() == null || !d_warMap.isD_status()) {
            d_generalUtil.prepareResponse(false, "Map is empty");
            return d_generalUtil.getResponse();
        }
        if (Arrays.asList(p_command.split(" ")).size() <= 1) {
            d_generalUtil.prepareResponse(false, "Invalid Command");
            return d_generalUtil.getResponse();
        }
        String l_fileName = Arrays.asList(p_command.split(" ")).get(1);
        boolean l_fileExtension = false;
        //For managing extension of .map in given name
        if (l_fileName.contains(".")) {
           String l_extension = l_fileName.split("\\.")[1];

            if (l_extension.equals("map")) {
                l_fileExtension = true;
            } else {
                l_fileExtension = false;
            }
        } else {
            l_fileName = l_fileName.concat(".map");
            l_fileExtension = true;
        }
        try {
            //validating the filename
            if (d_generalUtil.validateIOString(l_fileName, "[a-zA-Z]+.?[a-zA-Z]+") && l_fileExtension) {

                List<String> l_mapFileList = d_generalUtil.getAvailableMapFiles();
                if (!l_fileName.equalsIgnoreCase((l_mapFileList) + ".map")) {
                    //Validate the map
                    if (validateMap(d_warMap)) {
                        if (!d_warMap.getD_mapName().equalsIgnoreCase(l_fileName)) {
                            d_warMap.setD_mapName(l_fileName.toLowerCase());
                        }
                        //save the map in system
                        writeMapToFile(d_warMap);
                        d_generalUtil.prepareResponse(true, "Map file succesfully saved");
                    } else {
                        d_generalUtil.prepareResponse(false, "Map is not valid");
                    }
                } else {
                    d_generalUtil.prepareResponse(false, "Map name is already exist please enter another name");
                }

            } else {
                d_generalUtil.prepareResponse(false, "Please enter valid file name");
            }

        } catch (Exception e) {
            d_generalUtil.prepareResponse(false, "Exception in savemap");
            e.printStackTrace();
        }

        return d_generalUtil.getResponse();
    }
    /**
     * {@inheritDoc }
     */
    @Override
    public CommandResponse showMap(WarMap p_warMap) {
        //To check whether map is empty or not
        if (p_warMap.getD_continents() == null || !p_warMap.isD_status()) {
            d_generalUtil.prepareResponse(false, "Map is empty");
            return d_generalUtil.getResponse();
        }
        String l_showMapIn2D = "";
        List<Country> l_countries = getAvailableCountries(p_warMap);
        int l_countrySize = l_countries.size();
        //Check whether atleast one country exist or not
        if (l_countrySize < 1) {
            d_generalUtil.prepareResponse(false, "Countries not found. Please add countries in the map.");

            return d_generalUtil.getResponse();
        }
        int l_i, l_j;
        l_countrySize++;
        AbstractMap.SimpleEntry<Integer, String[][]> pair = prepareMetricesOfMap(l_countries, p_warMap);
        int l_maxLength = pair.getKey();
        String[][] l_mapMetrices = pair.getValue();
        //Loop for formating map output string
        for (l_i = 0; l_i < l_countrySize; l_i++) {
            for (l_j = 0; l_j < l_countrySize; l_j++) {
                if (l_j == 0) {
                    String l_stringFrmat = String.format("%1$" + l_maxLength + "s", l_mapMetrices[l_i][l_j]);
                    l_showMapIn2D = l_showMapIn2D + l_stringFrmat;
                } else {
                    String l_stringFrmat = String.format("%1$" + l_countries.get(l_j - 1).getD_countryName().length() + "s", l_mapMetrices[l_i][l_j]) + " ";
                    l_showMapIn2D = l_showMapIn2D + l_stringFrmat;
                }
            }
            l_showMapIn2D = l_showMapIn2D + "\n";
        }
        d_generalUtil.prepareResponse(true, l_showMapIn2D);

        return d_generalUtil.getResponse();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean validateMap(WarMap p_warMap) {
        if (p_warMap.getD_continents() == null || !p_warMap.isD_status()) {
            return false;
        }
        boolean l_result = false;
        try {
            // check one or more continent is available in map
            if (p_warMap.getD_continents() != null && p_warMap.getD_continents().size() > 0) {
                ArrayList<Country> l_countries = getAvailableCountries(p_warMap);

                // check one or more country is available in map
                if (!l_countries.isEmpty()) {

                    //check continent is avilable for each country
                    List<Integer> l_continentList = new ArrayList();
                    for (Map.Entry<Integer, Continent> l_entry : p_warMap.getD_continents().entrySet()) {
                        l_continentList.add(l_entry.getValue().getD_continentIndex());
                    }
                    //check country is avilable for each country
                    for (Map.Entry<Integer, Continent> l_entry : p_warMap.getD_continents().entrySet()) {
                        List<Country> l_countryList = l_entry.getValue().getD_countryList();
                        for (Country l_country : l_countryList) {
                            if (!l_continentList.contains(l_country.getD_continentIndex())) {
                                l_result = false;
                            }
                        }
                    }
                    // check graph is connected or not
                    l_countries = getAvailableCountries(p_warMap);
                    int l_countrySize = l_countries.size();
                    int l_i, l_j;
                    l_countrySize++;
                    AbstractMap.SimpleEntry<Integer, String[][]> pair = prepareMetricesOfMap(l_countries, p_warMap);
                    String[][] l_mapMetrix = pair.getValue();

                    int[][] l_intMetric = new int[l_countrySize - 1][l_countrySize - 1];
                    for (l_i = 0; l_i < l_countrySize; l_i++) {
                        for (l_j = 0; l_j < l_countrySize; l_j++) {
                            if (l_i == 0 && l_j == 0) {
                                continue;
                            } else if (l_i == 0 && l_j != 0) {
                                continue;
                            } else if (l_j == 0 && l_i != 0) {
                                continue;
                            } else {
                                l_intMetric[l_i - 1][l_j - 1] = Integer.parseInt(l_mapMetrix[l_i][l_j]);
                            }
                        }
                    }
                    //Check whether map is bidirectional connected or not
                    if (d_generalUtil.isConnected(l_intMetric, l_countrySize - 1)) {
                        l_result = true;
                    }
                }
            } else {
                l_result = false;
            }
        } catch (Exception e) {
            l_result = false;
        }

        return l_result;
    }

    //-----------------Below Functions are utility function for above commands----------//
    //  Delete commands Function
    /**
     * This method will return true and break if continent is deleted and this
     * method is common for both terminal and GUI
     *
     * @param p_continentName the name of the continent you want to delete
     * @return true if continent successfully deleted
     */
    public boolean deleteContinent(String p_continentName) {
        boolean l_result = false;
        List<String> l_countryOfContinent = new ArrayList();

        //to store country of deleting continent
        for (Map.Entry<Integer, Continent> l_continent : d_warMap.getD_continents().entrySet()) {
            if (p_continentName.equalsIgnoreCase(l_continent.getValue().getD_continentName())) {
                if (l_continent.getValue() != null && l_continent.getValue().getD_countryList() != null) {
                    for (Country l_country : l_continent.getValue().getD_countryList()) {
                        l_countryOfContinent.add(l_country.getD_countryName());
                    }
                }
            }
        }
        //to remove country of deleting continent from other countries neighbour list
        for (Map.Entry<Integer, Continent> l_continent : d_warMap.getD_continents().entrySet()) {
            if (l_countryOfContinent != null) {
                List<Country> l_countryList = l_continent.getValue().getD_countryList();
                if (l_countryList != null) {
                    for (Country l_country : l_countryList) {
                        boolean l_status = false;
                        List<String> l_neighbourList = new ArrayList<>();
                        List<String> l_neighbourForDelete = new ArrayList<>();
                        l_neighbourList = l_country.getD_neighbourCountries();//get neighbour list
                        if (l_neighbourList != null) {
                            //Iterate over neighbour list for deletion
                            for (String l_neighbour : l_neighbourList) {
                                if (l_countryOfContinent.contains(l_neighbour)) {
                                    l_neighbourForDelete.add(l_neighbour);
                                    l_status = true;
                                }
                            }
                        }
                        if (l_status) {
                            if (l_neighbourForDelete != null) {
                                for (String l_neighbourDelete : l_neighbourForDelete) {
                                    l_neighbourList.remove(new String(l_neighbourDelete));
                                }
                            }
                        }
                        l_country.setD_neighbourCountries(l_neighbourList);
                    }
                }
                l_continent.getValue().setD_countryList(l_countryList);
            }
        }
        //to delete continent
        if (d_warMap.getD_continents() != null) {
            for (Map.Entry<Integer, Continent> l_entry : d_warMap.getD_continents().entrySet()) {
                if (l_entry.getValue() != null && p_continentName.equalsIgnoreCase(l_entry.getValue().getD_continentName())) {
                    d_warMap.getD_continents().remove(l_entry.getKey());
                    l_result = true;
                    break;
                }
            }
        }
        return l_result;
    }

    /**
     * This method will return true and break if country got removed and this
     * method is used for removal of country
     *
     * @param p_countryName Name of the country you want to delete for
     * @return false if not possible to delete
     */
    public CommandResponse deleteCountry(String p_countryName) {
        boolean l_result = false;
        CommandResponse l_dCountryResponse = new CommandResponse();
        //Check whether continent exist or not
        if (d_warMap.getD_continents() != null) {
            for (Map.Entry<Integer, Continent> l_continent : d_warMap.getD_continents().entrySet()) {
                List<Country> l_countryList = l_continent.getValue().getD_countryList();
                if (l_countryList != null) {
                    List<Country> l_removedCountry = l_countryList.stream().filter(countrty -> p_countryName.equalsIgnoreCase(countrty.getD_countryName()))
                            .collect(Collectors.toList());
                    //to remove and set updated countries to map 
                    if (!l_removedCountry.isEmpty()) {
                        l_countryList.removeAll(l_removedCountry);
                        l_continent.getValue().setD_countryList(l_countryList);
                        l_result = true;
                        l_dCountryResponse.setD_responseString("Country Deleted Sucessfully");
                    }
                }
                // remove the deleted country from other country's neighbour
                if (l_countryList != null) {
                    for (Country l_country : l_countryList) {
                        boolean l_status = false;
                        List<String> l_neighbourList = new ArrayList<>();
                        l_neighbourList = l_country.getD_neighbourCountries();
                        if (l_neighbourList != null) {
                            for (String l_neighbour : l_neighbourList) {
                                if (l_neighbour.equals(p_countryName)) {
                                    l_status = true;
                                }
                            }
                        }

                        //To remove the country
                        if (l_status) {
                            l_neighbourList.remove(new String(p_countryName));
                        }
                        l_country.setD_neighbourCountries(l_neighbourList);
                    }
                }
                l_continent.getValue().setD_countryList(l_countryList);
            }
            l_dCountryResponse.setD_isValid(true);
            if (!l_result) {
                l_dCountryResponse.setD_responseString("Country Does Not Exist!!");
            }
        } else {
            l_dCountryResponse.setD_isValid(true);
            l_dCountryResponse.setD_responseString("No Continent Exist");
        }
        return l_dCountryResponse;
    }

    /**
     * This method will return true and break if neighbor got removed and this
     * method is used for removal of country's neighbor
     *
     * @param p_countryName name of the country you want to delete for
     * @param p_neighborCountryName name of the neighbor you want to delete
     * @return false if not possible to delete or does not exist
     */
    public CommandResponse deleteNeighbour(String p_countryName, String p_neighborCountryName) {
        boolean l_result = false;
        CommandResponse l_dCountryResponse = new CommandResponse();

        //Check whether continent exist or not
        if (d_warMap.getD_continents() != null) {
            for (Map.Entry<Integer, Continent> l_continent : d_warMap.getD_continents().entrySet()) {
                if (l_continent.getValue().getD_countryList() != null) {
                    for (Country l_country : l_continent.getValue().getD_countryList()) {
                        //To check whether country exist or not
                        if (p_countryName.equalsIgnoreCase(l_country.getD_countryName())) {
                            //get neighour that matches neighbour given by user
                            List<String> l_neighborToRemove = l_country.getD_neighbourCountries().stream().filter(l_neighborName -> (l_neighborName.equalsIgnoreCase(p_neighborCountryName))).collect(Collectors.toList());
                            //if neighbour found then remove for list of neighbour
                            if (!l_neighborToRemove.isEmpty()) {
                                l_country.getD_neighbourCountries().removeAll(l_neighborToRemove);
                                l_result = true;
                                l_dCountryResponse.setD_responseString("NeighbourCountry Deleted Sucessfully");
                            }
                        }
                    }
                }
            }
            l_dCountryResponse.setD_isValid(true);
            if (!l_result) {
                l_dCountryResponse.setD_responseString("NeighbourCountry Does Not Exist!!");
            }
        } else {
            l_dCountryResponse.setD_isValid(true);
            l_dCountryResponse.setD_responseString("No Continent Exist");
        }
        return l_dCountryResponse;
    }

    // //Add Commands Function
    /**
     * This method will save continent for both terminal and GUI
     *
     * @param p_continentName name of continent
     * @param p_value value of Continent
     */
    public void saveContinent(String p_continentName, String p_value) {
        if (d_warMap.getD_continents() != null) {
            ContinentId = getContientMaximumIndex(d_warMap.getD_continents()) + 1;
        }
        //create the new continent object
        Continent l_continent = new Continent();
        l_continent.setD_continentIndex(ContinentId);
        l_continent.setD_continentName(p_continentName);
        l_continent.setD_continentValue(Integer.parseInt(p_value));
        //save the continent in map
        if (d_warMap.getD_continents() == null) {
            Map<Integer, Continent> l_continentMap = new HashMap();
            l_continentMap.put(ContinentId, l_continent);
            d_warMap.setD_continents(l_continentMap);
        } else {
            d_warMap.getD_continents().put(ContinentId, l_continent);
        }
    }

    /**
     * This method will save country given from both GUI and command line
     *
     * @param p_countryName name of the country
     * @param p_continentIndex index of continent
     */
    public void saveCountry(String p_countryName, int p_continentIndex) {
        ArrayList<Country> l_countries = getAvailableCountries(d_warMap);
        if (!l_countries.isEmpty()) {
            CountryId = getCountryMaximumIndex(l_countries) + 1;
        }
        //create the country object
        Country l_country = new Country();
        l_country.setD_continentIndex(p_continentIndex);
        l_country.setD_countryName(p_countryName);
        l_country.setD_countryIndex(CountryId);
        Continent l_continent = d_warMap.getD_continents().get(p_continentIndex);
        //Store the country in warmap object 
        if (l_continent.getD_countryList() != null) {
            l_continent.getD_countryList().add(l_country);
        } else {
            List<Country> l_countryList = new ArrayList();
            l_countryList.add(l_country);
            l_continent.setD_countryList(l_countryList);
        }
    }

    /**
     * This method is used to add the neighbour
     *
     * @param p_countryId is unique ID of country in which we want to add
     * neighbour
     * @param p_neighbour is the name of neighbour
     * @return returns true if neighbour is successfully added
     */
    public CommandResponse saveNeighbour(int p_countryId, int p_neighbour) {
        if (p_countryId == p_neighbour) {
            d_generalUtil.prepareResponse(false, "country can not be the neighbour of itself");
            return d_generalUtil.getResponse();
        }
        boolean l_result = false;
        if (d_warMap.getD_continents() != null) {
            //iterate through All continents
            for (Map.Entry<Integer, Continent> l_entry : d_warMap.getD_continents().entrySet()) {
                //iterate through All countries of contitnes
                if (l_entry.getValue() != null && l_entry.getValue().getD_countryList() != null) {
                    for (Country l_country : l_entry.getValue().getD_countryList()) {
                        //For checking country id to store the neighbour
                        if (p_countryId == l_country.getD_countryIndex()) {
                            l_result = true;
                            String l_neighbourNameToAdd = getCountryNamebyCountryId(d_warMap.getD_continents(), p_neighbour);
                            if (l_country.getD_neighbourCountries() == null) {
                                List<String> addToNeighbourList = new ArrayList<String>();
                                l_country.setD_neighbourCountries(addToNeighbourList);
                                l_country.getD_neighbourCountries().add(l_neighbourNameToAdd);  //add country in neighbour list
                                d_generalUtil.prepareResponse(true, "neighbour country is added successfully");
                                break;
                            } else {
                                if (!l_country.getD_neighbourCountries().contains(l_neighbourNameToAdd)) {
                                    l_country.getD_neighbourCountries().add(l_neighbourNameToAdd);
                                    d_generalUtil.prepareResponse(true, "neighbour country is added successfully");
                                    break;

                                } else {

                                    d_generalUtil.prepareResponse(false, "neighbour country is already exist");
                                    return d_generalUtil.getResponse();

                                }
                            }
                        }

                    }
                }
            }
            //flag to check country does not exist
            if (l_result == false) {
                d_generalUtil.prepareResponse(false, "Country does not exist!!!");
                return d_generalUtil.getResponse();
            }
        }
        return d_generalUtil.getResponse();
    }

    //General Util functions for Map Editor commands
    /**
     * This method is used to convert map object to metric
     *
     * @param p_countries list of countries
     * @param p_warMap : object of WarMap model
     * @return return no of countries and metric
     */
    public AbstractMap.SimpleEntry<Integer, String[][]> prepareMetricesOfMap(List<Country> p_countries, WarMap p_warMap) {

        int l_maxLength = 0;
        int l_countrySize = p_countries.size();
        int l_i, l_j;
        l_countrySize++;
        String[][] l_mapMetrices = new String[l_countrySize][l_countrySize];
        //iterate through all countries for one axis
        for (l_i = 0; l_i < l_countrySize; l_i++) {
            //iterate through all countries for y axis
            for (l_j = 0; l_j < l_countrySize; l_j++) {
                if (l_i == 0 && l_j == 0) {
                    l_mapMetrices[l_i][l_j] = " ";
                    continue;
                } else if (l_i == 0 && l_j != 0) {
                    l_mapMetrices[l_i][l_j] = p_countries.get(l_j - 1).getD_countryName(); // list country name
                    if (l_maxLength < l_mapMetrices[l_i][l_j].length()) {
                        l_maxLength = l_mapMetrices[l_i][l_j].length();
                    }
                } else if (l_j == 0 && l_i != 0) {
                    //for setting country and continent name
                    int l_conintentIndex = p_countries.get(l_i - 1).getD_continentIndex();
                    String l_continentName = getContinentNameByContinentId(p_warMap.getD_continents(), l_conintentIndex);
                    int l_armies=p_countries.get(l_i-1).getD_noOfArmies();
                    l_mapMetrices[l_i][l_j] = p_countries.get(l_i - 1).getD_countryName() +" "+l_armies+ " (" + l_continentName + ") ";
                    if (l_maxLength < l_mapMetrices[l_i][l_j].length()) {
                        l_maxLength = l_mapMetrices[l_i][l_j].length();
                    }
                } else {
                    //for setting  0 or 1 based on adjacancy
                    if (p_countries.get(l_i - 1).getD_neighbourCountries() != null) {
                        if (p_countries.get(l_i - 1).getD_neighbourCountries().contains(l_mapMetrices[0][l_j])) {
                            l_mapMetrices[l_i][l_j] = "1";
                        } else {
                            l_mapMetrices[l_i][l_j] = "0";
                        }
                    } else {
                        l_mapMetrices[l_i][l_j] = "0";
                    }
                }
            }
        }

        return new AbstractMap.SimpleEntry<>(l_maxLength, l_mapMetrices);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean writeMapToFile(WarMap p_warMap) {
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

            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
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
                writer.println("name " + p_warMap.getD_mapName());
                writer.println();
                writer.println(FILES);
                writer.println("pic warzone_pic.png");
                writer.println();
                writer.println(l_continentStringBuilder.toString());
                writer.println(l_countryStringBuilder.toString());
                writer.println(l_neighborStringBuilder.toString());
                l_status = true;
            }
        } catch (Exception e) {
            l_status = false;
        }
        return l_status;
    }

    @Override
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
                    if (l_fileLine.equalsIgnoreCase(FILES)) {
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
                    //this if condition read all the contients from file and set into continent model
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
                    //this if condtion read all the countries from file and set into country model
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
                        for (int i = 1; i < l_neighbourArray.length; i++) {
                            l_neighbourName
                                    .add(getCountryNamebyCountryId(l_continentMap, Integer.parseInt(l_neighbourArray[i])));
                        }

                        for (int i = 0; i < l_currentContinent.getD_countryList().size(); i++) {
                            Country l_currentCountry = l_currentContinent.getD_countryList().get(i);
                            if (l_currentCountry.getD_countryIndex() == Integer.parseInt(l_neighbourArray[0])) {
                                l_currentCountry.setD_neighbourCountries(l_neighbourName);
                                l_currentContinent.getD_countryList().set(i, l_currentCountry);
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
            //getting country list 
            List<Country> l_countryList = continent.getD_countryList();
            if (l_countryList != null) {
                for (Country l_country : l_countryList) {

                    if (l_country != null) {
                        //comparing index with country's which we want to find
                        if (l_country.getD_countryIndex() == p_countryIndex) {

                            return continent;
                        }
                    }
                }
            }
        }

        return continent;
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

        for (Map.Entry<Integer, Continent> entry : p_continentMap.entrySet()) {
            //getting country list 
            Continent l_continent = entry.getValue();

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

    /**
     * This method will return  index from  name
     *
     * @param p_warMap is object of WarMap model
     * @param p_countryName is the name of
     * @return index of
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
     * This method is used for getting  index by  name
     *
     * @param p_continentMap
     * @param p_countryName
     * @return CountryIndex
     */
    private int getCountryIndexByCountryName(Map<Integer, Continent> p_continentMap, String p_countryName) {
        for (Map.Entry<Integer, Continent> entry : p_continentMap.entrySet()) {
            Continent l_continent = entry.getValue();
            //geting all countries from the continents
            List<Country> l_countryList = l_continent.getD_countryList();
            if (l_countryList != null) {
                for (Country l_country : l_countryList) {

                    if (l_country != null) {
                        //comparing the county name with given l_country name
                        if (p_countryName.equalsIgnoreCase(l_country.getD_countryName())) {
                            return l_country.getD_countryIndex();
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * {@inheritDoc }
     */
    public ArrayList<Country> getAvailableCountries(WarMap p_continentMap) {

        List<Country> l_countries = new ArrayList<Country>();
        l_countries.clear();
        for (Map.Entry<Integer, Continent> l_entry : p_continentMap.getD_continents().entrySet()) {
            if (l_entry.getValue().getD_countryList() != null) {
                for (Country l_country : l_entry.getValue().getD_countryList()) {
                    l_countries.add(l_country);
                }
            }
        }
        return (ArrayList<Country>) l_countries;
    }

    /**
     * This method will return the continent name by continent id if finding the
     * continent from country model
     *
     * @param p_continentMap is a map of continents
     * @param p_continentIndex is neighbor index
     * @return Continent name
     */
    private String getContinentNameByContinentId(Map<Integer, Continent> p_continentMap, int p_continentIndex) {

        String l_continentName = "";

        for (Map.Entry<Integer, Continent> l_continentMap : p_continentMap.entrySet()) {
            //get countries form the continent
            Continent l_continent = l_continentMap.getValue();

            int l_conName = l_continent.getD_continentIndex();
            //comparing the country index with given country index
            if (l_conName == p_continentIndex) {
                l_continentName = l_continent.getD_continentName();
                break;
            }

        }
        return l_continentName;
    }

    /**
     * This method return Current Updated WarMap object
     *
     * @return WarMap model object
     */
    public WarMap getWarMapObject() {
        return d_warMap;
    }

    /**
     * used to give the arraylist of name of continents
     *
     * @param p_warMap gives the map object
     * @return list of the string of continents
     */
    public ArrayList<String> getAvailableContinentName(WarMap p_warMap) {
        if (p_warMap != null) {
            List<String> l_continent = new ArrayList<String>();
            if(p_warMap.getD_continents()!=null) {
                for (Map.Entry<Integer, Continent> l_entries : p_warMap.getD_continents().entrySet()) {
                    l_continent.add(l_entries.getValue().getD_continentName().toLowerCase());
                }
            }
            return (ArrayList<String>) l_continent;
        } else {
            return null;
        }
    }

    /**
     * used to give the arraylist of name of countries
     *
     * @param p_warMap gives the map object
     * @return arralist of the countryNames
     */
    public ArrayList<String> getAvailableCountryName(WarMap p_warMap) {
        if (p_warMap != null) {
            List<String> l_country = new ArrayList<String>();
            for (Country l_c : getAvailableCountries(d_warMap)) {
                l_country.add(l_c.getD_countryName().toLowerCase());
            }
            return (ArrayList<String>) l_country;
        } else {
            return null;
        }
    }

    /**
     * This is used to get maximum length of continents
     *
     * @param p_continents : List of the continents
     * @return MAximum Index of continents
     */
    public int getContientMaximumIndex(Map<Integer, Continent> p_continents) {
        int l_maxLength = 0;
        if (p_continents != null) {
            for (Map.Entry<Integer, Continent> l_entries : p_continents.entrySet()) {
                if (l_entries.getValue().getD_continentIndex() > l_maxLength) {
                    l_maxLength = l_entries.getValue().getD_continentIndex();
                }
            }
        }
        return l_maxLength;
    }

    /**
     * This is used to get maximum length of countries
     *
     * @param p_country : List of the countries
     * @return MAximum Index of Country
     */
    public int getCountryMaximumIndex(List<Country> p_country) {
        int l_maxLength = 0;
        if (p_country != null) {
            for (Country l_country : p_country) {
                if (l_country.getD_countryIndex()> l_maxLength) {
                    l_maxLength = l_country.getD_countryIndex();
                }
            }
        }
        return l_maxLength;
    }
}
