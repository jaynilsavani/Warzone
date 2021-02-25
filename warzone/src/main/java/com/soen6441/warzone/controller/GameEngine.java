package com.soen6441.warzone.controller;

import com.soen6441.warzone.model.*;
import com.soen6441.warzone.service.GameConfigService;
import com.soen6441.warzone.service.GameEngineService;
import com.soen6441.warzone.service.GeneralUtil;
import java.io.IOException;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Class is made to handle Game Engine controller request
 *
 * @author <a href="mailto:patelvicky1995@gmail.com">Vicky Patel</a>
 */
@Controller
public class GameEngine implements Initializable {

    /**
     * array of falg that shows that whether player is done with issuing order
     * or not for particular round
     */
    private static int[] PlayerFlag;

    /**
     * counter that invokes after each order issued by player
     */
    private static int PlayCounter = 0;

    /**
     * counter that records the number of rounds of issue order
     */
    private static int CounterRound = 0;

    @FXML
    public TextArea d_TerritoryListText;
    @FXML
    public TextArea d_ContinentText;
    @FXML
    public TextArea d_TerritoryPlayerArmyText;
    @FXML
    Button d_BtnExit;
    @Autowired
    GamePlay d_gamePlay;
    @Autowired
    GeneralUtil d_generalUtil;
    @FXML
    private TextField d_CommandLine;
    @FXML
    private TextArea d_FireCommandList;
    @FXML
    private Button d_FireCommand;
    @FXML
    private Label d_playerTurn;
    @Autowired
    private GameEngineService d_gameEngineSevice;
    @Autowired
    private GameConfigService d_gameConfig;

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
        String l_s = d_CommandLine.getText().trim();
        String[] l_validatestr = l_s.split("\\s");
        if ((d_generalUtil.validateIOString(l_s, "deploy\\s+[a-zA-Z]+\\s+[1-9][0-9]*") && l_validatestr.length == 3) || l_s.equalsIgnoreCase("done")) { //validating that user input should be in "deploy string int"
            d_CommandLine.clear();
            CommandResponse l_commandResponse = issuingPlayer(l_s);                   //to invoke the issue order after player gives the command
            d_FireCommandList.appendText(l_commandResponse.getD_responseString() + "\n");
            while (true) {                                                       //loop to check for which player gets a turn
                int l_j = 0;
                for (int l_i = 0; l_i < PlayerFlag.length; l_i++) {
                    if (PlayerFlag[l_i] == 1) {
                        l_j++;
                    }
                }
                if (l_j == PlayerFlag.length) {                                //to reset the round after each player is done with issuing orders
                    List<CommandResponse> l_commandList = executionOfOrders();
                    for (int l_i = 0; l_i < l_commandList.size(); l_i++) {        //to add the result of each command that was issued to textarea
                        d_FireCommandList.appendText(l_commandList.get(l_i).getD_responseString());
                    }
                    d_FireCommandList.appendText(playerOwnedCountries(d_gamePlay));
                    CommandResponse l_map = d_gameConfig.showPlayerMap(d_gamePlay);           //to show the map and player*country table
                    d_FireCommandList.appendText(l_map.getD_responseString());

                    PlayCounter = 0;
                    CounterRound = 0;
                    Arrays.fill(PlayerFlag, 0);                                   //flag that resets the issue counter
                    d_gamePlay = d_gameEngineSevice.assignReinforcements(d_gamePlay);          // to reinforce the armies every time the loop resets
                    d_FireCommandList.appendText("\n" + d_gameEngineSevice.showReinforcementArmies(d_gamePlay));
                    d_playerTurn.setText(d_gamePlay.getD_playerList().get(PlayCounter).getD_playerName() + "'s turn");
                    d_playerTurn.setFont(Font.font(Font.getFontNames().get(0)));
                    d_playerTurn.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
                    d_CommandLine.clear();
                    break;
                }

                PlayCounter++;
                if (PlayCounter == PlayerFlag.length) {                                   //to reset the counter if it matches the number of player
                    PlayCounter = 0;
                }

                if (PlayerFlag[PlayCounter] == 1) {                                            //it checks that plalyer is done with issues and continue loop
                    continue;
                } else if (PlayerFlag[PlayCounter] == 0) {                                       //break the loop if finds the next player available to issue an order
                    d_playerTurn.setText(d_gamePlay.getD_playerList().get(PlayCounter).getD_playerName() + "'s turn");
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
     * @param p_gameConfig game configuration
     */
    public void setGamePlay(GamePlay p_gameConfig) {
        d_gamePlay = p_gameConfig;
        d_playerTurn.setText(d_gamePlay.getD_playerList().get(PlayCounter).getD_playerName() + "'s turn");
        d_playerTurn.setFont(Font.font(Font.getFontNames().get(0)));
        d_playerTurn.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
        PlayerFlag = new int[d_gamePlay.getD_playerList().size()];
        Arrays.fill(PlayerFlag, 0);
        d_FireCommandList.appendText(playerOwnedCountries(d_gamePlay));
        reinforcementArmies();
    }

    /**
     * This is used for Main Game loop Which includes AssignREinforcement
     */
    private void reinforcementArmies() {
        d_gamePlay = d_gameEngineSevice.assignReinforcements(d_gamePlay);
        d_FireCommandList.appendText(d_gameEngineSevice.showReinforcementArmies(d_gamePlay));

    }

    /**
     * This method is used for executing issued Order
     *
     * @return The liast of Command Response of Executed Order
     */
    private List<CommandResponse> executionOfOrders() {
        List<CommandResponse> l_orderStatus = new ArrayList<>();
        for (int l_i = 0; l_i < CounterRound; l_i++) {                       //main loop for giving the turn to player in round-robin
            for (int l_j = 0; l_j < d_gamePlay.getD_playerList().size(); l_j++) {
                if (d_gamePlay.getD_playerList().get(l_j).hasOrder()) {             //checks if the player has an order or not
                    Order l_order = d_gamePlay.getD_playerList().get(l_j).next_order();
                    String l_countryName = ((DeployOrder) l_order).getD_CountryName();
                    ((DeployOrder) l_order).setD_player(d_gamePlay.getD_playerList().get(l_j));         //to add the player to use in execution
                    boolean l_executeOrder = l_order.executeOrder();                           //invokes the order
                    if (l_executeOrder) {
                        l_orderStatus.add(new CommandResponse(l_executeOrder, "" + d_gamePlay.getD_playerList().get(l_j).getD_playerName() + "'s command executed sucessfully\n"));
                        d_gamePlay.getD_playerList().remove(l_j);                                          //replaces the player with the updated player from order
                        d_gamePlay.getD_playerList().add(l_j, ((DeployOrder) l_order).getD_player());

                        int l_noOfArmies = ((DeployOrder) l_order).getD_noOfArmies();
                        if (d_gamePlay.getD_warMap().getD_continents() != null) {
                            for (Map.Entry<Integer, Continent> l_entry : d_gamePlay.getD_warMap().getD_continents().entrySet()) {
                                for (Country l_countries : l_entry.getValue().getD_countryList()) {
                                    if (l_countries.getD_countryName().equalsIgnoreCase(l_countryName)) {
                                        l_countries.setD_noOfArmies(l_noOfArmies);                              //sets the no. of armies to the country of map
                                    }
                                }
                            }
                        }

                    } else {                                                              //return false ,if the deployment is failed
                        l_orderStatus.add(new CommandResponse(l_executeOrder, d_gamePlay.getD_playerList().get(l_j).getD_playerName() + " either country is incorrect or not enough armies\n"));
                    }
                }
            }
        }
        return l_orderStatus;
    }

    /**
     * This method is used to store the user input of orders in player's lst of
     * orders
     *
     * @param p_command String that has been given by user
     * @return return the response in term of wether order is added or player is
     * completed with orders or not
     */
    public CommandResponse issuingPlayer(String p_command) {
        Player l_player = d_gamePlay.getD_playerList().get(PlayCounter);              //assigns the current player using the playcounter
        if (CounterRound < l_player.getD_orders().size()) {                         //update the roundcounter if the one round completes
            CounterRound = l_player.getD_orders().size();
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
                d_gamePlay.getD_playerList().get(PlayCounter).setD_currentToCountry(l_commands[1]);
                d_gamePlay.getD_playerList().get(PlayCounter).setD_currentNoOfArmiesToMove(Integer.parseInt(l_commands[2]));
                d_gamePlay.getD_playerList().get(PlayCounter).setD_orders(l_order);
                d_gamePlay.getD_playerList().get(PlayCounter).issue_order();
            } else {
                d_gamePlay.getD_playerList().get(PlayCounter).setD_currentToCountry(l_commands[1]);
                d_gamePlay.getD_playerList().get(PlayCounter).setD_currentNoOfArmiesToMove(Integer.parseInt(l_commands[2]));
                d_gamePlay.getD_playerList().get(PlayCounter).issue_order();

            }

            d_generalUtil.prepareResponse(true, CounterRound + " | " + p_command + " | " + l_player.getD_playerName());
            return d_generalUtil.getResponse();
        }

    }

    public String playerOwnedCountries(GamePlay p_gamePlay) {
        String l_responseString = "\n";
        for (Player l_player : p_gamePlay.getD_playerList()) {
            l_responseString += l_player.getD_playerName() + " : [";
            for (Country l_cn : l_player.getD_ownedCountries()) {
                l_responseString += l_cn.getD_countryName() + " , ";
            }
            l_responseString += " ] \n ";
        }
        return l_responseString;
    }

}
