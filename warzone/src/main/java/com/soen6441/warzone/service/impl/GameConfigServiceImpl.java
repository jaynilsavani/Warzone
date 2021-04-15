package com.soen6441.warzone.service.impl;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.model.WarMap;
import com.soen6441.warzone.service.GameConfigService;
import com.soen6441.warzone.service.GeneralUtil;
import com.soen6441.warzone.service.MapHandlingInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.soen6441.warzone.model.*;
import static com.soen6441.warzone.model.Strategies.strategyToObjectMapper;
import static com.soen6441.warzone.model.Strategies.stringToStrategyMapper;

import java.util.Arrays;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is an implementation of GameConfigService for configuration utility
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">Yashkumar Vaghani</a>
 */
@Service
public class GameConfigServiceImpl implements GameConfigService {

    /**
     * This is used to avail Map Handling service Functions
     */
    @Autowired
    private MapHandlingInterface d_mapHandlingImpl;

    /**
     * This is used to avail General Utility Functions
     */
    @Autowired
    private GeneralUtil d_generalUtil;

    /**
     * {@inheritDoc }
     */
    @Override
    public CommandResponse showPlayerMap(GameData p_gameData) {
        WarMap l_warMap = p_gameData.getD_warMap();
        String l_mapTitle = "\nMap of countries(1 indicates adjacency between two countries)::\n" + d_mapHandlingImpl.showMap(l_warMap).getD_responseString();
        CommandResponse l_showCountris = new CommandResponse(true, l_mapTitle);
        if (p_gameData.getD_playerList() == null) {
            String l_showMapOfCountris = l_showCountris.getD_responseString();
            d_generalUtil.prepareResponse(true, l_showMapOfCountris);
        } else {
            //getting country list and player list with nullable condition
            String l_showMapOfCountris = l_showCountris.getD_responseString();
            List<Country> l_countryList = d_mapHandlingImpl.getAvailableCountries(l_warMap);
            int l_colSize = l_countryList.size() + 1;
            int l_rowSize = p_gameData.getD_playerList().size() + 1;
            String[][] l_playerToCountry = new String[l_rowSize][l_colSize];
            String l_playerRowData = "";
            int l_maxLength = 0;
            //Iterate over player list
            for (int l_i = 0; l_i < l_rowSize; l_i++) {
                //Iterate over country list
                if (l_i > 0) {
                    l_playerRowData = l_playerRowData + p_gameData.getD_playerList().get(l_i - 1).getD_playerName().toUpperCase() + " : ";
                }
                for (int l_j = 0; l_j < l_colSize; l_j++) {
                    if (l_i == 0 && l_j == 0) {

                        l_playerToCountry[l_i][l_j] = "";
                        continue;
                    } else if (l_i == 0 && l_j != 0) {
                        l_playerToCountry[l_i][l_j] = l_countryList.get(l_j - 1).getD_countryName();    //get country list for player
                        if (l_maxLength < l_playerToCountry[l_i][l_j].length()) {
                            l_maxLength = l_playerToCountry[l_i][l_j].length();     //get the number of countries
                        }
                    } else if (l_i != 0 && l_j == 0) {
                        l_playerToCountry[l_i][l_j] = p_gameData.getD_playerList().get(l_i - 1).getD_playerName();      //get player name in game
                        if (l_maxLength < l_playerToCountry[l_i][l_j].length()) {
                            l_maxLength = l_playerToCountry[l_i][l_j].length();
                        }
                    } else {
                        if (p_gameData.getD_playerList().get(l_i - 1).getD_ownedCountries().contains(l_countryList.get(l_j - 1))) {

                            int l_same = 0;
                            //Iterate over owned country list
                            while (l_same < p_gameData.getD_playerList().get(l_i - 1).getD_ownedCountries().size()) {
                                if (l_countryList.get(l_j - 1).getD_countryName().equalsIgnoreCase(p_gameData.getD_playerList().get(l_i - 1).getD_ownedCountries().get(l_same).getD_countryName())) {
                                    l_playerToCountry[l_i][l_j] = String.valueOf(l_countryList.get(l_j - 1).getD_noOfArmies()); //get number of armies for country
                                    if (l_countryList.get(l_j - 1).getD_noOfArmies() > 0) {
                                        l_playerRowData = l_playerRowData + l_countryList.get(l_j - 1).getD_countryName() + "-" + l_countryList.get(l_j - 1).getD_noOfArmies() + ",";
                                    }
                                    break;
                                }
                                l_same++;
                            }
                        } else {
                            l_playerToCountry[l_i][l_j] = "0";
                        }
                    }

                }
                if (l_i > 0) {
                    l_playerRowData = l_playerRowData + "\n";
                }
            }
            String l_titleMessage = "\nList of countries which contains atleast 1 army:: \n";
            l_showMapOfCountris = l_showMapOfCountris + l_titleMessage;
            //formatting matrix
            /*for (int l_i = 0; l_i < l_rowSize; l_i++) {
                for (int l_j = 0; l_j < l_colSize; l_j++) {
                    String l_stringFrmat = String.format("%1$" + l_maxLength + "s", l_playerToCountry[l_i][l_j]);      //string formatting for matrix representation
                    l_showMapOfCountris = l_showMapOfCountris + l_stringFrmat + " ";
                }
                l_showMapOfCountris = l_showMapOfCountris + "\n";

            }*/
            l_showMapOfCountris = l_showMapOfCountris + l_playerRowData;
            l_showCountris.setD_isValid(true);
            l_showCountris.setD_responseString(l_showMapOfCountris);

        }

        return l_showCountris;

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public WarMap loadMap(String p_fileName) throws IOException {
        return d_generalUtil.readMapByType(p_fileName);
    }

    /**
     * This function is used to update player list
     *
     * @param p_currentGameData : object of GameData model
     * @param p_command : updation command
     * @return Current Updated Gameplay and command response
     */
    @Override
    public AbstractMap.Entry<GameData, CommandResponse> updatePlayer(GameData p_currentGameData, String p_command) {
        List<String> l_commandSegments = Arrays.asList(p_command.split(" "));
        String l_playerName, l_strategy;
        GameData l_currentGameData = new GameData(p_currentGameData);
        //Iterate over command segmentation
        for (int i = 0; i < l_commandSegments.size(); i++) {
            String l_playerCommand = l_commandSegments.get(i);
            if (l_playerCommand.equalsIgnoreCase("-add") || l_playerCommand.equalsIgnoreCase("-remove")) {
                if (l_playerCommand.equalsIgnoreCase("-add")) {
                    l_playerName = l_commandSegments.get(i + 1);
                    l_strategy = l_commandSegments.get(i + 2);
                    //Validation of the player name
                    if (d_generalUtil.validateIOString(l_playerName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")) {
                        //To check if player Exist Or not 
                        if (getPlayerByName(l_currentGameData, l_playerName).isEmpty()) {
                            Player l_player = new Player();
                            l_player.setD_playerName(l_playerName);     //set the player name
                            l_player.setD_stragey(Strategies.strategyToObjectMapper(Strategies.stringToStrategyMapper(l_strategy), p_currentGameData));
                            if (l_currentGameData.getD_playerList() == null) {
                                l_currentGameData.setD_playerList(new ArrayList<>());
                            }
                            l_currentGameData.getD_playerList().add(l_player);
                            d_generalUtil.prepareResponse(true, "Player added successfully");
                        } else {
                            d_generalUtil.prepareResponse(false, "Player " + l_playerName + " already exist");
                            break;
                        }

                    } else {
                        d_generalUtil.prepareResponse(false, "Player name " + l_playerName + " is not valid");
                        break;
                    }
                } //For checking Remove condition of the player command
                else if (l_playerCommand.equalsIgnoreCase("-remove")) {
                    l_playerName = l_commandSegments.get(i + 1);
                    //Validation of the player name
                    if (d_generalUtil.validateIOString(l_playerName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")) {

                        //To check Whether Player exist or not
                        if (getPlayerByName(l_currentGameData, l_playerName).size() > 0) {
                            Player l_removedPlayer = getPlayerByName(l_currentGameData, l_playerName).get(0);
                            if (l_currentGameData.getD_playerList() != null || l_currentGameData.getD_playerList().size() > 0) {
                                l_currentGameData.getD_playerList().remove(l_removedPlayer);        //Remove player from player list
                                l_currentGameData.setD_playerList(l_currentGameData.getD_playerList());
                                d_generalUtil.prepareResponse(true, "Player removed successfully");
                            }
                        } else {
                            d_generalUtil.prepareResponse(false, "Player " + l_playerName + " does not exist");
                            break;
                        }

                    } else {
                        d_generalUtil.prepareResponse(false, "Player name " + l_playerName + " is not valid");
                        break;
                    }
                }
            }
        }
        //to merge updated player list to original gameplay
        if (d_generalUtil.getResponse().isD_isValid()) {
            List<Player> l_playerList = l_currentGameData.getD_playerList();
            p_currentGameData.setD_playerList(l_playerList);
        }
        AbstractMap.Entry<GameData, CommandResponse> l_playerResponse = new AbstractMap.SimpleEntry<>(p_currentGameData, d_generalUtil.getResponse());
        return l_playerResponse;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public CommandResponse assignCountries(GameData p_gameData) {
        WarMap l_warMap = p_gameData.getD_warMap();
        //Check Whether player is available in Gameplay or not
        if (p_gameData.getD_playerList() == null) {
            d_generalUtil.prepareResponse(false, "No player in the game");

        } else if (p_gameData.getD_playerList().get(0).getD_ownedCountries().size() != 0) {
            d_generalUtil.prepareResponse(false, "Countries are already assigned");
        } else {
            int l_noOfPlayers = p_gameData.getD_playerList().size();

            int l_noOfCountries = d_mapHandlingImpl.getAvailableCountries(l_warMap).size(); //store size of country from map
            List<Country> l_country_check = d_mapHandlingImpl.getAvailableCountries(l_warMap);
            int l_counter = l_noOfCountries / l_noOfPlayers;
            int l_modulus = l_noOfCountries % l_noOfPlayers;
            List<Country> l_checkCountryOnPlayer = new ArrayList<Country>();
            int l_i = 0, l_j = 0;
            for (l_i = 0; l_i < l_counter; l_i++) {
                for (l_j = 0; l_j < l_noOfPlayers; l_j++) {
                    //If player does not have countries 
                    if (p_gameData.getD_playerList().get(l_j).getD_ownedCountries() == null) {
                        List<Country> l_country = new ArrayList<Country>();
                        while (true) {
                            Random l_rand = new Random();       //generate random number
                            int rand_int1 = l_rand.nextInt(l_noOfCountries);
                            if (!l_checkCountryOnPlayer.contains(l_country_check.get(rand_int1))) {
                                Country c = new Country(l_country_check.get(rand_int1));
                                c.setD_countryIndex(0);
                                l_country.add(c);   //add country in list
                                l_checkCountryOnPlayer.add(l_country_check.get(rand_int1));
                                p_gameData.getD_playerList().get(l_j).setD_ownedCountries(l_country);
                                break;

                            }

                        }
                    } //If player have countries 
                    else {
                        while (true) {
                            Random l_rand = new Random();
                            int l_rand_int1 = l_rand.nextInt(l_noOfCountries);
                            if (!l_checkCountryOnPlayer.contains(l_country_check.get(l_rand_int1))) {
                                p_gameData.getD_playerList().get(l_j).getD_ownedCountries().add(l_country_check.get(l_rand_int1));
                                l_checkCountryOnPlayer.add(l_country_check.get(l_rand_int1));
                                break;

                            }

                        }
                    }

                }
            }
            //Assigning country of remaining modules
            for (l_i = 0; l_i < l_modulus; l_i++) {
                while (true) {
                    Random l_rand = new Random();
                    int l_rand_int1 = l_rand.nextInt(l_noOfCountries);
                    if (!l_checkCountryOnPlayer.contains(l_country_check.get(l_rand_int1))) {
                        p_gameData.getD_playerList().get(l_i).getD_ownedCountries().add(l_country_check.get(l_rand_int1));
                        l_checkCountryOnPlayer.add(l_country_check.get(l_rand_int1));
                        break;

                    }

                }
            }
            //For creating user friendly response
            String l_response = "";
            for (l_i = 0; l_i < l_noOfPlayers; l_i++) {
                l_response = l_response + p_gameData.getD_playerList().get(l_i).getD_playerName() + " owns [";  // Response string got player owned countries
                for (l_j = 0; l_j < p_gameData.getD_playerList().get(l_i).getD_ownedCountries().size(); l_j++) {
                    if (l_j == (p_gameData.getD_playerList().get(l_i).getD_ownedCountries().size() - 1)) {
                        l_response = l_response + p_gameData.getD_playerList().get(l_i).getD_ownedCountries().get(l_j).getD_countryName();
                    } else {
                        l_response = l_response + p_gameData.getD_playerList().get(l_i).getD_ownedCountries().get(l_j).getD_countryName() + ",";
                    }

                }
                l_response = l_response + "]\n";    //formatting response
            }

            d_generalUtil.prepareResponse(true, l_response);
        }

        return d_generalUtil.getResponse();

    }




    /**
     * @param p_currentGameData Object Of current gameplay
     * @param p_playerName Player name
     * @return The List of player with given name
     */
    public List<Player> getPlayerByName(GameData p_currentGameData, String p_playerName) {
        List<Player> l_players = new ArrayList<Player>();
        if (p_playerName != null && p_currentGameData.getD_playerList() != null) {
            l_players = p_currentGameData.getD_playerList().stream().filter(l_player -> l_player.getD_playerName().equalsIgnoreCase(p_playerName)).collect(Collectors.toList());
        }

        return l_players;
    }

    /**
     * 
     * {@inheritDoc }
     */
    @Override
    public CommandResponse getResponse() {
        return d_generalUtil.getResponse();
    }
}
