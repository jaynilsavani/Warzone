package com.soen6441.warzone.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import com.soen6441.warzone.service.MapHandlingInterface;
import com.soen6441.warzone.model.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    @Autowired
    private CommandResponse commandResponse;

    private static int ContinentId = 1;
    private static int CountryId = 1;
    private static int NeighbourId = 1;

    public static final String MAP_DEF_PATH = "src/main/resources/maps/";

    public static final String NAME = "name";
    public static final String FILES = "[files]";
    public static final String CONTINENTS = "[continents]";
    public static final String COUNTRIES = "[countries]";
    public static final String BORDERS = "[borders]";

    @Override
    public boolean isNullOrEmpty(String p_str) {
        if (p_str != null && !p_str.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public CommandResponse validateCommand(String p_command) {
        boolean l_isValid = false;
        try {
            if (!isNullOrEmpty(p_command)) {
                if (p_command.startsWith("editcontinent")) {
                    return checkCommandEditContinent(p_command);
                } else if (p_command.startsWith("editcountry")) {
                    return checkCommandEditCountry(p_command);
                } else if (p_command.startsWith("editneighbor") || p_command.startsWith("editneighbour")) {
                    // checkCommandEditNeighbour(p_command);
                } else if (p_command.startsWith("showmap")) {
                    // show map
                } else if (p_command.startsWith("savemap")) {
                    // save map
                } else if (p_command.startsWith("editmap")) {
                    return checkCommandEditMap(p_command);
                } else if (p_command.startsWith("validatemap")) {
                    //  

                } else {
                    l_isValid = false;
                }

            } else {
                prepareResponse(false, "Please enter valid command");
                l_isValid = false;

            }
        } catch (Exception e) {
            e.printStackTrace();
            prepareResponse(false, "Please enter valid command");
            l_isValid = false;
        }

        return commandResponse;
    }

    /**
     * This method will prepare response of command entered by user
     *
     * @param p_isValid to check command is successfully executed or not
     * @param p_responeMessage response message of command
     */
    public void prepareResponse(boolean p_isValid, String p_responeMessage) {
        this.commandResponse.setD_isValid(p_isValid);
        this.commandResponse.setD_responseString(p_responeMessage);
    }

    /**
     * This method will check edit Continent command, validate and then call
     * perform next operation
     *
     * @param p_editContinentCommand is edit continent command sent from user
     * @return message of result after edit Continent operation
     */
    public CommandResponse checkCommandEditContinent(String p_editContinentCommand) {
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

                    if (d_warMap.getD_continents() != null) {
                        for (Map.Entry<Integer, Continent> l_entry : d_warMap.getD_continents().entrySet()) {
                            if (l_entry.getValue() != null && l_continentName.equalsIgnoreCase(l_entry.getValue().getD_continentName())) {
                                // show error message "continent already exists in map file"
                                l_isValidName = false;
                                break;
                            }
                        }
                    }

                    if (l_isValidName) {
                        saveContinent(l_continentName, l_continetValue);
                        commandResponse.setD_isValid(true);
                        commandResponse.setD_responseString("Continent saved successfully");
                        return commandResponse;
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
//        return "Continent changes successfully executed.";
        return commandResponse;
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
        return l_result;
    }

    /**
     * This method will return true and break if country got removed and this
     * method is used for removal of country
     *
     * @param p_countryIndex Id of the country you want to delete for
     * @return false if not possible to delete
     */
    public boolean deleteCountry(int p_countryIndex) {
        boolean l_result = false;

        for (Map.Entry<Integer, Continent> l_continent : d_warMap.getD_continents().entrySet()) {
            List<Country> l_countryList = l_continent.getValue().getD_countryList();
            List<Country> l_removedCountry = l_countryList.stream().filter(countrty -> p_countryIndex == countrty.getD_countryIndex())
                    .collect(Collectors.toList());
            //to remove and set updated countries to map 
            if (!l_removedCountry.isEmpty()) {
                l_countryList.removeAll(l_removedCountry);
                l_continent.getValue().setD_countryList(l_countryList);
                l_result = true;
            }
        }
        return l_result;
    }

    /**
     * This method will return true and break if neighbor got removed and this
     * method is used for removal of country's neighbor
     *
     * @param p_countryIndex id of the country you want to delete for
     * @param p_neighborIndex id of the neighbor you want to delete
     *
     * @return false if not possible to delete or does not exist
     */
    public boolean deleteNeighbour(int p_countryIndex, int p_neighborIndex) {
        boolean l_result = false;
        for (Map.Entry<Integer, Continent> l_continent : d_warMap.getD_continents().entrySet()) {
            for (Country l_country : l_continent.getValue().getD_countryList()) {
                if (p_countryIndex == l_country.getD_countryIndex()) {
                    //Get neighbour name of user input
                    String l_neighbourNameToRemove = getNeighbourNamebyIndex(d_warMap.getD_continents(), p_neighborIndex);
                    //get neighour that matches neighbour given by user
                    List<String> l_neighborToRemove = l_country.getD_neighbourCountries().stream().filter(l_neighborName -> (l_neighborName == null ? l_neighbourNameToRemove == null : l_neighborName.equalsIgnoreCase(l_neighbourNameToRemove))).collect(Collectors.toList());
                    //if neighbour found then remove for list of neighbour
                    if (!l_neighborToRemove.isEmpty()) {
                        l_country.getD_neighbourCountries().removeAll(l_neighborToRemove);
                        l_result = true;
                    }
                }
            }
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
        if (d_warMap.getD_continents() == null) {
            Map<Integer, Continent> l_continentMap = new HashMap();
            l_continentMap.put(ContinentId, l_continent);
            d_warMap.setD_continents(l_continentMap);
        } else {
            d_warMap.getD_continents().put(ContinentId, l_continent);
        }
        ContinentId++;
    }

    /**
     * This method will check edit Continent command, validate and then call
     * perform next operation
     *
     * @param p_editCountryCommand is edit command sent from user
     * @return message of result after edit Country command execution
     */
    public CommandResponse checkCommandEditCountry(String p_editCountryCommand) {
        String l_countryName = "";
        String l_continentName = "";
        List<String> l_editCountryCommandString = Arrays.asList(p_editCountryCommand.split(" "));

        for (int i = 0; i < l_editCountryCommandString.size(); i++) {
            if (l_editCountryCommandString.get(i).equalsIgnoreCase("-add")) {
                l_countryName = l_editCountryCommandString.get(i + 1);
                l_continentName = l_editCountryCommandString.get(i + 2);
                if (validateIOString(l_countryName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")
                        && validateIOString(l_continentName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")) {

                    // prepare country list of continent entered by user
                    ArrayList<Country> l_countryList = new ArrayList<Country>();
                    int l_continentIndex = 1;
                    boolean isValidContinent = false;
                    if (d_warMap.getD_continents() != null) {
                        for (Map.Entry<Integer, Continent> l_entry : d_warMap.getD_continents().entrySet()) {
                            // check if continent exists or not
                            if (l_entry.getValue() != null && l_continentName.equalsIgnoreCase(l_entry.getValue().getD_continentName())) {
                                l_continentIndex = l_entry.getKey();
                                l_entry.getValue().getD_countryList().forEach((country) -> {
                                    l_countryList.add(country);
                                });
                                isValidContinent = true;
                                break;
                            }
                        }
                    } else {
                        prepareResponse(false, "Continent not found");
                        break;

                    }

                    if (isValidContinent) {
                        if (!l_countryList.isEmpty()) {
                            // if continent already have countries
                            for (Country country : l_countryList) {
                                if (!l_countryName.equalsIgnoreCase(country.getD_countryName())) {
                                    saveCountry(l_countryName, l_continentIndex);
                                    prepareResponse(true, "Country saved successfully");
                                } else {
                                    prepareResponse(false, "Country already exists");
                                    break;
                                }
                            }
                        } else {
                            // if continent doesn't have any country then add it
                            saveCountry(l_countryName, l_continentIndex);
                        }
                    } else {
                        prepareResponse(false, "Continent is not valid");
                        break;
                    }

                } else {
                    prepareResponse(false, "Please enter valid country name or continent name");
                }

            } else if (l_editCountryCommandString.get(i).equalsIgnoreCase("-remove")) {
                l_countryName = l_editCountryCommandString.get(i + 1);

                if (validateIOString(l_countryName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")) {
                    // delete country operation

                } else {
                    prepareResponse(false, "Please enter valid country Name");
                }
            }

        }

        return commandResponse;
    }

    /**
     * This method will save country given from both GUI and command line
     *
     * @param p_countryName name of the country
     * @param p_continentIndex index of continent
     */
    public void saveCountry(String p_countryName, int p_continentIndex) {
        Country l_country = new Country();
        l_country.setD_continentIndex(p_continentIndex);
        l_country.setD_countryName(p_countryName);
        l_country.setD_countryIndex(CountryId);
        Continent l_continent = d_warMap.getD_continents().get(p_continentIndex);
        l_continent.getD_countryList().add(l_country);
        CountryId++;
    }

    @Override
    public boolean validateIOString(String p_string, String p_regex) {
        if (!p_string.isEmpty()) {
            Pattern l_pattern = Pattern.compile(p_regex);
            Matcher l_matcher = l_pattern.matcher(p_string);
            return l_matcher.find();
        } else {
            return false;
        }
    }

    @Override
    public WarMap readMap(String p_fileName) {

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

                        l_currentcontinent.getD_countryList().add(l_country);
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
                                    .add(getNeighbourNamebyIndex(l_continentMap, Integer.parseInt(l_neighbourArray[i])));
                        }

                        for (int i = 0; i < l_currentContinent.getD_countryList().size(); i++) {
                            Country currentcountry = l_currentContinent.getD_countryList().get(i);
                            if (currentcountry.getD_countryIndex() == Integer.parseInt(l_neighbourArray[0])) {
                                currentcountry.setD_neighbourCountries(l_neighbourName);
                                l_currentContinent.getD_countryList().set(i, currentcountry);
                            }
                        }
                        l_continentMap.put(l_currentContinent.getD_continentIndex(), l_currentContinent);
                    }
                }
            }
            l_warMap.setD_continents(l_continentMap);
        } catch (Exception e) {
            e.printStackTrace();
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

            List<Country> l_countryList = continent.getD_countryList();
            for (Country country : l_countryList) {

                if (country != null) {

                    if (country.getD_countryIndex() == p_countryIndex) {

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

    /**
     * This method will check edit map command and if file is already exist then
     * read the data of existing map file otherwise it will create new map file
     *
     * @param p_editMapCommand
     */
    public CommandResponse checkCommandEditMap(String p_editMapCommand) {
        String l_fileName = Arrays.asList(p_editMapCommand.split(" ")).get(1);

        if (validateIOString(l_fileName, "[a-zA-Z]+.?[a-zA-Z]+")) {
            List<String> l_mapFileNameList = getAvailableMapFiles();
            String l_fullName;
            int index = l_fileName.lastIndexOf('.');
            l_fullName = index > 0
                    ? l_fileName.toLowerCase() : l_fileName.toLowerCase() + ".map";

            // Set status and map file name 
            d_warMap.setD_status(true);
            d_warMap.setD_mapName(l_fullName);

            if (l_mapFileNameList.contains(l_fullName)) {
                try {
                    d_warMap = readMap(l_fullName);
                    prepareResponse(true, "Map loaded successfully! Do not forget to save map file after editing");
                } catch (Exception e) {
                    prepareResponse(false, "Exception in EditMap, Invalid Map Please correct Map");
                }
            } else {
                prepareResponse(true, "Map not found in system, new map is created. Pleaase do not forget to save map file after editing");
            }
        } else {
            prepareResponse(false, "Please enter valid file name for editMap command");
        }

        return commandResponse;
    }

    /**
     * This method is will used to get all available map files
     *
     * @return it will return list of map file
     * @throws java.io.IOException throws IO Exception
     */
    public List<String> getAvailableMapFiles() throws IOException {
        List<String> l_maps = new ArrayList<>();

        l_maps = getListOfAllFiles(Paths.get(MAP_DEF_PATH), ".map");

        return l_maps;
    }

    /**
     * List all files from this given path and extension
     *
     * @param p_path directory path of files
     * @param p_fileExtension extension of file to be searched
     * @return list of files
     */
    public List<String> getListOfAllFiles(Path p_path, String p_fileExtension)
            throws IOException {
        List<String> l_files;
        try (Stream<Path> l_walk = Files.walk(p_path)) {
            l_files = l_walk.map(filePath -> filePath.toFile().getName())
                    .filter(fileName -> fileName.endsWith(p_fileExtension))
                    .collect(Collectors.toList());
        }

        return l_files;
    }
}
