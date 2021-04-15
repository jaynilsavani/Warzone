package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.Continent;
import com.soen6441.warzone.model.Order;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.observerpattern.LogEntryBuffer;
import com.soen6441.warzone.observerpattern.WriteLogFile;
import com.soen6441.warzone.service.OrderProcessor;
import com.soen6441.warzone.service.impl.OrderProcessorImpl;
import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.soen6441.warzone.config.WarzoneConstants.DEFAULT_ASSIGN_REINFORCEMENT_DIVIDER;
import static com.soen6441.warzone.config.WarzoneConstants.DEFAULT_ASSIGN_REINFORCEMENT_INITIAL;
import com.soen6441.warzone.model.CommandResponse;

import com.soen6441.warzone.service.MapHandlingInterface;
import com.soen6441.warzone.service.impl.MapHandlingImpl;
import com.soen6441.warzone.strategy.HumanStrategy;

/**
 * ConcreteState of the State pattern.This Phase is used to take order from each
 * players in round robin manner.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class IssueOrderPhase extends GamePlay {

    private LogEntryBuffer d_logEntryBuffer = new LogEntryBuffer();
    private WriteLogFile d_writeLogFile = new WriteLogFile(d_logEntryBuffer);
    MapHandlingInterface l_map = new MapHandlingImpl();

    /**
     * This parameterized constructor is used to invoke Phase Constructor and
     * set the reference variable to GameEngine object for the state transition
     *
     * @param p_gameEngine Object of GameEngine
     */
    public IssueOrderPhase(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Parent execute() {
        this.printInvalidCommandMessage();
        return null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void next(Object p_nextObject) {
        ExecuteOrderPhase l_executeOrderPhase = new ExecuteOrderPhase(d_gameEngine);
        l_executeOrderPhase.d_gameData = this.d_gameData;
        d_gameEngine.setPhase(l_executeOrderPhase);
        d_gameEngine.getPhase().executeOrder();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void executeOrder() {
        this.printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void issueOrder(String p_command) {
        Player l_player = d_gameData.getD_playerList().get(d_gameEngine.d_playCounter);              //assigns the current player using the playcounter
        if (d_gameEngine.l_noOfTurns < l_player.getD_orders().size()) {                         //update the roundcounter if the one round completes
            d_gameEngine.l_noOfTurns=l_player.getD_orders().size();
        }
        if (p_command.equalsIgnoreCase("done")) {                        //stops the player to get further chance to issue an order
            d_gameEngine.d_playerFlag[d_gameEngine.d_playCounter] = 1;
            String l_response = l_player.getD_playerName() + " : done with issuing orders";
            d_gameEngine.d_generalUtil.prepareResponse(true, l_response);
            d_issueResponse = d_gameEngine.d_generalUtil.getResponse();

        } else {                                                   //issue and order to that player
            //String[] l_commands = p_command.split("\\s+");
            if (l_player.getD_orders() == null) {
                List<Order> l_order = new ArrayList<>();
                d_gameData.getD_playerList().get(d_gameEngine.d_playCounter).setD_orders(l_order);
            }
            if (l_player.getD_stragey() instanceof HumanStrategy) {
                d_issueResponse = d_gameEngine.d_orderProcessor.processOrder(p_command.trim(), d_gameData);
            }
            //These needs to Uncomment
//            if (d_issueResponse.isD_isValid()) {
//            Player l_p = d_gameData.getD_playerList().get(d_gameEngine.d_playCounter);
            l_player.setOrderProcessor(d_gameEngine.d_player.getOrderProcessor());
            l_player.getD_stragey().setD_gameData(d_gameData);
            l_player.issue_order();
            d_logEntryBuffer.setLogEntryBuffer("Command:: " + l_player.getD_playerName() + "'s turn -> " + l_player.getOrderProcessor().getOrderString());
            if (l_player.getOrderProcessor().getOrderString().equalsIgnoreCase("done")) {
                d_gameEngine.d_playerFlag[d_gameEngine.d_playCounter] = 1;
                String l_response = l_player.getD_playerName() + " : done with issuing orders";
                d_gameEngine.d_generalUtil.prepareResponse(true, l_response);
                d_issueResponse = d_gameEngine.d_generalUtil.getResponse();
            } else {
                d_gameEngine.d_generalUtil.prepareResponse(true, d_gameEngine.l_noOfTurns + " | " + l_player.getOrderProcessor().getOrderString() + " | " + l_player.getD_playerName());
                d_issueResponse = d_gameEngine.d_generalUtil.getResponse();
            }
            if (l_player.getD_ownedCountries().size() == l_map.getAvailableCountries(d_gameData.getD_warMap()).size()) {
                l_player.setD_isWinner(true);
                if (d_gameData.getD_gameMode() != 1) {
                    d_gameEngine.d_CommandLine.setText(l_player.getD_playerName().toUpperCase() + "  IS WINNER!!!\n");
                    d_gameEngine.d_generalUtil.prepareResponse(true, l_player.getD_playerName().toUpperCase() + "  IS WINNER!!!\n");
                    d_issueResponse = d_gameEngine.d_generalUtil.getResponse();
                    d_gameEngine.d_FireCommand.setDisable(true);
                    d_gameEngine.d_CommandLine.setDisable(true);
                    d_gameEngine.d_playerTurn.setText("");
                }
                d_gameEngine.l_winner = true;
            }

        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void assignReinforcements() {
        if (d_gameData.getD_playerList() != null && (!d_gameData.getD_playerList().isEmpty())) {

            for (Player l_player : d_gameData.getD_playerList()) {
                if (l_player.getD_ownedCountries() != null && (!l_player.getD_ownedCountries().isEmpty())) {
                    int l_noOfArmy = DEFAULT_ASSIGN_REINFORCEMENT_INITIAL;
                    //This is used to check and assign noOf armies According to Warzone rule based on owned countries
                    if ((l_player.getD_ownedCountries().size() / DEFAULT_ASSIGN_REINFORCEMENT_DIVIDER) > DEFAULT_ASSIGN_REINFORCEMENT_INITIAL) {
                        l_noOfArmy = (l_player.getD_ownedCountries().size() / DEFAULT_ASSIGN_REINFORCEMENT_DIVIDER);
                    }
                    List<Continent> l_continentsOwnedByPlayer = d_gameEngine.d_gameEngineSevice.continentsOwnedByPlayer(l_player, d_gameData);
                    //This is used to check and assign countries for control value addition
                    if (l_continentsOwnedByPlayer.size() > 0) {
                        for (Continent continent : l_continentsOwnedByPlayer) {
                            l_noOfArmy += continent.getD_continentValue();
                        }
                    }
                    l_player.setD_noOfArmies(l_noOfArmy);
                    l_player.setD_issuedNoOfArmies(l_noOfArmy);

                }
            }
        }
    }
}
