package com.soen6441.warzone.controller;

import com.soen6441.warzone.model.*;
import com.soen6441.warzone.service.GameConfigService;
import com.soen6441.warzone.service.GameEngineService;
import com.soen6441.warzone.service.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.FontPosture;
import javafx.scene.control.Alert.AlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.*;

/**
 * This Class is made to handle Game Engine controller request
 *
 * @author <a href="mailto:patelvicky1995@gmail.com">Vicky Patel</a>
 */
@Controller
public class GameEngine implements Initializable {

    @FXML
    public TextArea d_TerritoryListText;
    @FXML
    public TextArea d_ContinentText;
    @FXML
    public TextArea d_TerritoryPlayerArmyText;
    @FXML
    Button d_BtnExit;
    @FXML
    private TextField d_CommandLine;

    @FXML
    private TextArea d_FireCommandList;

    @FXML
    private Button d_FireCommand;

    @FXML
    private Label d_playerTurn;

    @Autowired
    GamePlay d_gamePlay;

    @Autowired
    private GameEngineService d_gameEngineSevice;

    @Autowired
    private GameConfigService d_gameConfig;

    @Autowired
    GeneralUtil d_generalUtil;
    /**
     * used to add flag for all players whether they want to add issue or not
     */
    private static int[] PlayerFlag;

    /**
     * used to give turn to each player
     */
    private static int PlayCounter = 0;

    private static int CounterRound = 0;

    private static int PreviousCounterRound = 0;


    /**
     * This method will exit the game and close the stage
     *
     * @param event will represents value sent from view
     */
    @FXML
    public void exitGame(ActionEvent event) {

        Stage stage = (Stage) d_BtnExit.getScene().getWindow();
        stage.close();

    }

    /**
     * This is the initialization method of this controller
     *
     * @param location  of the FXML file
     * @param resources is properties information
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        d_FireCommandList.setStyle("-fx-font-family: monospace");
    }

    /**
     * This method is used to get fire command from user and put it as a
     * parameter in validation
     *
     * @param event
     */
    public void getData(ActionEvent event) {
        String l_s = d_CommandLine.getText();
        String[] l_validatestr = l_s.split("\\s");
        if ((d_generalUtil.validateIOString(l_s, "deploy\\s[a-zA-Z]+\\s[0-9]+") && l_validatestr.length == 3) || l_s.equalsIgnoreCase("done")) {
            d_CommandLine.clear();
            CommandResponse l_commandResponse = issuingPlayer(l_s);
            d_FireCommandList.appendText(l_commandResponse.getD_responseString() + "\n");
            while (true) {
                int l_j = 0;
                for (int l_i = 0; l_i < PlayerFlag.length; l_i++) {
                    if (PlayerFlag[l_i] == 1) {
                        l_j++;
                    }
                }
                if (l_j == PlayerFlag.length) {
                    List<CommandResponse> l_commandList = executionOfOrders();
                    for (int l_i = 0; l_i < l_commandList.size(); l_i++) {
                        d_FireCommandList.appendText(l_commandList.get(l_i).getD_responseString());
                    }
                    CommandResponse l_map = d_gameConfig.showPlayerMap(d_gamePlay);
                    d_FireCommandList.appendText(l_map.getD_responseString());

                    PlayCounter = 0;
                    CounterRound = 0;
                    Arrays.fill(PlayerFlag, 0);
                    d_gamePlay = d_gameEngineSevice.assignReinforcements(d_gamePlay);
                    d_playerTurn.setText("Issue an order for " + d_gamePlay.getPlayerList().get(PlayCounter).getD_playerName());
                    d_playerTurn.setFont(Font.font(Font.getFontNames().get(0)));
                    d_playerTurn.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
                    d_CommandLine.clear();
                    break;
                }


                PlayCounter++;
                if (PlayCounter == PlayerFlag.length) {
                    PlayCounter = 0;
                }
                if (PlayerFlag[PlayCounter] == 1) {
                    continue;
                } else if (PlayerFlag[PlayCounter] == 0) {
                    d_playerTurn.setText("Issue an order for " + d_gamePlay.getPlayerList().get(PlayCounter).getD_playerName());
                    d_playerTurn.setFont(Font.font(Font.getFontNames().get(0)));
                    d_playerTurn.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
                    d_CommandLine.clear();
                    break;
                }
            }
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Invalid");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Command!!!");
            alert.showAndWait();
        }
    }

    /**
     * This is used for setting GameConfig for GameEngine
     *
     * @param p_gameConfig
     */
    public void setGamePlay(GamePlay p_gameConfig) {
        d_gamePlay = p_gameConfig;
        d_playerTurn.setText("Issue order for " + d_gamePlay.getPlayerList().get(0).getD_playerName());
        d_playerTurn.setFont(Font.font(Font.getFontNames().get(0)));
        d_playerTurn.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
        PlayerFlag = new int[d_gamePlay.getPlayerList().size()];
        Arrays.fill(PlayerFlag, 0);
        reinforcementArmies();
    }

    /**
     * This is used for Main Game loop Which includes
     * AssignREinforcement
     */
    private void reinforcementArmies() {
        d_gamePlay = d_gameEngineSevice.assignReinforcements(d_gamePlay);


    }

    /**
     * This method is used for executing issued Order
     *
     * @return The liast of Command Response of Executed Order
     */
    private List<CommandResponse> executionOfOrders() {
        List<CommandResponse> l_orderStatus = new ArrayList<>();
        for (int l_i = PreviousCounterRound; l_i < CounterRound; l_i++) {
            for (int l_j = 0; l_j < d_gamePlay.getPlayerList().size(); l_j++) {
                if (d_gamePlay.getPlayerList().get(l_j).hasOrder()) {
                    Order l_order = d_gamePlay.getPlayerList().get(l_j).next_order();
                    String l_countryName = ((DeployOrder) l_order).getD_CountryName();
                    ((DeployOrder) l_order).setD_player(d_gamePlay.getPlayerList().get(l_j));
                    boolean l_executeOrder = l_order.executeOrder();
                    if (l_executeOrder) {
                        l_orderStatus.add(new CommandResponse(l_executeOrder, "" + d_gamePlay.getPlayerList().get(l_j).getD_playerName() + "'s command executed sucessfully\n"));
                        d_gamePlay.getPlayerList().remove(l_j);
                        d_gamePlay.getPlayerList().add(l_j, ((DeployOrder) l_order).getD_player());

                        int l_noOfArmies = ((DeployOrder) l_order).getD_noOfArmies();
                        if (d_gamePlay.getD_warMap().getD_continents() != null) {
                            for (Map.Entry<Integer, Continent> l_entry : d_gamePlay.getD_warMap().getD_continents().entrySet()) {
                                for (Country l_countries : l_entry.getValue().getD_countryList()) {
                                    if (l_countries.getD_countryName().equalsIgnoreCase(l_countryName)) {
                                        l_countries.setD_noOfArmies(l_noOfArmies);
                                    }
                                }
                            }
                        }

                    } else {
                        l_orderStatus.add(new CommandResponse(l_executeOrder, d_gamePlay.getPlayerList().get(l_j).getD_playerName() + " either country is incorrect or not enough armies\n"));
                    }
                }
            }
        }
        return l_orderStatus;
    }

    /**
     * This method is used to store the user input of orders in player's lst of orders
     *
     * @param p_command String that has been given by user
     * @return return the response in term of wether order is added or player is completed with orders or not
     */
    public CommandResponse issuingPlayer(String p_command) {
        Player l_player = d_gamePlay.getPlayerList().get(PlayCounter);
        if (CounterRound < l_player.getD_orders().size()) {
            CounterRound = l_player.getD_orders().size();
        }
        if (p_command.equalsIgnoreCase("done")) {
            PlayerFlag[PlayCounter] = 1;
            String l_response = l_player.getD_playerName() + " : done with issuing orders";
            d_generalUtil.prepareResponse(true, l_response);
            return d_generalUtil.getResponse();
        } else {
            String[] l_commands = p_command.split("\\s+");
            if (l_player.getD_orders() == null) {
                List<Order> l_order = new ArrayList<Order>();
                d_gamePlay.getPlayerList().get(PlayCounter).setD_currentToCountry(l_commands[1]);
                d_gamePlay.getPlayerList().get(PlayCounter).setD_currentNoOfArmiesToMove(Integer.parseInt(l_commands[2]));
                d_gamePlay.getPlayerList().get(PlayCounter).setD_orders(l_order);
                d_gamePlay.getPlayerList().get(PlayCounter).issue_order();
            } else {
                d_gamePlay.getPlayerList().get(PlayCounter).setD_currentToCountry(l_commands[1]);
                d_gamePlay.getPlayerList().get(PlayCounter).setD_currentNoOfArmiesToMove(Integer.parseInt(l_commands[2]));
                d_gamePlay.getPlayerList().get(PlayCounter).issue_order();

            }

            d_generalUtil.prepareResponse(true, CounterRound + " | " + p_command + " | " + l_player.getD_playerName());
            return d_generalUtil.getResponse();
        }


    }


}
