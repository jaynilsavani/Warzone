package com.soen6441.warzone.controller;

import com.soen6441.warzone.config.StageManager;
import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GamePlay;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.model.WarMap;
import com.soen6441.warzone.service.GameConfigService;
import com.soen6441.warzone.service.GeneralUtil;
import com.soen6441.warzone.service.MapHandlingInterface;
import com.soen6441.warzone.view.FxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This Class is made to handle Game Config controller request
 *
 * @author <a href="mailto:patelvicky1995@gmail.com">Vicky Patel</a>
 */
@Controller
public class GameConfigController implements Initializable {

    public static final String LOAD_MAP = "loadmap";
    public static final String SHOW_MAP = "showmap";
    public static final String GAME_PLAYER = "gameplayer";
    public static final String ASSIGN_COUNTRY = "assigncountries";
    private static int AssignCountryFlag = 0;

    @FXML
    private TextField d_CommandLine;

    @FXML
    private TextArea d_showPlayPhase;

    @FXML
    private Button d_StartGame;

    @Autowired
    private WarMap d_warMap;

    @Autowired
    private GameConfigService d_gameConfigService;

    @Autowired
    private GeneralUtil d_generalUtil;

    @Autowired
    private GamePlay d_gamePlay;

    @Lazy
    @Autowired
    private StageManager d_stageManager;

    @Autowired
    private MapHandlingInterface d_maphandlinginterface;

    /**
     * This is the initialization method of this controller
     *
     * @param p_location  of the FXML file
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
        d_stageManager.switchScene(FxmlView.HOME, null);
        d_gamePlay = new GamePlay();
    }

    /**
     * This method will redirect user Game Start Screen
     *
     * @param p_event represent value send from view
     */
    @FXML
    void toStartGame(ActionEvent p_event) {

        d_stageManager.switchScene(FxmlView.GAMEENGINE, d_gamePlay);
    }

    /**
     * This method is used to get fire command from user and put it as a
     * parameter in validation
     *
     * @param p_event : events from view
     */
    public void getData(ActionEvent p_event) {
        String l_command = d_CommandLine.getText();
        List<String> l_commandSegments = Arrays.asList(l_command.split(" "));
        CommandResponse l_gmConfigRes = new CommandResponse();

        if (l_command.toLowerCase().startsWith(SHOW_MAP)) {                                                     //condition if user gives input to show the map
            if (d_gamePlay.getD_warMap() != null) {
                l_gmConfigRes = d_gameConfigService.showPlayerMap(d_gamePlay);
            } else {
                l_gmConfigRes.setD_isValid(false);
                l_gmConfigRes.setD_responseString("Please load the map first");
            }
        } else if (l_command.toLowerCase().startsWith(LOAD_MAP)) {                                               //condition satisfies if user wants to load the map
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
                    d_generalUtil.prepareResponse(false, "Please enter validloadmap command");
                    l_gmConfigRes = d_generalUtil.getResponse();
                }
            }
        } else if (l_command.toLowerCase().startsWith(GAME_PLAYER)) {                                  //if user wants to add or remove players
            if (AssignCountryFlag == 1) {                                                  //if countries are assigned already then ,this condition won't allow to add player again
                l_gmConfigRes.setD_isValid(false);
                l_gmConfigRes.setD_responseString("countries are already assigned to each player");
            } else {
                if (d_gamePlay.getD_warMap() != null) {
                    if ((l_commandSegments.size() - 1) % 2 == 0) {                                 //validates the command
                        d_gamePlay = d_gameConfigService.updatePlayer(d_gamePlay, l_command);
                        String l_playerName = "Players are updated Sucessfully\n[";
                        for (Player l_p : d_gamePlay.getD_playerList()) {
                            l_playerName = l_playerName + " " + l_p.getD_playerName() + ",";
                        }
                        l_playerName = l_playerName + "]";

                        d_generalUtil.prepareResponse(true, l_playerName);
                    } else {                                                                    //if command is not valid
                        d_generalUtil.prepareResponse(false, "Please enter valid Game Player command");
                    }
                    l_gmConfigRes = d_generalUtil.getResponse();
                } else {                                                                     //if map of game engine is empty
                    l_gmConfigRes.setD_isValid(false);
                    l_gmConfigRes.setD_responseString("Please load the map first");
                }
            }
        } else if (l_command.toLowerCase().startsWith(ASSIGN_COUNTRY)) {                           //if user wants to assigncountries to players
            if (d_gamePlay.getD_warMap() == null) {
                l_gmConfigRes.setD_isValid(false);
                l_gmConfigRes.setD_responseString("Please load the map first");
            } else {
                if (l_commandSegments.size() == 1) {                                          //to validate the command
                    try {
                        l_gmConfigRes = d_gameConfigService.assignCountries(d_gamePlay);
                        if (l_gmConfigRes.isD_isValid()) {
                            d_StartGame.setDisable(false);
                            AssignCountryFlag = 1;
                        }
                    } catch (IOException e) {
                        l_gmConfigRes.setD_isValid(false);
                        l_gmConfigRes.setD_responseString("Players are not added due to map is not readable");
                    }

                } else {                                                                        //if validation of command fails
                    d_generalUtil.prepareResponse(false, "Please enter validloadmap command");
                    l_gmConfigRes = d_generalUtil.getResponse();
                }
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
                        d_gamePlay.setD_warMap(d_warMap);                                     //set loaded map in the Game play object
                        d_gamePlay.setD_fileName(p_fileName);
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
