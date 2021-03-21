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
    public  int[] d_playerFlag;

    /**
     * counter that invokes after each order issued by player
     */
    public  int d_playCounter = 0;

    @FXML
    public TextArea d_TerritoryListText;
    @FXML
    public TextArea d_ContinentText;
    @FXML
    public TextArea d_TerritoryPlayerArmyText;
    @FXML
    private Button d_BtnExit;

    @Lazy
    @Autowired
    private StageManager d_stageManager;

    @Autowired
    private GameData d_gameData;

    @Autowired
    public GeneralUtil d_generalUtil;

    @FXML
    private TextField d_CommandLine;

    @FXML
    private TextArea d_FireCommandList;

    @FXML
    private Button d_FireCommand;

    @FXML
    private Label d_playerTurn;

    @Autowired
    public GameEngineService d_gameEngineSevice;

    @Autowired
    private GameConfigService d_gameConfig;

    @Autowired
    public OrderProcessor d_orderProcessor;

    @Autowired
    public Player d_player;

    private LogEntryBuffer d_logEntryBuffer = new LogEntryBuffer();
    private WriteLogFile d_writeLogFile = new WriteLogFile(d_logEntryBuffer);

    /**
     * This method will exit the game and close the stage
     *
     * @param p_event will represents value sent from view
     */
    @FXML
    public void exitGame(ActionEvent p_event) {
        Stage l_stage = (Stage) d_BtnExit.getScene().getWindow();
        l_stage.close();
    }

    public void setPhase(Phase p_phase) {
        gamePhase = p_phase;
        System.out.println("new phase: " + p_phase.getClass().getSimpleName());
        d_logEntryBuffer.setLogEntryBuffer("-------------" + p_phase.getClass().getSimpleName() + "-------------");
    }

    public Phase getPhase() {
        if (gamePhase != null) {
            return gamePhase;
        }
        return null;

    }

    public StageManager getStageManager() {
        return d_stageManager;
    }

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
        if ((d_generalUtil.validateIOString(l_commandString, "(advance|airlift)\\s+[a-zA-Z]+\\s+[a-zA-Z]+\\s+[1-9][0-9]*") && l_validatestr.length == 4)||(d_generalUtil.validateIOString(l_commandString, "(bomb|blockade|negotiate)\\s+[a-zA-Z]+") && l_validatestr.length == 2) || (d_generalUtil.validateIOString(l_commandString, "deploy\\s+[a-zA-Z]+\\s+[1-9][0-9]*") && l_validatestr.length == 3) || l_commandString.equalsIgnoreCase("done")) { //validating that user input should be in "deploy string int"
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
                    }
                    d_FireCommandList.appendText(d_gameEngineSevice.playerOwnedCountries(d_gameData));
                    CommandResponse l_map = d_gameConfig.showPlayerMap(d_gameData);           //to show the map and player*country table
                    d_FireCommandList.appendText(l_map.getD_responseString());

                    d_playCounter = 0;
                    d_gameData.setD_maxNumberOfTurns(0);
                    Arrays.fill(d_playerFlag, 0);                                   //flag that resets the issue counter
                    l_issueorder.d_gameData = d_gameData;
                    l_issueorder.assignReinforcements();                    //for reinforcement
                    d_gameData = l_issueorder.d_gameData;
                    //d_gameData = d_gameEngineSevice.assignReinforcements(d_gameData);          // to reinforce the armies every time the loop resets
                    d_FireCommandList.appendText("\n" + d_gameEngineSevice.showReinforcementArmies(d_gameData));
                    d_playerTurn.setText(d_gameData.getD_playerList().get(d_playCounter).getD_playerName() + "'s turn");
                    d_playerTurn.setFont(Font.font(Font.getFontNames().get(0)));
                    d_playerTurn.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
                    d_CommandLine.clear();
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
            l_alert.setContentText("Invalid Command!!!");
            l_alert.showAndWait();
        }
    }

    /**
     * This is used for setting GameConfig for GameEngine
     *
     */
    public void setGamePlay() {
        GamePlay l_gamePlay = (GamePlay) gamePhase;
        d_gameData = l_gamePlay.d_gameData;
        d_playerTurn.setText(d_gameData.getD_playerList().get(d_playCounter).getD_playerName() + "'s turn");
        d_playerTurn.setFont(Font.font(Font.getFontNames().get(0)));
        d_playerTurn.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
        d_playerFlag = new int[d_gameData.getD_playerList().size()];
        Arrays.fill(d_playerFlag, 0);
        d_FireCommandList.appendText(d_gameEngineSevice.playerOwnedCountries(d_gameData));
        IssueOrderPhase l_issueorder = (IssueOrderPhase) gamePhase;
        l_issueorder.d_gameData = d_gameData;
        l_issueorder.assignReinforcements();                    //for reinforcement
        d_gameData = l_issueorder.d_gameData;
        d_FireCommandList.appendText(d_gameEngineSevice.showReinforcementArmies(d_gameData));
        //reinforcementArmies();
    }

    /**
     * This is used for Main Game loop Which includes AssignREinforcement
     */
    /*private void reinforcementArmies() {
        d_gameData = d_gameEngineSevice.assignReinforcements(d_gameData);


    }*/

    /**
     * This method is used for executing issued Order
     *
     * @return The liast of Command Response of Executed Order
     */
//    private List<CommandResponse> executionOfOrders() {
//        List<CommandResponse> l_orderStatus = new ArrayList<>();
//        for (int l_i = 0; l_i < CounterRound; l_i++) {                       //main loop for giving the turn to player in round-robin
//            for (int l_j = 0; l_j < d_gameData.getD_playerList().size(); l_j++) {
//                if (d_gameData.getD_playerList().get(l_j).hasOrder()) {             //checks if the player has an order or not
//                    Order l_order = d_gameData.getD_playerList().get(l_j).next_order();
//                    String l_countryName = ((DeployOrder) l_order).getD_CountryName();
//                    ((DeployOrder) l_order).setD_player(d_gameData.getD_playerList().get(l_j));         //to add the player to use in execution
//                    boolean l_executeOrder = l_order.executeOrder();                           //invokes the order
//                    if (l_executeOrder) {
//                        l_orderStatus.add(new CommandResponse(l_executeOrder, "" + d_gameData.getD_playerList().get(l_j).getD_playerName() + "'s command executed sucessfully\n"));
//                        d_gameData.getD_playerList().remove(l_j);                                          //replaces the player with the updated player from order
//                        d_gameData.getD_playerList().add(l_j, ((DeployOrder) l_order).getD_player());
//
//                        int l_noOfArmies = ((DeployOrder) l_order).getD_noOfArmies();
//                        if (d_gameData.getD_warMap().getD_continents() != null) {
//                            for (Map.Entry<Integer, Continent> l_entry : d_gameData.getD_warMap().getD_continents().entrySet()) {
//                                for (Country l_countries : l_entry.getValue().getD_countryList()) {
//                                    if (l_countries.getD_countryName().equalsIgnoreCase(l_countryName)) {
//                                        l_countries.setD_noOfArmies(l_noOfArmies);                              //sets the no. of armies to the country of map
//                                    }
//                                }
//                            }
//                        }
//
//                    } else {                                                              //return false ,if the deployment is failed
//                        l_orderStatus.add(new CommandResponse(l_executeOrder, d_gameData.getD_playerList().get(l_j).getD_playerName() + " either country is incorrect or not enough armies\n"));
//                    }
//                }
//            }
//        }
//        return l_orderStatus;
//    }

   /* public CommandResponse issuingPlayer(String p_command) {
        Player l_player = d_gameData.getD_playerList().get(PlayCounter);              //assigns the current player using the playcounter
        if (d_gameData.getD_maxNumberOfTurns() < l_player.getD_orders().size()) {                         //update the roundcounter if the one round completes
            d_gameData.setD_maxNumberOfTurns(l_player.getD_orders().size());
        }
        if (p_command.equalsIgnoreCase("done")) {                        //stops the player to get further chance to issue an order
            PlayerFlag[PlayCounter] = 1;
            String l_response = l_player.getD_playerName() + " : done with issuing orders";
            d_generalUtil.prepareResponse(true, l_response);
            return d_generalUtil.getResponse();
        } else {                                                   //issue and order to that player
            String[] l_commands = p_command.split("\\s+");
            if (l_player.getD_orders() == null) {
                List<Order> l_order = new ArrayList<Order>();
                d_gameData.getD_playerList().get(PlayCounter).setD_currentToCountry(l_commands[1]);
                d_gameData.getD_playerList().get(PlayCounter).setD_currentNoOfArmiesToMove(Integer.parseInt(l_commands[2]));
                d_gameData.getD_playerList().get(PlayCounter).setD_orders(l_order);
                d_gameData.getD_playerList().get(PlayCounter).issue_order();
            } else {
                d_gameData.getD_playerList().get(PlayCounter).setD_currentToCountry(l_commands[1]);
                d_gameData.getD_playerList().get(PlayCounter).setD_currentNoOfArmiesToMove(Integer.parseInt(l_commands[2]));
                d_gameData.getD_playerList().get(PlayCounter).issue_order();

            }

            d_generalUtil.prepareResponse(true, d_gameData.getD_maxNumberOfTurns() + " | " + p_command + " | " + l_player.getD_playerName());
            return d_generalUtil.getResponse();
        }

    }
    */



}
