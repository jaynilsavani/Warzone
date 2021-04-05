package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.*;
import com.soen6441.warzone.observerpattern.LogEntryBuffer;
import com.soen6441.warzone.observerpattern.WriteLogFile;
import com.soen6441.warzone.service.MapHandlingInterface;
import com.soen6441.warzone.service.impl.MapHandlingImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.Parent;

/**
 * ConcreteState of the State pattern.This Phase is used to execute orders which
 * are issued by players in the previous phase.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class ExecuteOrderPhase extends GamePlay {

    private LogEntryBuffer d_logEntryBuffer = new LogEntryBuffer();
    private WriteLogFile d_writeLogFile = new WriteLogFile(d_logEntryBuffer);

    /**
     * This parameterized constructor is used to invoke Phase Constructor and
     * set the reference variable to GameEngine object for the state transition.
     *
     * @param p_gameEngine Object of GameEngine
     */
    public ExecuteOrderPhase(GameEngine p_gameEngine) {
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
        IssueOrderPhase l_isueOrderPhase = new IssueOrderPhase(d_gameEngine);
        l_isueOrderPhase.d_gameData = (GameData) d_gameData;
        l_isueOrderPhase.d_commandResponses = d_commandResponses;
        d_gameEngine.setPhase(l_isueOrderPhase);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void executeOrder() {
        List<CommandResponse> l_orderStatus = new ArrayList<>();
        for (int l_i = 0; l_i < d_gameData.getD_maxNumberOfTurns(); l_i++) {                       //main loop for giving the turn to player in round-robin
            for (int l_j = 0; l_j < d_gameData.getD_playerList().size(); l_j++) {
                if (d_gameData.getD_playerList().get(l_j).hasOrder()) {             //checks if the player has an order or not
                    Order l_order = d_gameData.getD_playerList().get(l_j).next_order();
                    boolean l_executeOrder = false;
                    MapHandlingInterface l_map = new MapHandlingImpl();
                    if (l_order instanceof AirliftOrder || l_order instanceof BlockadeOrder
                            || l_order instanceof BombOrder || l_order instanceof NegotiateOrder) {
                        GameCard l_gameCard = GameCard.commandToGameCardMapper(l_order);
                        boolean l_hasCard = d_gameData.getD_playerList().get(l_j).hasCard(
                                l_gameCard);
                        if (l_hasCard) {
                            l_executeOrder = l_order.executeOrder();                           //invokes the order
                            d_gameData = l_order.getGameData();
                            d_gameData.getD_playerList().get(l_j).removeCard(l_gameCard);
                        }
                    } else {

                        l_executeOrder = l_order.executeOrder();                           //invokes the order
                        d_gameData = l_order.getGameData();
                    }

                    if (l_executeOrder && d_gameData.getD_playerList().get(l_j).getD_ownedCountries().size() == l_map.getAvailableCountries(d_gameData.getD_warMap()).size()) {
                        l_orderStatus.add(new CommandResponse(l_executeOrder, "" + d_gameData.getD_playerList().get(l_j).getD_playerName().toUpperCase() + " IS WINNER!!!\n"));
                        d_logEntryBuffer.setLogEntryBuffer("Winner Declared: " + d_gameData.getD_playerList().get(l_j).getD_playerName().toUpperCase() + " IS WINNER!!!\n");
                        break;
                    }
                    //to check if player's order executed successfully
                    if (l_executeOrder) {
                        l_orderStatus.add(new CommandResponse(l_executeOrder, "" + d_gameData.getD_playerList().get(l_j).getD_playerName() + "'s command executed successfully\n"));
                        d_logEntryBuffer.setLogEntryBuffer("Order Executed Successfully: " + d_gameData.getD_playerList().get(l_j).getD_playerName() + "'s command executed successfully");
                    }
                    if (!l_executeOrder && (l_order instanceof DeployOrder)) {                                                              //return false ,if the deployment is failed
                        l_orderStatus.add(new CommandResponse(l_executeOrder, d_gameData.getD_playerList().get(l_j).getD_playerName() +" :: "+ l_order.getOrderResponse()));
                        d_logEntryBuffer.setLogEntryBuffer("Order Execution Failed: " + l_order.getOrderResponse());
                    }
//                    if (!l_executeOrder && (l_order instanceof AdvanceOrder)) {                                                              //return false ,if the deployment is failed
//                        l_orderStatus.add(new CommandResponse(l_executeOrder, d_gameData.getD_playerList().get(l_j).getD_playerName() + " (advance) either countryfrom or countryto is incorrect or not enough armies or player has negotiated\n"));
//                        d_logEntryBuffer.setLogEntryBuffer("Order Execution Failed: " + d_gameData.getD_playerList().get(l_j).getD_playerName() + " (advance) either countryfrom or countryto is incorrect or not enough armies");
//                    }
//                    if (!l_executeOrder && (l_order instanceof BombOrder)) {                                                              //return false ,if the deployment is failed
//                        l_orderStatus.add(new CommandResponse(l_executeOrder, d_gameData.getD_playerList().get(l_j).getD_playerName() + " (bomb) country is incorrect or his owned country or card is not assigned\n"));
//                        d_logEntryBuffer.setLogEntryBuffer("Order Execution Failed: " + d_gameData.getD_playerList().get(l_j).getD_playerName() + "  (bomb) country is incorrect or his owned country or card is not assigned ");
//                    }
//                    if (!l_executeOrder && (l_order instanceof BlockadeOrder)) {                                                              //return false ,if the deployment is failed
//                        l_orderStatus.add(new CommandResponse(l_executeOrder, d_gameData.getD_playerList().get(l_j).getD_playerName() + " (blockade) country is incorrect or not his owned coutry or card is not assigned\n"));
//                        d_logEntryBuffer.setLogEntryBuffer("Order Execution Failed: " + d_gameData.getD_playerList().get(l_j).getD_playerName() + "(blockade) country is incorrect or not his owned coutry or card is not assigned");
//                    }
//                    if (!l_executeOrder && (l_order instanceof NegotiateOrder)) {                                                              //return false ,if the deployment is failed
//                        l_orderStatus.add(new CommandResponse(l_executeOrder, d_gameData.getD_playerList().get(l_j).getD_playerName() + "(negotiate) player is incorrect or same player for negotiation or card is not assigned\n"));
//                        d_logEntryBuffer.setLogEntryBuffer("Order Execution Failed: " + d_gameData.getD_playerList().get(l_j).getD_playerName() + " (negotiate) player is incorrect or same player for negotiation or card is not assigned");
//                    }
//                    if (!l_executeOrder && (l_order instanceof AirliftOrder)) {                                                              //return false ,if the deployment is failed
//                        l_orderStatus.add(new CommandResponse(l_executeOrder, d_gameData.getD_playerList().get(l_j).getD_playerName() + " (airlift) either countryfrom or countryto is incorrect or not enough armies or player has negotiated or card is not assigned\n"));
//                        d_logEntryBuffer.setLogEntryBuffer("Order Execution Failed: " + d_gameData.getD_playerList().get(l_j).getD_playerName() + " (airlift) either countryfrom or countryto is incorrect or not enough armies or player has negotiated or card is not assigned");
//                    }
                }
            }
        }
        for (Player l_player : d_gameData.getD_playerList()) {
            if (l_player.isD_isWinner()) {
                l_player.addCard(GameCard.randomGameCard());
                l_orderStatus.add(new CommandResponse(true, "player " + l_player.getD_playerName() + " received " + l_player.getD_cards()));
            }
        }

        d_commandResponses = l_orderStatus;
        this.next(null);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void issueOrder(String p_command) {
        this.printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void assignReinforcements() {
        this.printInvalidCommandMessage();
    }

}
