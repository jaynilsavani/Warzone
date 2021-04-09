package com.soen6441.warzone.controller;

import com.soen6441.warzone.config.StageManager;

import static com.soen6441.warzone.config.WarzoneConstants.*;

import com.soen6441.warzone.model.*;
import com.soen6441.warzone.observerpattern.LogEntryBuffer;
import com.soen6441.warzone.observerpattern.WriteLogFile;
import com.soen6441.warzone.service.GameConfigService;
import com.soen6441.warzone.service.GameEngineService;
import com.soen6441.warzone.service.GeneralUtil;
import com.soen6441.warzone.service.OrderProcessor;
import com.soen6441.warzone.service.impl.OrderProcessorImpl;
import com.soen6441.warzone.state.GamePlay;
import com.soen6441.warzone.state.IssueOrderPhase;
import com.soen6441.warzone.state.MapPhase;
import com.soen6441.warzone.state.Phase;
import com.soen6441.warzone.state.StartUpPhase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;

import javafx.scene.Parent;
import org.springframework.context.annotation.Lazy;

/**
 * This Class is made to handle Game Engine controller request
 *
 * @author <a href="mailto:patelvicky1995@gmail.com">Vicky Patel</a>
 */
@Controller
public class GameEngine implements Initializable {

    public Phase gamePhase;
    /**
     * array of falg that shows that whether player is done with issuing order
     * or not for particular round
     */
    public int[] d_playerFlag;

    /**
     * counter that invokes after each order issued by player
     */
    public int d_playCounter = 0;

    @FXML
    public TextArea d_TerritoryListText;
    @FXML
    public TextArea d_ContinentText;
    @FXML
    public TextArea d_TerritoryPlayerArmyText;
    @Autowired
    public GeneralUtil d_generalUtil;
    @Autowired
    public GameEngineService d_gameEngineSevice;
    @Autowired
    public OrderProcessor d_orderProcessor;
    @Autowired
    public Player d_player;
    @FXML
    private Button d_BtnExit;
    @Lazy
    @Autowired
    private StageManager d_stageManager;
    @Autowired
    private GameData d_gameData;
    @FXML
    private TextField d_CommandLine;
    @FXML
    private TextArea d_FireCommandList;
    @FXML
    private Button d_FireCommand;
    @FXML
    private Label d_playerTurn;
    @FXML
    private  TextArea d_countriesList;
    @FXML
    private  TextArea d_neighboursList;
    @FXML
    private TextArea d_continentToCountry;
    @Autowired
    private GameConfigService d_gameConfig;
    private LogEntryBuffer d_logEntryBuffer = new LogEntryBuffer();
    private WriteLogFile d_writeLogFile = new WriteLogFile(d_logEntryBuffer);

    /**
     * This method will exit the game and close the stage
     *
     * @param p_event will represents value sent from view
     */
    @FXML
    public void exitGame(ActionEvent p_event) {
        d_logEntryBuffer.setLogEntryBuffer("Exit Game\n");
        Stage l_stage = (Stage) d_BtnExit.getScene().getWindow();
        l_stage.close();
    }

    /**
     * This method will get the phase name
     *
     * @return represent current game phase of the game
     */
    public Phase getPhase() {
        if (gamePhase != null) {
            return gamePhase;
        }
        return null;

    }

    /**
     * This method will set the phase of game and write that in log file
     *
     * @param p_phase value sent from the State
     */
    public void setPhase(Phase p_phase) {
        gamePhase = p_phase;
        d_logEntryBuffer.setLogEntryBuffer("-------------" + p_phase.getClass().getSimpleName() + "-------------");
    }

    /**
     * This method will used to get the stage manager name
     *
     * @return current stage of game
     */
    public StageManager getStageManager() {
        return d_stageManager;
    }

    /**
     * This method is used to set the initial phase of the game to map phase or
     * start up phase
     *
     * @param p_phaseName phase which is to be set
     * @return root object of game screen
     */
    public Parent setInitialPhase(String p_phaseName) {
        switch (p_phaseName) {
            case PHASE_MAP:
                gamePhase = new MapPhase(this);
                break;
            case PHASE_GAME_START_UP:
                gamePhase = new StartUpPhase(this);

        }
        this.setPhase(gamePhase);
        return gamePhase.execute();
    }

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
        d_FireCommandList.setStyle("-fx-font-family: monospace");
    }

    /**
     * This method is used to get fire command from user and put it as a
     * parameter in validation
     *
     * @param p_event handling the user events
     */
    public void getData(ActionEvent p_event) {
        String l_commandString = d_CommandLine.getText().trim();
        String[] l_validatestr = l_commandString.split("\\s");
        boolean l_winner = false;
        if ((d_generalUtil.validateIOString(l_commandString, "(advance|airlift)\\s+[a-zA-Z-_]+\\s+[a-zA-Z-_]+\\s+[1-9][0-9]*") && l_validatestr.length == 4) || (d_generalUtil.validateIOString(l_commandString, "(bomb|blockade|negotiate)\\s+[a-zA-Z-_]+") && l_validatestr.length == 2) || (d_generalUtil.validateIOString(l_commandString, "deploy\\s+[a-zA-Z-_]+\\s+[1-9][0-9]*") && l_validatestr.length == 3) || l_commandString.equalsIgnoreCase("done")) { //validating that user input should be in "deploy string int"
            d_CommandLine.clear();
            IssueOrderPhase l_issueorder = (IssueOrderPhase) gamePhase;
            l_issueorder.d_gameData = d_gameData;
            l_issueorder.issueOrder(l_commandString);                    //to invoke the issue order after player gives the command
            CommandResponse l_commandResponse = l_issueorder.d_issueResponse;
            d_gameData = l_issueorder.d_gameData;
            d_FireCommandList.appendText(l_commandResponse.getD_responseString() + "\n");
            while (true) {                                                       //loop to check for which player gets a turn
                int l_j = 0;
                for (int l_i = 0; l_i < d_playerFlag.length; l_i++) {
                    if (d_playerFlag[l_i] == 1) {
                        l_j++;
                    }
                }
                if (l_j == d_playerFlag.length) {
                    //to reset the round after each player is done with issuing orders
                    l_issueorder = (IssueOrderPhase) gamePhase;
                    l_issueorder.d_gameData = d_gameData;
                    l_issueorder.next("");
                    IssueOrderPhase l_issuephase = (IssueOrderPhase) gamePhase;
                    d_gameData = l_issueorder.d_gameData;
                    List<CommandResponse> l_commandList = l_issuephase.d_commandResponses;
                    for (int l_i = 0; l_i < l_commandList.size(); l_i++) {        //to add the result of each command that was issued to textarea
                        d_FireCommandList.appendText(l_commandList.get(l_i).getD_responseString());
                        //to check the winner
                        if (l_commandList.get(l_i).getD_responseString().contains("IS WINNER!!!")) {
                            l_winner = true;
                            d_CommandLine.setText(l_commandList.get(l_i).getD_responseString());
                            d_CommandLine.setDisable(true);
                            d_FireCommand.setDisable(true);
                            d_playerTurn.setText("");
                            break;

                        }
                    }
                    if (!l_winner) {
                        d_FireCommandList.appendText(d_gameEngineSevice.playerOwnedCountries(d_gameData));
                        CommandResponse l_map = d_gameConfig.showPlayerMap(d_gameData);           //to show the map and player*country table
                        d_FireCommandList.appendText(l_map.getD_responseString());
                        for (Player l_player : d_gameData.getD_playerList()) {
                            l_player.setD_isWinner(false);
                            l_player.setD_negotiatePlayerList(new ArrayList<>());
                            l_player.setD_negotiatePlayer("");
                        }
                        d_playCounter = 0;
                        d_gameData.setD_maxNumberOfTurns(0);
                        Arrays.fill(d_playerFlag, 0);                                   //flag that resets the issue counter
                        l_issueorder.d_gameData = d_gameData;
                        l_issueorder.assignReinforcements();                    //for reinforcement
                        d_gameData = l_issueorder.d_gameData;
                        d_FireCommandList.appendText("\n" + d_gameEngineSevice.showReinforcementArmies(d_gameData));
                        d_playerTurn.setText(d_gameData.getD_playerList().get(d_playCounter).getD_playerName() + "'s turn");    //label to show player's turn
                        d_playerTurn.setFont(Font.font(Font.getFontNames().get(0)));
                        d_playerTurn.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
                        d_CommandLine.clear();

                    }
                    break;
                }

                d_playCounter++;
                if (d_playCounter == d_playerFlag.length) {                                   //to reset the counter if it matches the number of player
                    d_playCounter = 0;
                }

                if (d_playerFlag[d_playCounter] == 1) {                                            //it checks that plalyer is done with issues and continue loop
                    continue;
                } else if (d_playerFlag[d_playCounter] == 0) {                                       //break the loop if finds the next player available to issue an order
                    d_playerTurn.setText(d_gameData.getD_playerList().get(d_playCounter).getD_playerName() + "'s turn");
                    d_playerTurn.setFont(Font.font(Font.getFontNames().get(0)));
                    d_playerTurn.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
                    d_CommandLine.clear();
                    break;
                }
            }
        } else {                                         //shows an alert if command is invalid
            Alert l_alert = new Alert(AlertType.CONFIRMATION);
            l_alert.setTitle("Invalid");
            l_alert.setHeaderText(null);
            l_alert.setContentText("Invalid Command!!!");   //message shown in of alert
            l_alert.showAndWait();
        }
    }

    /**
     * This is used for setting GameConfig for GameEngine
     */
    public void setGamePlay() {
        GamePlay l_gamePlay = (GamePlay) gamePhase;
        d_gameData = l_gamePlay.d_gameData;
        showMapContents(l_gamePlay);
        d_playerTurn.setText(d_gameData.getD_playerList().get(d_playCounter).getD_playerName() + "'s turn");  //shows whose turn now is
        d_playerTurn.setFont(Font.font(Font.getFontNames().get(0)));
        d_playerTurn.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
        d_playerFlag = new int[d_gameData.getD_playerList().size()];
        Arrays.fill(d_playerFlag, 0);       //flag that resets the issue order
        d_FireCommandList.appendText(d_gameEngineSevice.playerOwnedCountries(d_gameData));
        IssueOrderPhase l_issueorder = (IssueOrderPhase) gamePhase;     //take issue order form user
        l_issueorder.d_gameData = d_gameData;
        l_issueorder.assignReinforcements();                    //for reinforcement
        d_gameData = l_issueorder.d_gameData;
        d_FireCommandList.appendText(d_gameEngineSevice.showReinforcementArmies(d_gameData));
        d_FireCommandList.appendText(d_gameConfig.showPlayerMap(d_gameData).getD_responseString());           //to show the map and player*country table
    }

    /**
     * used to add the data of warmap to ui
     * @param p_gameplay object to fetch the data of warmap from game engine
     */
    public void showMapContents(GamePlay p_gameplay)
    {
        String l_seperator=" ==> ";
        String l_coutriesList="",l_neighbourList="",l_continents="";
        for (Map.Entry<Integer, Continent> l_entry : p_gameplay.d_gameData.getD_warMap().getD_continents().entrySet()) {
            l_continents=l_continents+l_entry.getValue().getD_continentName()+l_seperator;
            for (Country l_country : l_entry.getValue().getD_countryList()) {
                l_continents=l_continents+l_country.getD_countryName()+" , " ;
            l_coutriesList=l_coutriesList+l_country.getD_countryName()+" ("+l_entry.getValue().getD_continentName()+")\n";
                l_neighbourList=l_neighbourList+l_country.getD_countryName()+l_seperator;
                for(String l_neighnoours :l_country.getD_neighbourCountries())
                {
                    l_neighbourList=l_neighbourList+l_neighnoours+" , ";
                }
                l_neighbourList=l_neighbourList+"\n";
            }
            l_continents=l_continents+"\n";
        }
        d_countriesList.appendText(l_coutriesList);
        d_neighboursList.appendText(l_neighbourList);
        d_continentToCountry.appendText(l_continents);

    }

    /**
     * This method is used to save game data in file
     *
     * @param p_gameData current game data
     */
    public boolean saveGame(GameData p_gameData, String p_fileName) {
        // for managing the .txt extension of the file
        if (p_fileName.contains(".")) {
            String l_fileNameSplit = p_fileName.split("\\.")[1];
            if (!l_fileNameSplit.equals("txt")) {
                p_fileName = p_fileName.concat(".txt");
            }
        } else {
            p_fileName = p_fileName.concat(".txt");
        }
        boolean l_status;
        try {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(GAME_DEF_PATH + p_fileName), "utf-8")));) {
                List<StringBuilder> l_stringBuilderList = new ArrayList<>();

                l_stringBuilderList.addAll(writeMapData(p_gameData));
                l_stringBuilderList.addAll(writePlayerData(p_gameData));

                writer.println("Date: " + (new Date()).toString());
                writer.println("Author: " + System.getProperty("user.name"));
                writer.println();
                writer.println("Game Data");
                writer.println();
                writer.println("[map_name]");
                writer.println(p_gameData.getD_warMap().getD_mapName());
                writer.println();
                for(StringBuilder l_stringBuilder : l_stringBuilderList){
                    writer.println(l_stringBuilder.toString());
                }
                l_status = true;
            }
        } catch (Exception e) {
            l_status = false;
        }

        return l_status;
    }

    /**
     * This method is used to write map data
     *
     * @param p_gameData current game data object
     * @return string of continents, countries, neighbours
     */
    public List<StringBuilder> writeMapData(GameData p_gameData)
    {
        List<StringBuilder> l_stringBuilderList = new ArrayList<>();

        //creation of content to write into file
        StringBuilder l_continentStringBuilder = new StringBuilder(CONTINENTS).append(System.lineSeparator());
        StringBuilder l_countryStringBuilder = new StringBuilder(COUNTRIES).append(System.lineSeparator());
        StringBuilder l_neighborStringBuilder = new StringBuilder(BORDERS).append(System.lineSeparator());
        Map<Integer, Continent> l_continentMap = p_gameData.getD_warMap().getD_continents();
        //for storing continent of the map

        for (Map.Entry<Integer, Continent> l_entry : l_continentMap.entrySet()) {
            Continent l_currentContinent = l_entry.getValue();

            //here all continents will store into the l_continentStringBuilder
            l_continentStringBuilder.append(l_currentContinent.getD_continentName() + " " + l_currentContinent.getD_continentValue()).append(System.lineSeparator());
            if (l_currentContinent.getD_countryList() != null) {
                List<Country> l_countryList = l_currentContinent.getD_countryList();
                for (Country l_country : l_countryList) {

                    //here all countries will store into the l_countryStringBuilder
                    l_countryStringBuilder.append(l_country.getD_countryIndex() + " " + l_country.getD_countryName() + " " + l_country.getD_continentIndex() + " " + l_country.getD_noOfArmies())
                            .append(System.lineSeparator());

                    if (l_country.getD_neighbourCountries() != null) {
                        List<String> l_neighborList = l_country.getD_neighbourCountries();
                        if (!l_neighborList.isEmpty() && l_neighborList != null) {
                            l_neighborStringBuilder.append(l_country.getD_countryIndex());
                            for (String l_neighborName : l_neighborList) {
                                //here all neighbors will store into the l_neighborStringBuilder
                                l_neighborStringBuilder.append(" " + getCountryIndexByCountryName(p_gameData.getD_warMap(), l_neighborName));
                            }
                            l_neighborStringBuilder.append(System.lineSeparator());
                        }
                    }
                }
            }
        }
        l_stringBuilderList.add(l_continentStringBuilder);
        l_stringBuilderList.add(l_countryStringBuilder);
        l_stringBuilderList.add(l_neighborStringBuilder);

        return l_stringBuilderList;
    }

    /**
     * This method is used to write player information 
     * @param p_gameData current game data object
     * @return list of player, countries owned by players and orders issued by player
     */
    public List<StringBuilder> writePlayerData(GameData p_gameData) {
        List<StringBuilder> l_stringBuilderList = new ArrayList<>();
        StringBuilder l_players = new StringBuilder(PLAYERS).append(System.lineSeparator());
        StringBuilder l_ownedCountries = new StringBuilder(OWNED_COUNTRIES).append(System.lineSeparator());
        StringBuilder l_orders = new StringBuilder(ORDERS).append(System.lineSeparator());
        StringBuilder l_cards = new StringBuilder(CARDS).append(System.lineSeparator());
        StringBuilder l_negotiatePlayers = new StringBuilder(NEGOTIATE_PLAYERS).append(System.lineSeparator());
        StringBuilder l_playerFlag = new StringBuilder(PLAYER_FLAG).append(System.lineSeparator());
        StringBuilder l_gamePhase = new StringBuilder(GAME_PHASE).append(System.lineSeparator());

        if(p_gameData.getD_playerList() != null) {
            for (Player l_player : p_gameData.getD_playerList()) {
                // prepare player data
                l_players.append(l_player.getD_playerName() + " " + l_player.getD_noOfArmies() + " " + l_player.getD_stragey() + System.lineSeparator());

                // prepare owned countries data
                if (!l_player.getD_ownedCountries().isEmpty()) {
                    l_ownedCountries.append(l_player.getD_playerName());
                    for (Country l_country : l_player.getD_ownedCountries()) {
                        l_ownedCountries.append(" " + l_country.getD_countryName());
                    }
                    l_ownedCountries.append(System.lineSeparator());
                }

                // prepare orders data
                if (!l_player.getD_orders().isEmpty()) {
                    l_orders.append(l_player.getD_playerName());
                    for (Order l_order : l_player.getD_orders()) {
                        l_orders.append(" " + l_order.toString());
                    }
                    l_orders.append(System.lineSeparator());
                }

                // prepare cards data
                if (!l_player.getD_cards().isEmpty()) {
                    l_cards.append(l_player.getD_playerName());
                    for (GameCard l_card : l_player.getD_cards()) {
                        l_cards.append(" " + l_card.toString());
                    }
                    l_cards.append(System.lineSeparator());
                }

                // prepare negotiate player data
                if (l_player.getD_negotiatePlayerList() != null && !l_player.getD_negotiatePlayerList().isEmpty()) {
                    l_negotiatePlayers.append(l_player.getD_playerName());
                    for (Player l_negotiatePlayer : l_player.getD_negotiatePlayerList()) {
                        l_negotiatePlayers.append(" " + l_negotiatePlayer.toString());
                    }
                    l_negotiatePlayers.append(System.lineSeparator());
                }
            }
        }

        // prepare player flag
        if (d_playerFlag != null && d_playerFlag.length > 0) {
            for (int i = 0; i < d_playerFlag.length; i++) {
                l_playerFlag.append(d_playerFlag[i] + " ");
            }
            l_playerFlag.append(System.lineSeparator());
        }
        l_gamePhase.append(p_gameData.getD_gamePhase());
        l_gamePhase.append(System.lineSeparator());

        // prepare result response
        l_stringBuilderList.add(l_players);
        l_stringBuilderList.add(l_ownedCountries);
        l_stringBuilderList.add(l_orders);
        l_stringBuilderList.add(l_cards);
        l_stringBuilderList.add(l_negotiatePlayers);
        l_stringBuilderList.add(l_playerFlag);
        l_stringBuilderList.add(l_gamePhase);

        return l_stringBuilderList;
    }

    /**
     * This method will return index from name
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
                            if (l_country.getD_countryName().equalsIgnoreCase(p_countryName)) {
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
}
