package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.Continent;
import com.soen6441.warzone.model.Order;
import com.soen6441.warzone.model.Player;
import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.List;

import static com.soen6441.warzone.config.WarzoneConstants.DEFAULT_ASSIGN_REINFORCEMENT_DIVIDER;
import static com.soen6441.warzone.config.WarzoneConstants.DEFAULT_ASSIGN_REINFORCEMENT_INITIAL;

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
    public void issueOrder(String p_command) {
        d_gameEngine=d_gameEngine.getPhase().d_gameEngine;
        Player l_player = d_gameData.getD_playerList().get(d_gameEngine.d_playCounter);              //assigns the current player using the playcounter
        if (d_gameData.getD_maxNumberOfTurns() < l_player.getD_orders().size()) {                         //update the roundcounter if the one round completes
            d_gameData.setD_maxNumberOfTurns(l_player.getD_orders().size());
        }
        if (p_command.equalsIgnoreCase("done")) {                        //stops the player to get further chance to issue an order
            d_gameEngine.d_playerFlag[d_gameEngine.d_playCounter] = 1;
            String l_response = l_player.getD_playerName() + " : done with issuing orders";
            d_gameEngine.d_generalUtil.prepareResponse(true, l_response);
            d_issueResponse=d_gameEngine.d_generalUtil.getResponse();

        } else{                                                   //issue and order to that player
            String[] l_commands = p_command.split("\\s+");
            if(l_player.getD_orders()==null)
            {
                List<Order> l_order=new ArrayList<>();
                d_gameData.getD_playerList().get(d_gameEngine.d_playCounter).setD_orders(l_order);
            }
            if(l_commands[0].equalsIgnoreCase("deploy"))
            {
                d_gameData.getD_playerList().get(d_gameEngine.d_playCounter).setD_commandtype(1);
                d_gameData.getD_playerList().get(d_gameEngine.d_playCounter).setD_currentToCountry(l_commands[1]);
                d_gameData.getD_playerList().get(d_gameEngine.d_playCounter).setD_currentNoOfArmiesToMove(Integer.parseInt(l_commands[2]));
                d_gameData.getD_playerList().get(d_gameEngine.d_playCounter).issue_order();
            }
            else if(l_commands[0].equalsIgnoreCase("advance"))
            {
                d_gameData.getD_playerList().get(d_gameEngine.d_playCounter).setD_commandtype(2);
                //implement issue_order once model is created
            }
            else if(l_commands[0].equalsIgnoreCase("bomb"))
            {
                d_gameData.getD_playerList().get(d_gameEngine.d_playCounter).setD_commandtype(3);
                d_gameData.getD_playerList().get(d_gameEngine.d_playCounter).setD_currentToCountry(l_commands[1]);
                d_gameData.getD_playerList().get(d_gameEngine.d_playCounter).issue_order();
            }
            else if(l_commands[0].equalsIgnoreCase("blockade"))
            {
                d_gameData.getD_playerList().get(d_gameEngine.d_playCounter).setD_commandtype(4);
                //implement issue_order once model is created
            }
            else if(l_commands[0].equalsIgnoreCase("airlift"))
            {
                d_gameData.getD_playerList().get(d_gameEngine.d_playCounter).setD_commandtype(5);
                //implement issue_order once model is created
            }
            else if(l_commands[0].equalsIgnoreCase("negotiate"))
            {
                d_gameData.getD_playerList().get(d_gameEngine.d_playCounter).setD_commandtype(6);
                //implement issue_order once model is created
            }


            d_gameEngine.d_generalUtil.prepareResponse(true, d_gameData.getD_maxNumberOfTurns() + " | " + p_command + " | " + l_player.getD_playerName());
            d_issueResponse=d_gameEngine.d_generalUtil.getResponse();
        }
    }

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

                }
            }
        }
    }


}
