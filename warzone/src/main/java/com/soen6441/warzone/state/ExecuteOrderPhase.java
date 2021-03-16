package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.Continent;
import com.soen6441.warzone.model.Country;
import com.soen6441.warzone.model.DeployOrder;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.model.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.Parent;

/**
 *
 * This Class is used to take order from each players
 * in round robin manner and then executes those orders.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class ExecuteOrderPhase extends GamePlay {
    /**
     *  This is a constructor which is used to invoke GamePlay Constructor
     * @param p_gameEngine Object of GameEngine
     */
    public ExecuteOrderPhase(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * {@inheritDoc }
     * @return the null value
     */
    @Override
    public Parent execute() {
        this.printInvalidCommandMessage();
        return null;
    }

    /**
     * {@inheritDoc }
     * @param p_nextObject Object that is being passed to next phase
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
                    String l_countryName = ((DeployOrder) l_order).getD_CountryName();
                    ((DeployOrder) l_order).setD_player(d_gameData.getD_playerList().get(l_j));         //to add the player to use in execution
                    boolean l_executeOrder = l_order.executeOrder();                           //invokes the order
                    if (l_executeOrder) {
                        l_orderStatus.add(new CommandResponse(l_executeOrder, "" + d_gameData.getD_playerList().get(l_j).getD_playerName() + "'s command executed sucessfully\n"));
                        d_gameData.getD_playerList().remove(l_j);                                          //replaces the player with the updated player from order
                        d_gameData.getD_playerList().add(l_j, ((DeployOrder) l_order).getD_player());

                        int l_noOfArmies = ((DeployOrder) l_order).getD_noOfArmies();
                        if (d_gameData.getD_warMap().getD_continents() != null) {
                            for (Map.Entry<Integer, Continent> l_entry : d_gameData.getD_warMap().getD_continents().entrySet()) {
                                for (Country l_countries : l_entry.getValue().getD_countryList()) {
                                    if (l_countries.getD_countryName().equalsIgnoreCase(l_countryName)) {
                                        l_countries.setD_noOfArmies(l_noOfArmies);                              //sets the no. of armies to the country of map
                                    }
                                }
                            }
                        }

                    } else {                                                              //return false ,if the deployment is failed
                        l_orderStatus.add(new CommandResponse(l_executeOrder, d_gameData.getD_playerList().get(l_j).getD_playerName() + " either country is incorrect or not enough armies\n"));
                    }
                }
            }
        }

        d_commandResponses = l_orderStatus;
        this.next(null);
    }

}
