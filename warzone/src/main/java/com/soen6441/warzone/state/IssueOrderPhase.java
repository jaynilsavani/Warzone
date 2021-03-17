package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.Order;
import com.soen6441.warzone.model.Player;
import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * ConcreteState of the State pattern.This Phase is used to take order from
 * each players in round robin manner.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class IssueOrderPhase extends GamePlay {

    /**
     * This parameterized constructor is used to invoke Phase Constructor and set the
     * reference variable to GameEngine object for the state transition
     * @param p_gameEngine Object of GameEngine
     *
     */
    public IssueOrderPhase(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }
    /**
     * {@inheritDoc }
     *
     */
    @Override
    public Parent execute() {
        this.printInvalidCommandMessage();
        return null;
    }
    /**
     * {@inheritDoc }
     *
     */
    @Override
    public void next(Object p_nextObject) {
        ExecuteOrderPhase executeOrderPhase = new ExecuteOrderPhase(d_gameEngine);
        executeOrderPhase.d_gameData = this.d_gameData;
        d_gameEngine.setPhase(executeOrderPhase);
        d_gameEngine.getPhase().executeOrder();
    }
    /**
     * {@inheritDoc }
     *
     */
    @Override
    public void executeOrder() {
        this.printInvalidCommandMessage();
    }

    @Override
    public void issuingPlayer(String p_command) {
        d_gameEngine=d_gameEngine.getPhase().d_gameEngine;
        Player l_player = d_gameData.getD_playerList().get(d_gameEngine.PlayCounter);              //assigns the current player using the playcounter
        if (d_gameData.getD_maxNumberOfTurns() < l_player.getD_orders().size()) {                         //update the roundcounter if the one round completes
            d_gameData.setD_maxNumberOfTurns(l_player.getD_orders().size());
        }
        if (p_command.equalsIgnoreCase("done")) {                        //stops the player to get further chance to issue an order
            d_gameEngine.PlayerFlag[d_gameEngine.PlayCounter] = 1;
            String l_response = l_player.getD_playerName() + " : done with issuing orders";
            d_gameEngine.d_generalUtil.prepareResponse(true, l_response);
            d_issueResponse=d_gameEngine.d_generalUtil.getResponse();

        } else {                                                   //issue and order to that player
            String[] l_commands = p_command.split("\\s+");
            if (l_player.getD_orders() == null) {
                List<Order> l_order = new ArrayList<Order>();
                d_gameData.getD_playerList().get(d_gameEngine.PlayCounter).setD_currentToCountry(l_commands[1]);
                d_gameData.getD_playerList().get(d_gameEngine.PlayCounter).setD_currentNoOfArmiesToMove(Integer.parseInt(l_commands[2]));
                d_gameData.getD_playerList().get(d_gameEngine.PlayCounter).setD_orders(l_order);
                d_gameData.getD_playerList().get(d_gameEngine.PlayCounter).issue_order();
            } else {
                d_gameData.getD_playerList().get(d_gameEngine.PlayCounter).setD_currentToCountry(l_commands[1]);
                d_gameData.getD_playerList().get(d_gameEngine.PlayCounter).setD_currentNoOfArmiesToMove(Integer.parseInt(l_commands[2]));
                d_gameData.getD_playerList().get(d_gameEngine.PlayCounter).issue_order();

            }

            d_gameEngine.d_generalUtil.prepareResponse(true, d_gameData.getD_maxNumberOfTurns() + " | " + p_command + " | " + l_player.getD_playerName());
            d_issueResponse=d_gameEngine.d_generalUtil.getResponse();
        }
    }


}
