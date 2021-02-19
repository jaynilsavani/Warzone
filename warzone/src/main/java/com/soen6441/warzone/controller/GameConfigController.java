package com.soen6441.warzone.controller;

import com.soen6441.warzone.view.FxmlView;
import com.soen6441.warzone.config.StageManager;
import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GamePlay;
import com.soen6441.warzone.model.WarMap;
import com.soen6441.warzone.service.GameConfigService;
import com.soen6441.warzone.service.GeneralUtil;
import com.soen6441.warzone.service.MapHandlingInterface;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.TextArea;

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
    public static int AssignCountryFlag=0;

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
     * @param location of the FXML file
     * @param resources is properties information
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        d_StartGame.setDisable(true);
        d_showPlayPhase.setStyle("-fx-font-family: monospace");
    }

    /**
     * This method will redirect user to the Home Screen
     *
     * @param event represents value send from view
     */
    @FXML
    void backToWelcome(ActionEvent event) {
        d_stageManager.switchScene(FxmlView.HOME, null);
        d_warMap = null;
        d_gamePlay = null;
    }

    /**
     * This method will redirect user Game Start Screen
     *
     * @param event represent value send from view
     */
    @FXML
    void toStartGame(ActionEvent event) {

        d_stageManager.switchScene(FxmlView.GAMEENGINE, d_gamePlay);
    }

    /**
     * This method is used to get fire command from user and put it as a
     * parameter in validation
     *
     * @param event
     */
    public void getData(ActionEvent event) {
        String l_command = d_CommandLine.getText();
        List<String> l_commandSegments = Arrays.asList(l_command.split(" "));
        CommandResponse l_gmConfigRes = new CommandResponse();
        if (l_command.toLowerCase().startsWith(SHOW_MAP)) {
            if (d_warMap != null) {
                l_gmConfigRes = d_gameConfigService.showPlayerMap(d_gamePlay);
                //d_showPlayPhase.appendText(l_gmConfigRes.toString());
            } else {
                l_gmConfigRes.setD_isValid(false);
                l_gmConfigRes.setD_responseString("Please load the map first");
            }
        } else if (l_command.toLowerCase().startsWith(LOAD_MAP)) {
            if(AssignCountryFlag==1)
            {
                l_gmConfigRes.setD_isValid(false);
                l_gmConfigRes.setD_responseString("countries are already assigned to each player");
            }
            String l_fileName = (l_commandSegments != null && l_commandSegments.size() == 2) ? l_commandSegments.get(1) : null;
            if (l_fileName != null) {
                if (d_generalUtil.validateIOString(l_fileName, "^[a-zA-Z]+.?[a-zA-Z]+") || d_generalUtil.validateIOString(l_fileName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")) {
                    l_gmConfigRes = loadMap(l_fileName);
                } else {
                    d_generalUtil.prepareResponse(false, "Please enter valid file name for loadmap command");
                    l_gmConfigRes = d_generalUtil.getResponse();
                }
            } else {
                d_generalUtil.prepareResponse(false, "Please enter validloadmap command");
                l_gmConfigRes = d_generalUtil.getResponse();
            }

        } else if (l_command.toLowerCase().startsWith(GAME_PLAYER)) {
            if(AssignCountryFlag==1)
            {
                l_gmConfigRes.setD_isValid(false);
                l_gmConfigRes.setD_responseString("countries are already assigned to each player");
            }
            if (d_gamePlay.getD_warMap() != null) {
                if ((l_commandSegments.size() - 1) % 2 == 0) {
                    d_gamePlay = d_gameConfigService.updatePlayer(d_gamePlay, l_command);
                    d_generalUtil.prepareResponse(true, "Please updated Sucessfully");
                } else {
                    d_generalUtil.prepareResponse(false, "Please enter valid Game Player command");
                }
                l_gmConfigRes = d_generalUtil.getResponse();
            }

        } else if (l_command.toLowerCase().startsWith(ASSIGN_COUNTRY)) {
            if (l_commandSegments.size() == 1) {
                try {
                    l_gmConfigRes= d_gameConfigService.assignCountries(d_gamePlay);
                    if(l_gmConfigRes.isD_isValid())
                    {
                        d_StartGame.setDisable(false);
                        AssignCountryFlag=1;
                    }
                    //d_showPlayPhase.appendText(l_gmConfigRes.toString());
                } catch (IOException e) {
                    l_gmConfigRes.setD_isValid(false);
                    l_gmConfigRes.setD_responseString("not able to assign countries due to map is not readable");
                    //d_showPlayPhase.appendText(l_gmConfigRes.toString());
                }

            } else {
                d_generalUtil.prepareResponse(false, "Please enter validloadmap command");
                l_gmConfigRes = d_generalUtil.getResponse();
            }

        } else {
            d_generalUtil.prepareResponse(false, "Please enter valid command");
            l_gmConfigRes = d_generalUtil.getResponse();

        }

        d_showPlayPhase.setText(l_gmConfigRes.toString());
//        d_CommandLine.clear();
    }

    //Utility functions For above Command execution 
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
            int index = p_fileName.lastIndexOf('.');
            l_fullName = index > 0
                    ? p_fileName.toLowerCase() : p_fileName.toLowerCase() + ".map";
            //check whether file is present or not
            if (l_mapFileNameList.contains(l_fullName)) {
                try {

                    d_warMap = d_gameConfigService.loadMap(l_fullName);
                    // Set status and map file name 
                    d_warMap.setD_status(true);
                    d_warMap.setD_mapName(l_fullName);
                    d_generalUtil.prepareResponse(true, "Map loaded successfully!");
                    //set loaded map in the Game play object
                    d_gamePlay.setD_warMap(d_warMap);
                    d_gamePlay.setFileName(p_fileName);
                    d_warMap.setD_mapName(l_fullName);
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
