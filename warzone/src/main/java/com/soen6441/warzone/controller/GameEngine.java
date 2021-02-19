package com.soen6441.warzone.controller;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GamePlay;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.service.GameEngineService;
import com.soen6441.warzone.service.MapHandlingInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

    @Autowired
    GamePlay d_gamePlay;

    @Autowired
    private GameEngineService d_gameEngineSevice;

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
     * @param location of the FXML file
     * @param resources is properties information
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * This method is used to get fire command from user and put it as a
     * parameter in validation
     *
     * @param event
     */
    public void getData(ActionEvent event) {
        String s = d_CommandLine.getText();
        System.out.println(s);
        d_CommandLine.clear();
    }

    /**
     * This is used for setting GameConfig for GameEngine
     *
     * @param p_gameConfig
     */
    public void setGamePlay(GamePlay p_gameConfig) {
        d_gamePlay = p_gameConfig;
        mainGameLoop();
    }

    /**
     * This is used for Main Game loop Which includes
     * AssignREinforcement,IssueOrder phase,NextOrder
     */
    private void mainGameLoop() {
        //Call for reinforcement
        d_gamePlay = d_gameEngineSevice.assignReinforcements(d_gamePlay);
        //call for issue order 
        //call for next order
        nextOrders();
    }

    /**
     *
     * This method is used for executing issued Order
     *
     * @return The liast of Command Response of Executed Order
     */
    private List<CommandResponse> nextOrders() {
        List<CommandResponse> l_orderStatus = new ArrayList<>();
        for (int i = 0; i < d_gamePlay.getMaxNumberOfTurns(); i++) {
            for (Player l_player : d_gamePlay.getPlayerList()) {
                if (l_player.hasOrder()) {
                    boolean executeOrder = l_player.next_order().executeOrder();
                    l_orderStatus.add(new CommandResponse(executeOrder, "" + l_player.getD_playerName() + "'s command executed sucessfully"));
                }
            }
        }
        return l_orderStatus;
    }

}
