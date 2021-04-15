package com.soen6441.warzone.controller;

import com.soen6441.warzone.config.StageManager;
import static com.soen6441.warzone.config.WarzoneConstants.GAME_DEF_PATH;

import com.soen6441.warzone.model.*;
import com.soen6441.warzone.observerpattern.LogEntryBuffer;
import com.soen6441.warzone.observerpattern.WriteLogFile;
import com.soen6441.warzone.service.GameConfigService;
import com.soen6441.warzone.service.GeneralUtil;
import com.soen6441.warzone.service.MapHandlingInterface;
import com.soen6441.warzone.state.StartUpPhase;
import com.soen6441.warzone.view.FxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.StageStyle;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

/**
 * This Class is made to handle Game Config controller request
 *
 * @author <a href="mailto:patelvicky1995@gmail.com">Vicky Patel</a>
 */
@EqualsAndHashCode
@Controller
public class GameConfigController implements Initializable {

    /**
     * loadmap
     */
    public static final String LOAD_MAP = "loadmap";
    /**
     * showmap
     */
    public static final String SHOW_MAP = "showmap";
    /**
     * gameplayer
     */
    public static final String GAME_PLAYER = "gameplayer";
    /**
     * assigncountries
     */
    public static final String ASSIGN_COUNTRY = "assigncountries";
    /**
     * tournament
     */
    public static final String TOURNAMENT = "tournament";
    /**
     * AssignCountryFlag
     */
    private static int AssignCountryFlag = 0;
    /**
     * FXML Component
     */
    @FXML
    private TextField d_CommandLine;
    /**
     * FXML Component
     */
    @FXML
    private TextArea d_showPlayPhase;
    /**
     * FXML Component
     */
    @FXML
    private Button d_StartGame;
    /**
     * FXML Component
     */
    @FXML
    private Button d_FireCommand;
    /**
     * FXML Component
     */
    @FXML
    private TextArea d_userGuide;

    /**
     * WarMap Object
     */
    @Autowired
    private WarMap d_warMap;

    /**
     * Gameconfig Service
     */
    @Autowired
    private GameConfigService d_gameConfigService;

    /**
     * General Util Service
     */
    @Autowired
    private GeneralUtil d_generalUtil;

    /**
     * Game Data Object
     */
    @Autowired
    private GameData d_gameData;
    /**
     * Stage Manager Object
     */
    @Lazy
    @Autowired
    private StageManager d_stageManager;

    /**
     * Map handling Service
     */
    @Autowired
    private MapHandlingInterface d_maphandlinginterface;

    /**
     * Game Engine
     */
    @Autowired
    private GameEngine d_gameEngine;

    /**
     * GameMode(Tournament, Single)
     */
    public static int Game_Mode = 0;

    /**
     * *
     * Log EntryBuffer(Observer)
     */
    private LogEntryBuffer d_logEntryBuffer = new LogEntryBuffer();
    /**
     * WriteLog File Object(Observable)
     */
    private WriteLogFile d_writeLogFile = new WriteLogFile(d_logEntryBuffer);

    /**
     * This is the initialization method of this controller
     *
     * @param p_location of the FXML file
     * @param p_resources is properties information
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL p_location, ResourceBundle p_resources) {
        d_StartGame.setDisable(true);
        d_showPlayPhase.setStyle("-fx-font-family: monospace");
    }

    /**
     * This method will redirect user to the Home Screen
     *
     * @param p_event represents value send from view
     */
    @FXML
    void backToWelcome(ActionEvent p_event) {
        d_gameData = new GameData();
        AssignCountryFlag = 0;
        d_stageManager.switchScene(FxmlView.HOME, null, "");
    }

    /**
     * This method is used to set the Game Phase
     *
     * @param p_gameEngine represent the phase to be set
     */
    public void setGameEngine(GameEngine p_gameEngine) {
        d_StartGame.setDisable(true);
        d_showPlayPhase.setStyle("-fx-font-family: monospace");
        List<String> choices = new ArrayList<>();
        choices.add("Tournament Mode");
        choices.add("Single Mode");
//Dialog Box
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Tournament Mode", choices);
        dialog.setContentText("Choose your Mode:");
        dialog.getDialogPane().getButtonTypes().remove(1);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.getDialogPane().setStyle("-fx-background-color: #fff; -fx-border-color: #000; -fx-border-width: 3;");

        Optional<String> result = dialog.showAndWait();
        //Result OF Choice
        result.ifPresent(letter -> {
            if (letter.equalsIgnoreCase("Single Mode")) {
                Game_Mode = 0;
            } else {
                Game_Mode = 1;
            }
        });
        if (Game_Mode == 1) {
            d_StartGame.setDisable(true);
            String s = "Tournament game play commands:\n"
                    + "tournament -M listofmapfiles -P listofplayerstrategies -G numberofgames -D maxnumberofturns";
            d_userGuide.setText(s);
        }

    }

    /**
     * This method will redirect user Game Start Screen
     *
     * @param p_event represent value send from view
     */
    @FXML
    void toStartGame(ActionEvent p_event) {
        StartUpPhase l_st = (StartUpPhase) d_gameEngine.getPhase();
        l_st.next(d_gameData);
    }

    /**
     * This method is used to get fire command from user and put it as a
     * parameter in validation
     *
     * @param p_event : events from view
     */
    public void getData(ActionEvent p_event) {
        String l_command = d_CommandLine.getText().trim();
        //write log about command
        d_logEntryBuffer.setLogEntryBuffer("Command:: " + l_command);
        List<String> l_commandSegments = Arrays.asList(l_command.split(" "));
        CommandResponse l_gmConfigRes = new CommandResponse();
        //Show MAp
        if (l_command.toLowerCase().startsWith(SHOW_MAP) && Game_Mode == 0) {                                                     //condition if user gives input to show the map
            if (d_gameData.getD_warMap() != null) {
                l_gmConfigRes = d_gameConfigService.showPlayerMap(d_gameData);
            } else {
                l_gmConfigRes.setD_isValid(false);
                l_gmConfigRes.setD_responseString("Please load the map first");
            }
            //LOAD_MAP
        } else if (l_command.toLowerCase().startsWith(LOAD_MAP) && Game_Mode == 0) {                                               //condition satisfies if user wants to load the map
            if (AssignCountryFlag == 1) {                                          //if countries are assigned already then ,this condition won't allow to load map again
                l_gmConfigRes.setD_isValid(false);
                l_gmConfigRes.setD_responseString("countries are already assigned to each player");
            } else {
                String l_fileName = (l_commandSegments != null && l_commandSegments.size() == 2) ? l_commandSegments.get(1) : null;
                if (l_fileName != null) {
                    if (d_generalUtil.validateIOString(l_fileName, "^[a-zA-Z]+.?[a-zA-Z]+") || d_generalUtil.validateIOString(l_fileName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")) {      //validates the filename given by user
                        l_gmConfigRes = loadMap(l_fileName);
                    } else {
                        d_generalUtil.prepareResponse(false, "Please enter valid file name for loadmap command");
                        l_gmConfigRes = d_generalUtil.getResponse();
                    }
                } else {
                    d_generalUtil.prepareResponse(false, "Please enter valid loadmap command");
                    l_gmConfigRes = d_generalUtil.getResponse();
                }
            }
        } //        GAME_PLAYER
        else if (l_command.toLowerCase().startsWith(GAME_PLAYER) && Game_Mode == 0) {                                  //if user wants to add or remove players
            if (AssignCountryFlag == 1) {                                                  //if countries are assigned already then ,this condition won't allow to add player again
                l_gmConfigRes.setD_isValid(false);
                l_gmConfigRes.setD_responseString("Countries are already assigned to each player");
            } else {
                if (d_gameData.getD_warMap() != null) {
                    if (d_generalUtil.validateIOString(l_command, "gameplayer((\\s-add\\s[a-z|A-Z|_-]+\\s(human|random|cheater|aggressive|benevolent))|(\\s-remove\\s[a-z|A-Z|_-]+))+")) {                                 //validates the command
                        Map.Entry<GameData, CommandResponse> l_updatedGamePlay = d_gameConfigService.updatePlayer(d_gameData, l_command);

                        if (l_updatedGamePlay.getValue().isD_isValid()) {
                            d_gameData = l_updatedGamePlay.getKey();
                            String l_playerName = "\nPlayers : \n[";
                            if (d_gameData.getD_playerList() != null) {
                                for (Player l_p : d_gameData.getD_playerList()) {           //stores players name and print
                                    l_playerName = l_playerName + " " + l_p.getD_playerName() + " :" + Strategies.strategyToStringMapper(l_p.getD_stragey()) + " ,";
                                }
                                l_playerName = l_playerName + "]";
                            }
                            l_updatedGamePlay.getValue().setD_responseString(l_updatedGamePlay.getValue().getD_responseString() + l_playerName);
                        }
                        d_generalUtil.prepareResponse(l_updatedGamePlay.getValue().isD_isValid(), l_updatedGamePlay.getValue().getD_responseString());
                    } else {                                                                    //if command is not valid
                        d_generalUtil.prepareResponse(false, "Please enter valid Game Player command!@");
                    }
                    l_gmConfigRes = d_generalUtil.getResponse();
                } else {                                                                     //if map of game engine is empty
                    l_gmConfigRes.setD_isValid(false);
                    l_gmConfigRes.setD_responseString("Please load the map first");
                }
            }
        } //ASSIGN_COUNTRY
        else if (l_command.toLowerCase().startsWith(ASSIGN_COUNTRY) && Game_Mode == 0) {                           //if user wants to assigncountries to players
            if (d_gameData.getD_warMap() == null) {
                l_gmConfigRes.setD_isValid(false);
                l_gmConfigRes.setD_responseString("Please load the map first");
            } else {
                if (l_commandSegments.size() == 1) {                                          //to validate the command
                    l_gmConfigRes = d_gameConfigService.assignCountries(d_gameData);
                    if (l_gmConfigRes.isD_isValid()) {
                        d_StartGame.setDisable(false);
                        AssignCountryFlag = 1;
                    }
                } else {                                                                        //if validation of command fails
                    d_generalUtil.prepareResponse(false, "Please enter validloadmap command");
                    l_gmConfigRes = d_generalUtil.getResponse();
                }
            }
        } else if (d_generalUtil.validateIOString(l_command, "savegame\\s+[a-zA-Z]+.?[a-zA-Z]+") && l_commandSegments.size() == 2) {
            if (d_gameData.getD_warMap() != null) {
                d_gameEngine.saveGame(d_gameData, l_commandSegments.get(1));
                d_generalUtil.prepareResponse(true, "Game saved successfully.");
            } else {
                d_generalUtil.prepareResponse(false, "Nothing to save.");
            }
            l_gmConfigRes = d_generalUtil.getResponse();
        } else if (d_generalUtil.validateIOString(l_command, "loadgame\\s+[a-zA-Z]+.?[a-zA-Z]+") && l_commandSegments.size() == 2) {
            try {
                List<String> l_games = new ArrayList<>();

                // get available files in games directory
                l_games = d_generalUtil.getListOfAllFiles(Paths.get(GAME_DEF_PATH), ".txt");

                // check file extension entered by user
                String l_fullName;
                int l_index = l_commandSegments.get(1).lastIndexOf('.');
                l_fullName = l_index > 0
                        ? l_commandSegments.get(1).toLowerCase() : l_commandSegments.get(1).toLowerCase() + ".txt";

                // check if file exists
                if (l_games.contains(l_fullName)) {
                    d_gameData = d_gameEngine.loadGame(l_fullName);

                    // check game data is null or not
                    if (d_gameData != null) {
                        // check player is added or not
                        if (d_gameData.getD_playerList() != null) {
                            // check countries are assigned or not
                            if (d_gameData.getD_playerList().get(0).getD_ownedCountries().size() != 0) {
                                d_StartGame.setDisable(false);
                                AssignCountryFlag = 1;
                                d_generalUtil.prepareResponse(true, "Game loaded successfully.");
                            } else {
                                d_StartGame.setDisable(true);
                                AssignCountryFlag = 0;
                                d_generalUtil.prepareResponse(true, "Game loaded successfully. Please run assigncountries command to play game!!");
                            }
                        } else {
                            d_generalUtil.prepareResponse(false, "Game loaded successfully. Please add players and run assigncountries\n command to play game!!");
                        }
                    } else {
                        d_generalUtil.prepareResponse(false, "File does not contains valid game data.");
                    }
                } else {
                    d_generalUtil.prepareResponse(false, "File does not found.");
                }

                l_gmConfigRes = d_generalUtil.getResponse();
            } catch (Exception e) {
                d_generalUtil.prepareResponse(false, "Error in loadgame command");
                l_gmConfigRes = d_generalUtil.getResponse();
            }
        } else if (l_command.toLowerCase().startsWith(TOURNAMENT) && Game_Mode == 1) {
            if (d_generalUtil.validateIOString(l_command, "tournament\\s-M\\s([a-zA-Z]+|([a-zA-Z]+\\.[a-zA-Z]+))((,[a-zA-Z]+\\.[a-zA-Z]+)|(,[a-zA-Z]+))*\\s-P\\s[a-zA-z]+(,[a-zA-z]+)*\\s-G\\s[1-5]\\s-D\\s[1-5][0-9]")) {
                String l_s = "";
                List<String> l_strategy = new ArrayList<>();
                boolean l_validate = false;
                int l_vl = 0;
                l_strategy.add("aggressive");
                l_strategy.add("random");
                l_strategy.add("benevolent");
                l_strategy.add("cheater");
                List<String> l_maps = Arrays.asList(l_commandSegments.get(2).split(","));
                List<String> l_players = Arrays.asList(l_commandSegments.get(4).split(","));
                int l_noOfGames = Integer.parseInt(l_commandSegments.get(6));
                int l_noOfTurns = Integer.parseInt(l_commandSegments.get(8));
                l_s = l_s + "maps are : " + l_maps + "\n Players are: " + l_players + "\nNo. of games is: " + l_noOfGames + "\nNo. of Turns is " + l_noOfTurns + "\n";
                for (String l_p : l_players) {
                    if (!(l_strategy.contains(l_p.toLowerCase()))) {
                        l_vl = 1;
                    }
                }
                if (l_noOfGames > 0 && l_noOfGames < 6 && l_maps.size() > 0 && l_maps.size() < 6 && l_players.size() > 0
                        && l_players.size() < 5 && l_noOfTurns > 9 && l_noOfTurns < 51 && l_vl == 0) {
                    l_validate = true;
                }
                if (l_validate) {
                    d_generalUtil.prepareResponse(l_validate, l_s);              //general command if none of the above condition matches
                    l_gmConfigRes = d_generalUtil.getResponse();
                    try {
                        Tournament l_t = d_gameEngine.createTournament(l_command);
                        if (l_t == null) {
                            l_gmConfigRes = d_gameEngine.d_tournamentResponse;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    d_generalUtil.prepareResponse(l_validate, "command is not valid with args");              //general command if none of the above condition matches
                    l_gmConfigRes = d_generalUtil.getResponse();
                }
            } else {
                d_generalUtil.prepareResponse(false, "command is not valid with args");
                //general command if none of the above condition matches
                l_gmConfigRes = d_generalUtil.getResponse();
            }

        } else {
            d_generalUtil.prepareResponse(false, "Please enter valid command");              //general command if none of the above condition matches
            l_gmConfigRes = d_generalUtil.getResponse();

        }

        d_showPlayPhase.setText(l_gmConfigRes.toString());
        d_CommandLine.clear();
    }

    /**
     * This is used as Sub function for Loading map
     *
     * @param p_fileName : File Name to load file
     * @return CommandResponse of the loadMap Command
     */
    public CommandResponse loadMap(String p_fileName) {

        List<String> l_mapFileNameList;
        try {
            l_mapFileNameList = d_generalUtil.getAvailableMapFiles();

            String l_fullName;
            int l_index = p_fileName.lastIndexOf('.');
            l_fullName = l_index > 0
                    ? p_fileName.toLowerCase() : p_fileName.toLowerCase() + ".map";
            if (l_mapFileNameList.contains(l_fullName)) {                                     //check whether file is present or not
                try {
                    d_warMap = d_gameConfigService.loadMap(l_fullName);
                    if (d_maphandlinginterface.validateMap(d_warMap)) {                       //validation of file
                        d_warMap.setD_status(true);                                           // Set status and map file name
                        d_warMap.setD_mapName(l_fullName);
                        d_generalUtil.prepareResponse(true, "Map loaded successfully!");
                        d_gameData.setD_warMap(d_warMap);                                     //set loaded map in the Game play object
                        d_gameData.setD_fileName(p_fileName);
                        d_warMap.setD_mapName(l_fullName);
                    } else {
                        d_generalUtil.prepareResponse(false, "Map is Invalid, Please select another map");
                    }
                } catch (IOException e) {
                    d_generalUtil.prepareResponse(false, "Exception in EditMap, Invalid Map Please correct Map");
                }
            } else {
                d_generalUtil.prepareResponse(false, "Map not found in system");
            }
        } catch (IOException ex) {
            d_generalUtil.prepareResponse(false, "Not able to get the Maps");
        }
        return d_generalUtil.getResponse();

    }

}
