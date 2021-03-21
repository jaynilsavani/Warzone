package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.Parent;

/**
 * ConcreteState of the State pattern.This Phase is used to execute orders
 * which are issued by players in the previous phase.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class ExecuteOrderPhase extends GamePlay {
    /**
     * This parameterized constructor is used to invoke Phase Constructor
     * and set the reference variable to GameEngine object for the state transition.
     *
     * @param p_gameEngine Object of GameEngine
     */
    public ExecuteOrderPhase(GameEngine p_gameEngine) {
        super( p_gameEngine );
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
        IssueOrderPhase l_isueOrderPhase = new IssueOrderPhase( d_gameEngine );
        l_isueOrderPhase.d_gameData = (GameData) d_gameData;
        l_isueOrderPhase.d_commandResponses = d_commandResponses;
        d_gameEngine.setPhase( l_isueOrderPhase );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void executeOrder() {
        List<CommandResponse> l_orderStatus = new ArrayList<>();
        for (int l_i = 0; l_i < d_gameData.getD_maxNumberOfTurns(); l_i++) {                       //main loop for giving the turn to player in round-robin
            for (int l_j = 0; l_j < d_gameData.getD_playerList().size(); l_j++) {
                if (d_gameData.getD_playerList().get( l_j ).hasOrder()) {             //checks if the player has an order or not
                    Order l_order = d_gameData.getD_playerList().get(l_j).next_order();
                    boolean l_executeOrder;

                        l_executeOrder = l_order.executeOrder();                           //invokes the order
                        d_gameData=l_order.getGameData();
                        if (l_executeOrder) {
                            l_orderStatus.add(new CommandResponse(l_executeOrder, "" + d_gameData.getD_playerList().get(l_j).getD_playerName() + "'s command executed sucessfully\n"));
                        } else {                                                              //return false ,if the deployment is failed
                            l_orderStatus.add(new CommandResponse(l_executeOrder, d_gameData.getD_playerList().get(l_j).getD_playerName() + " either country is incorrect or not enough armies\n"));
                        }
                }
            }
        }

        d_commandResponses = l_orderStatus;
        this.next( null );
    }

    @Override
    public void issueOrder(String p_command) {
        this.printInvalidCommandMessage();
    }

    @Override
    public void assignReinforcements() {
        this.printInvalidCommandMessage();
    }


}
