package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.soen6441.warzone.service.GameConfigService;
import com.soen6441.warzone.service.GameEngineService;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    GameConfigService d_g;

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
        boolean l_executeOrder;
        List<CommandResponse> l_orderStatus = new ArrayList<>();
        for (int l_i = 0; l_i < d_gameData.getD_maxNumberOfTurns(); l_i++) {                       //main loop for giving the turn to player in round-robin
            for (int l_j = 0; l_j < d_gameData.getD_playerList().size(); l_j++) {
                if (d_gameData.getD_playerList().get(l_j).hasOrder()) {             //checks if the player has an order or not
                    Order l_order = d_gameData.getD_playerList().get(l_j).next_order();
                    if (l_order instanceof DeployOrder) {
                        ((DeployOrder) l_order).setD_player(d_gameData.getD_playerList().get(l_j));         //to add the player to use in execution
                        ((DeployOrder) l_order).setD_gameData(d_gameData);
                        l_executeOrder = l_order.executeOrder();                           //invokes the order
                        d_gameData=((DeployOrder) l_order).getD_gameData();
                        if (l_executeOrder) {
                            l_orderStatus.add(new CommandResponse(l_executeOrder, "" + d_gameData.getD_playerList().get(l_j).getD_playerName() + "'s command executed sucessfully\n"));
                        } else {                                                              //return false ,if the deployment is failed
                            l_orderStatus.add(new CommandResponse(l_executeOrder, d_gameData.getD_playerList().get(l_j).getD_playerName() + " either country is incorrect or not enough armies\n"));
                        }

                    }
                    else if (l_order instanceof AdvanceOrder) {

                        ((AdvanceOrder) l_order).setD_player(d_gameData.getD_playerList().get(l_j));
                        ((AdvanceOrder) l_order).setD_gameData(d_gameData);
                        l_executeOrder = l_order.executeOrder();
                        d_gameData = ((AdvanceOrder) l_order).getD_gameData();
                        //System.out.println("after "+d_gameData);
                        if (l_executeOrder) {
                            l_orderStatus.add(new CommandResponse(l_executeOrder, "" + d_gameData.getD_playerList().get(l_j).getD_playerName() + "'s command executed sucessfully\n"));

                        } else {
                            l_orderStatus.add(new CommandResponse(l_executeOrder, d_gameData.getD_playerList().get(l_j).getD_playerName() + "countryfrom or coutry to or number of armies are not valid\n"));
                        }

                    } else if (l_order instanceof BombOrder) {
                        //implementation of execution order
                    } else if (l_order instanceof BlockadeOrder) {
                        //implementation of execution order
                    } else if (l_order instanceof AirliftOrder) {
                        //implementation of execution order
                    } else if (l_order instanceof NegotiateOrder) {
                        //implementation of execution order
                    }


                }
            }
        }

        d_commandResponses = l_orderStatus;
        this.next(null);
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
