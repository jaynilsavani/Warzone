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
import com.soen6441.warzone.state.GamePlay;
import com.soen6441.warzone.state.IssueOrderPhase;
import com.soen6441.warzone.state.MapPhase;
import com.soen6441.warzone.state.Phase;
import com.soen6441.warzone.state.StartUpPhase;
import com.soen6441.warzone.strategy.HumanStrategy;
import com.soen6441.warzone.strategy.Strategy;
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

import java.io.*;
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

    /**
     * phase of the game
     */
    public Phase gamePhase;
    /**
     * array of flag that shows that whether player is done with issuing order
     * or not for particular round
     */
    public int[] d_playerFlag;

    /**
     * counter that invokes after each order issued by player
     */
    public int d_playCounter = 0;
    public boolean l_winner = false;
    public boolean d_isLoadedGame = false;

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
    public TextField d_CommandLine;
    @FXML
    private TextArea d_FireCommandList;
    @FXML
    public Button d_FireCommand;
    @FXML
    public Label d_playerTurn;
    @FXML
    private TextArea d_countriesList;
    @FXML
    private TextArea d_neighboursList;
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
        playerIteration(l_commandString, false);
    }

    /**
     * This is used for setting GameConfig for GameEngine
     */
    public void setGamePlay() {
        GamePlay l_gamePlay = (GamePlay) gamePhase;
        d_gameData = l_gamePlay.d_gameData;
        showMapContents(l_gamePlay);
        int l_i=0;
        while(l_i<l_gamePlay.d_gameData.getD_playerList().size())
        {
            if(l_gamePlay.d_gameData.getD_playerList().get(l_i).getD_stragey() instanceof HumanStrategy)
            {
                d_playerTurn.setText(d_gameData.getD_playerList().get(l_i).getD_playerName() + "'s turn");  //shows whose turn now is
                break;
            }
            l_i++;
        }
        d_playerTurn.setText(d_gameData.getD_playerList().get(d_playCounter).getD_playerName() + "'s turn");  //shows whose turn now is
        d_playerTurn.setFont(Font.font(Font.getFontNames().get(0)));
        d_playerTurn.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
        if (!d_isLoadedGame) {
            d_playerFlag = new int[d_gameData.getD_playerList().size()];
            Arrays.fill(d_playerFlag, 0);       //flag that resets the issue order
        }
        IssueOrderPhase l_issueorder = (IssueOrderPhase) gamePhase;     //take issue order form user
        l_issueorder.d_gameData = d_gameData;
        l_issueorder.assignReinforcements();                    //for reinforcement
        d_gameData = l_issueorder.d_gameData;
        d_FireCommandList.appendText(d_gameConfig.showPlayerMap(d_gameData).getD_responseString());           //to show the map and player*country table
        d_FireCommandList.appendText(d_gameEngineSevice.showReinforcementArmies(d_gameData));
        d_FireCommandList.appendText(d_gameEngineSevice.playerOwnedCountries(d_gameData)+"\n");
        d_FireCommandList.appendText("------------ORDERS------------\n");
        // print order list on click of start game
        if (d_gameData.getD_playerList().get(0).getD_orders().size() != 0) {
            int l_maxCounter = getMaxNumberOfOrders(d_gameData);
            for (int l_j = 0; l_j < l_maxCounter; l_j++) {
                for (Player l_playerList : d_gameData.getD_playerList()) {
                    if (l_playerList.getD_orders().size() > l_j)
                        d_FireCommandList.appendText(l_j + " | " +prepareOrderString(l_playerList.getD_orders().get(l_j).toString()) + " | " + l_playerList.getD_playerName() +"\n");
                }
            }
        }
        if(!(l_gamePlay.d_gameData.getD_playerList().get(0).getD_stragey() instanceof HumanStrategy))
        {
            playerIteration("", true);
        }
    }

    /**
     * This method will return maximum number of order count
     *
     * @param p_gameData game data object
     * @return return max number of orders
     */
    public int getMaxNumberOfOrders(GameData p_gameData) {
        int l_count = 0;
        for (Player l_player : p_gameData.getD_playerList()) {
            if (l_count < l_player.getD_orders().size())
                l_count = l_player.getD_orders().size();
        }

        return l_count;
    }

    /**
     * used to add the data of warmap to ui
     *
     * @param p_gameplay object to fetch the data of warmap from game engine
     */
    public void showMapContents(GamePlay p_gameplay) {
        String l_seperator = " ==> ";
        String l_coutriesList = "", l_neighbourList = "", l_continents = "";
        for (Map.Entry<Integer, Continent> l_entry : p_gameplay.d_gameData.getD_warMap().getD_continents().entrySet()) {
            l_continents = l_continents + l_entry.getValue().getD_continentName() + l_seperator;
            for (Country l_country : l_entry.getValue().getD_countryList()) {
                l_continents = l_continents + l_country.getD_countryName() + " , ";
                l_coutriesList = l_coutriesList + l_country.getD_countryName() + " (" + l_entry.getValue().getD_continentName() + ")\n";
                l_neighbourList = l_neighbourList + l_country.getD_countryName() + l_seperator;
                for (String l_neighnoours : l_country.getD_neighbourCountries()) {
                    l_neighbourList = l_neighbourList + l_neighnoours + " , ";
                }
                l_neighbourList = l_neighbourList + "\n";
            }
            l_continents = l_continents + "\n";
        }
        d_countriesList.appendText(l_coutriesList);
        d_neighboursList.appendText(l_neighbourList);
        d_continentToCountry.appendText(l_continents);

    }

    /**
     * This method is used to iterate the players for their turn
     *
     * @param p_commandString command response string
     * @param p_isNotHumanPlayer to check whether the player is Human or not
     */
    private void playerIteration(String p_commandString, boolean p_isNotHumanPlayer) {
        String[] l_validatestr = p_commandString.split("\\s");
        if (d_generalUtil.validateIOString(p_commandString, "savegame\\s+[a-zA-Z]+.?[a-zA-Z]+") && l_validatestr.length == 2) {
            saveGame(d_gameData, l_validatestr[1]);
            Alert l_alert = new Alert(AlertType.INFORMATION);
            l_alert.setTitle("Success!!");
            l_alert.setHeaderText(null);
            l_alert.setContentText("Game saved successfully.");   //message shown in of alert
            l_alert.showAndWait();
        }
        else if (p_isNotHumanPlayer || ((d_generalUtil.validateIOString(p_commandString, "(advance|airlift)\\s+[a-zA-Z-_]+\\s+[a-zA-Z-_]+\\s+[1-9][0-9]*") && l_validatestr.length == 4) || (d_generalUtil.validateIOString(p_commandString, "(bomb|blockade|negotiate)\\s+[a-zA-Z-_]+") && l_validatestr.length == 2) || (d_generalUtil.validateIOString(p_commandString, "deploy\\s+[a-zA-Z-_]+\\s+[1-9][0-9]*") && l_validatestr.length == 3) || p_commandString.equalsIgnoreCase("done"))) {
            //validating that user input should be in "deploy string int"
            d_CommandLine.clear();
            IssueOrderPhase l_issueorder = (IssueOrderPhase) gamePhase;
            l_issueorder.d_gameData = d_gameData;
            l_issueorder.issueOrder(p_commandString);
            //to invoke the issue order after player gives the command
            CommandResponse l_commandResponse = l_issueorder.d_issueResponse;
            d_gameData = l_issueorder.d_gameData;
            d_FireCommandList.appendText(l_commandResponse.getD_responseString() + "\n");
            while (true) {
                //loop to check for which player gets a turn
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
                        //To check the winner
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
                        d_FireCommandList.appendText(d_gameEngineSevice.playerOwnedCountries(d_gameData)+"\n");
                        d_FireCommandList.appendText("------------ORDERS------------\n");
                        int l_ind=0;
                        while(l_ind<d_gameData.getD_playerList().size())
                        {
                            if(d_gameData.getD_playerList().get(l_ind).getD_stragey() instanceof HumanStrategy)
                            {
                                d_playerTurn.setText(d_gameData.getD_playerList().get(l_ind).getD_playerName() + "'s turn");  //shows whose turn now is
                                d_playerTurn.setFont(Font.font(Font.getFontNames().get(0)));
                                d_playerTurn.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
                                break;
                            }
                            l_ind++;
                        }
                        if(!(d_gameData.getD_playerList().get(d_playCounter).getD_stragey() instanceof HumanStrategy))
                        {
                            playerIteration("",true);
                        }
                        if(!l_winner) {
                            d_CommandLine.clear();
                        }

                    }
                    break;
                }

                d_playCounter++;

                if (d_playCounter == d_playerFlag.length) {                                   //to reset the counter if it matches the number of player
                    d_playCounter = 0;
                }

                if (d_playerFlag[d_playCounter] == 1) {                                            //it checks that plalyer is done with issues and continue loop
                    continue;
                } else if (d_playerFlag[d_playCounter] == 0) {
                    //break the loop if finds the next player available to issue an order
                    if (!l_winner) {
                        if (d_gameData.getD_playerList().get(d_playCounter).getD_stragey() instanceof HumanStrategy) {

                            d_playerTurn.setText(d_gameData.getD_playerList().get(d_playCounter).getD_playerName() + "'s turn");
                            d_playerTurn.setFont(Font.font(Font.getFontNames().get(0)));
                            d_playerTurn.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
                            d_CommandLine.clear();

                        } else {
                            playerIteration("", true);
                        }
                    }
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
     * This method is used to save game data in file
     *
     * @param p_gameData current game data
     * @param p_fileName filename
     */
    public boolean saveGame(GameData p_gameData, String p_fileName) {
        // for managing the .txt extension of the file
        if (p_fileName.contains(".")) {
            String l_fileNameSplit = p_fileName.split("\\.")[1];
            if (!l_fileNameSplit.equals("txt")) {
                p_fileName = p_fileName.split("\\.")[0].toLowerCase().concat(".txt");
            }
        } else {
            p_fileName = p_fileName.toLowerCase().concat(".txt");
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
                writer.println(MAP_NAME);
                writer.println(p_gameData.getD_warMap().getD_mapName());
                writer.println();
                for (StringBuilder l_stringBuilder : l_stringBuilderList) {
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
     *
     * @param p_gameData current game data object
     * @return list of player, countries owned by players and orders issued by player
     */
    public List<StringBuilder> writePlayerData(GameData p_gameData) {
        List<StringBuilder> l_stringBuilderList = new ArrayList<>();
        StringBuilder l_players = new StringBuilder(PLAYERS).append(System.lineSeparator());
        StringBuilder l_ownedCountries = new StringBuilder(OWNED_COUNTRIES).append(System.lineSeparator());
        StringBuilder l_orders = new StringBuilder(ORDERS).append(System.lineSeparator());
        StringBuilder l_cards = new StringBuilder(CARDS).append(System.lineSeparator());
        StringBuilder l_playerFlag = new StringBuilder(PLAYER_FLAG).append(System.lineSeparator());
        StringBuilder l_playerCounter = new StringBuilder(PLAYER_COUNTER).append(System.lineSeparator());

        if (p_gameData.getD_playerList() != null) {
            for (Player l_player : p_gameData.getD_playerList()) {
                String[] l_strategy = l_player.getD_stragey().getClass().getName().split("\\.");
                // prepare player data
                l_players.append(l_player.getD_playerName() + " " + l_player.getD_noOfArmies() + " " + l_strategy[l_strategy.length - 1] + " " + System.lineSeparator());

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
                        l_orders.append(" " + l_order.toString().replace(" ", ""));
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
            }
        }

        // prepare player flag
        if (d_playerFlag != null && d_playerFlag.length > 0) {
            for (int l_i = 0; l_i < d_playerFlag.length; l_i++) {
                l_playerFlag.append(d_playerFlag[l_i] + " ");
            }
            l_playerFlag.append(System.lineSeparator());
        }

        l_playerCounter.append(d_playCounter);

        // prepare result response
        l_stringBuilderList.add(l_players);
        l_stringBuilderList.add(l_ownedCountries);
        l_stringBuilderList.add(l_orders);
        l_stringBuilderList.add(l_cards);
        l_stringBuilderList.add(l_playerFlag);
        l_stringBuilderList.add(l_playerCounter);

        return l_stringBuilderList;
    }

    /**
     * This method is used to load game from saved file
     *
     * @param p_fileName file name
     * @return gamedata object
     */
    public GameData loadGame(String p_fileName) throws Exception {
        String l_fileLine = "";
        boolean l_mapName, l_isContinents, l_isCountries, l_isBorders, l_isPlayers, l_isOwnedCountries, l_isOrders, l_isCards, l_isPlayerFlag, l_isPlayerCounter;
        l_mapName = l_isContinents = l_isCountries = l_isBorders = l_isPlayers = l_isOwnedCountries = l_isOrders = l_isCards = l_isPlayerFlag = l_isPlayerCounter = false;
        WarMap l_warMap = new WarMap();
        List<Player> l_players = new ArrayList<>();
        GameData l_gameData = new GameData();

        try (BufferedReader l_bufferedReader = new BufferedReader(new FileReader(GAME_DEF_PATH + p_fileName))) {

            Map<Integer, Continent> l_continentMap = new HashMap();
            Continent l_continent = null;
            Country l_country = null;
            int l_continentCounter = 1;

            //while loop read each line from file and process accordingly
            while ((l_fileLine = l_bufferedReader.readLine()) != null) {
                if (l_fileLine != null && !l_fileLine.isEmpty()) {
                    if (l_fileLine.equalsIgnoreCase(MAP_NAME)) {
                        l_mapName = true;
                        continue;
                    }
                    if (l_mapName && !l_fileLine.equalsIgnoreCase(CONTINENTS)) {
                        l_warMap.setD_mapName(l_fileLine);
                    }

                    l_warMap.setD_status(true);

                    if (l_fileLine.equalsIgnoreCase(CONTINENTS)) {
                        l_mapName = false;
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
                        Continent l_currentContinent = l_continentMap.get(l_continentIndex);

                        l_country = new Country();
                        l_country.setD_countryName(l_countries[1]);
                        l_country.setD_countryIndex(Integer.parseInt(l_countries[0]));
                        l_country.setD_continentIndex(l_continentIndex);
                        l_country.setD_noOfArmies(Integer.parseInt(l_countries[3]));
                        if (l_currentContinent.getD_countryList() == null) {
                            List<Country> l_countryList = new ArrayList();
                            l_countryList.add(l_country);
                            l_currentContinent.setD_countryList(l_countryList);
                        } else {
                            l_currentContinent.getD_countryList().add(l_country);
                        }
                        l_continentMap.put(l_continentIndex, l_currentContinent);
                    }

                    if (l_fileLine.equalsIgnoreCase(BORDERS)) {
                        l_isCountries = false;
                        l_isBorders = true;
                        continue;
                    }

                    //this if condition read neighbors of each country and set into neighbour list of country model
                    if (l_isBorders && !l_fileLine.equalsIgnoreCase(PLAYERS)) {
                        String[] l_neighbourArray = l_fileLine.split(" ");

                        Continent l_currentContinent = getContinentByCountryId(l_continentMap, Integer.parseInt(l_neighbourArray[0]));

                        List<String> l_neighbourName = new ArrayList<>();
                        for (int l_i = 1; l_i < l_neighbourArray.length; l_i++) {
                            l_neighbourName
                                    .add(getCountryNameByCountryId(l_continentMap, Integer.parseInt(l_neighbourArray[l_i])));
                        }

                        for (int l_i = 0; l_i < l_currentContinent.getD_countryList().size(); l_i++) {
                            Country l_currentCountry = l_currentContinent.getD_countryList().get(l_i);
                            if (l_currentCountry.getD_countryIndex() == Integer.parseInt(l_neighbourArray[0])) {
                                l_currentCountry.setD_neighbourCountries(l_neighbourName);
                                l_currentContinent.getD_countryList().set(l_i, l_currentCountry);
                            }
                        }
                        l_continentMap.put(l_currentContinent.getD_continentIndex(), l_currentContinent);
                    }

                    // prepare player list data
                    if (l_fileLine.equalsIgnoreCase(PLAYERS)) {
                        l_isPlayers = true;
                        l_isBorders = false;
                        continue;
                    }
                    if (l_isPlayers && !l_fileLine.equalsIgnoreCase(OWNED_COUNTRIES)) {
                        String[] l_playerArray = l_fileLine.split(" ");
                        Player l_newPlayer = new Player();
                        l_newPlayer.setD_playerName(l_playerArray[0]);
                        l_newPlayer.setD_noOfArmies(Integer.parseInt(l_playerArray[1]));
                        l_players.add(l_newPlayer);
                    }

                    // prepare owned countries list
                    if (l_fileLine.equalsIgnoreCase(OWNED_COUNTRIES)) {
                        l_isOwnedCountries = true;
                        l_isPlayers = false;
                        continue;
                    }
                    if (l_isOwnedCountries && !l_fileLine.equalsIgnoreCase(ORDERS)) {
                        String[] l_playerArray = l_fileLine.split(" ");
                        List<Country> l_ownedCountryList = new ArrayList<>();
                        List<Integer> l_playerIndex = new ArrayList<>();
                        for (int l_i = 0; l_i < l_players.size(); l_i++) {
                            if (l_players.get(l_i).getD_playerName().equalsIgnoreCase(l_playerArray[0])) {
                                for (int l_j = 1; l_j < l_playerArray.length; l_j++) {
                                    if (l_warMap != null) {
                                        Country l_newCountry = getCountryObjectByCountryName(l_continentMap, l_playerArray[l_j]);
                                        l_ownedCountryList.add(l_newCountry);
                                        l_playerIndex.add(l_i);
                                    }
                                }
                            }
                        }
                        if (l_playerIndex != null && !l_playerIndex.isEmpty()) {
                            l_players.get(l_playerIndex.get(0)).setD_ownedCountries(l_ownedCountryList);
                        }
                    }

                    // prepare orders list
                    if (l_fileLine.equalsIgnoreCase(ORDERS)) {
                        l_isOwnedCountries = false;
                        l_isOrders = true;
                        continue;
                    }

                    // prepare card data
                    if (l_fileLine.equalsIgnoreCase(CARDS)) {
                        l_isOrders = false;
                        l_isCards = true;
                        continue;
                    }
                    if (l_isCards && !l_fileLine.equalsIgnoreCase(PLAYER_FLAG)) {
                        String[] l_cardArray = l_fileLine.split(" ");
                        List<Integer> l_playerIndex = new ArrayList<>();
                        List<GameCard> l_cards = new ArrayList<>();
                        for (int l_i = 0; l_i < l_players.size(); l_i++) {
                            if (l_players.get(l_i).getD_playerName().equalsIgnoreCase(l_cardArray[0])) {
                                for (int l_j = 1; l_j < l_cardArray.length; l_j++) {
                                    l_cards.add(GameCard.valueOf(l_cardArray[l_j]));
                                    l_playerIndex.add(l_i);
                                }
                            }
                        }
                        if (l_playerIndex != null && !l_playerIndex.isEmpty()) {
                            l_players.get(l_playerIndex.get(0)).setD_cards(l_cards);
                        }
                    }

                    // prepare player flag data
                    if (l_fileLine.equalsIgnoreCase(PLAYER_FLAG)) {
                        l_isPlayerFlag = true;
                        l_isCards = false;
                        continue;
                    }
                    if (l_isPlayerFlag && !l_fileLine.equalsIgnoreCase(PLAYER_COUNTER)) {
                        d_playerFlag = new int[l_players.size()];
                        String[] l_playerFlagArray = l_fileLine.split(" ");
                        for (int l_i = 0; l_i < l_playerFlagArray.length; l_i++) {
                            d_playerFlag[l_i] = Integer.parseInt(l_playerFlagArray[l_i]);
                        }
                    }

                    // prepare player counter data
                    if (l_fileLine.equalsIgnoreCase(PLAYER_COUNTER)) {
                        l_isPlayerCounter = true;
                        l_isPlayerFlag = false;
                        continue;
                    }
                    if (l_isPlayerCounter) {
                        d_playCounter = Integer.parseInt(l_fileLine);
                    }
                }
            }
            l_warMap.setD_continents(l_continentMap);

            l_gameData.setD_warMap(l_warMap);
            if(l_players.size() != 0) {
                l_gameData.setD_playerList(l_players);
            }
        } catch (Exception e) {
            throw e;
        }

        // prepare order and strategy data
        try (BufferedReader l_bufferedReader = new BufferedReader(new FileReader(GAME_DEF_PATH + p_fileName))) {
            //while loop read each line from file and process accordingly
            while ((l_fileLine = l_bufferedReader.readLine()) != null) {
                // prepare order data
                if (l_fileLine != null && !l_fileLine.isEmpty()) {
                    if (l_fileLine.equalsIgnoreCase(ORDERS)) {
                        l_isOrders = true;
                        continue;
                    }
                    if (l_fileLine.equalsIgnoreCase(CARDS)) {
                        l_isOrders = false;
                        continue;
                    }
                    if (l_isOrders && !l_fileLine.equalsIgnoreCase(CARDS)) {
                        String[] l_playerArray = l_fileLine.split(" ");
                        List<Integer> l_playerIndex = new ArrayList<>();
                        List<Order> l_orders = new ArrayList<>();
                        for (int l_i = 0; l_i < l_gameData.getD_playerList().size(); l_i++) {
                            if (l_gameData.getD_playerList().get(l_i).getD_playerName().equalsIgnoreCase(l_playerArray[0])) {
                                for (int l_j = 1; l_j < l_playerArray.length; l_j++) {
                                    String l_orderString = prepareOrderString(l_playerArray[l_j]);
                                    if(!l_orderString.isEmpty()) {
                                        CommandResponse l_commandResponse = d_orderProcessor.processOrder(l_orderString, l_gameData);
                                        if (l_commandResponse.isD_isValid()) {
                                            Order l_order = d_orderProcessor.getOrder();
                                            l_order.setD_player(l_players.get(l_i));
                                            l_orders.add(l_order);
                                            l_playerIndex.add(l_i);
                                        }
                                    }
                                }
                            }
                            if (l_playerIndex != null && !l_playerIndex.isEmpty()) {
                                l_players.get(l_playerIndex.get(0)).setD_orders(l_orders);
                            }
                        }
                    }

                    // prepare strategy data
                    if (l_fileLine.equalsIgnoreCase(PLAYERS)) {
                        l_isOrders = false;
                        l_isPlayers = true;
                        continue;
                    }
                    if (l_fileLine.equalsIgnoreCase(OWNED_COUNTRIES)) {
                        l_isPlayers = false;
                        continue;
                    }
                    if (l_isPlayers && !l_fileLine.equalsIgnoreCase(OWNED_COUNTRIES)) {
                        String[] l_playerArray = l_fileLine.split(" ");
                        List<Integer> l_playerIndex = new ArrayList<>();
                        Strategy l_strategy = null;
                        for (int l_i = 0; l_i < l_gameData.getD_playerList().size(); l_i++) {
                            if (l_gameData.getD_playerList().get(l_i).getD_playerName().equalsIgnoreCase(l_playerArray[0])) {
                                String l_strategyString = l_playerArray[2].replace("Strategy","");
                                l_strategy = Strategies.strategyToObjectMapper(Strategies.stringToStrategyMapper(l_strategyString.toLowerCase()), l_gameData);
                                l_playerIndex.add(l_i);
                                break;
                            }
                        }
                        if (l_playerIndex != null && !l_playerIndex.isEmpty() && l_strategy != null) {
                            l_players.get(l_playerIndex.get(0)).setD_stragey(l_strategy);
                        }
                    }
                }
            }

            if(l_players.size() != 0) {
                l_gameData.setD_playerList(l_players);
            }
        } catch (Exception e) {
            throw e;
        }

        d_isLoadedGame = true;
        return l_gameData;
    }

    /**
     * This method prepare order string
     *
     * @param p_orderString : string of order
     * @return return order string
     */
    public String prepareOrderString(String p_orderString) {
        String[] l_order = p_orderString.split("\\(");
        l_order[1] = l_order[1].replaceAll("\\)", "");
        String[] l_fields = l_order[1].split(",");
        String l_orderString = "";
        switch (l_order[0]) {
            case "DeployOrder":
                l_orderString = "deploy";
                l_orderString += getOrderString(l_fields);
                break;
            case "AdvanceOrder":
                l_orderString = "advance";
                l_orderString += getOrderString(l_fields);
                break;
            case "AirliftOrder":
                l_orderString = "airlift";
                l_orderString += getOrderString(l_fields);
                break;
            case "NegotiateOrder":
                l_orderString = "negotiate";
                l_orderString += getOrderString(l_fields);
                break;
            case "BombOrder":
                l_orderString = "bomb";
                l_orderString += getOrderString(l_fields);
                break;
            case "BlockadeOrder":
                l_orderString = "blockade";
                l_orderString += getOrderString(l_fields);
                break;
        }
        return l_orderString;
    }

    /**
     * This method prepare arguments for order command
     *
     * @param p_fields : list of arguments
     * @return string of arguments
     */
    public String getOrderString(String[] p_fields) {
        String l_orderString = "";
        for (int l_j = 0; l_j < p_fields.length-1; l_j++) {
            String[] l_filedArray = p_fields[l_j].split("=");
            l_orderString += " " + l_filedArray[1];
        }
        return l_orderString;
    }

    /**
     * This method return Country Object
     *
     * @param p_countryName : name of the country
     * @param p_continent   : continent object
     * @return Country object
     */
    public Country getCountryObjectByCountryName(Map<Integer, Continent> p_continent, String p_countryName) {
        Country l_countryName = null;
        if (p_continent != null) {
            for (Map.Entry<Integer, Continent> l_entry : p_continent.entrySet()) {
                if (l_entry.getValue().getD_countryList() != null) {
                    for (Country l_country : l_entry.getValue().getD_countryList()) {
                        if (p_countryName.equalsIgnoreCase(l_country.getD_countryName())) {
                            l_countryName = l_country;
                            break;
                        }
                    }
                }
            }
        }

        return l_countryName;
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

    /**
     * This method will return Continent model from given countryId
     *
     * @param p_continentMap is map of continents
     * @param p_countryIndex is countryId for that you want to find continent
     * @return Continent model
     */
    private Continent getContinentByCountryId(Map<Integer, Continent> p_continentMap, int p_countryIndex) {
        Continent l_continent = null;
        for (Map.Entry<Integer, Continent> entry : p_continentMap.entrySet()) {

            l_continent = entry.getValue();
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
    private String getCountryNameByCountryId(Map<Integer, Continent> p_continentMap, int p_countryIndex) {

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
}
