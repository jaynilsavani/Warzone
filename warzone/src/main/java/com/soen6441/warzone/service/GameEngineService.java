package com.soen6441.warzone.service;

import com.soen6441.warzone.model.Continent;
import com.soen6441.warzone.model.Player;
import java.util.List;
import com.soen6441.warzone.model.GamePlay;

/**
 * This interface is used for Function for Game playing and
 * GameEngineServiceImpl is the implementation of it.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public interface GameEngineService {

    /**
     * This method used to apply assign Reinforcement on provided GamePlay
     * Object
     *
     * @param p_gamePlay Current Game Play object on which assign Reinforcement
     * need to apply
     * @return updated Gameplay Which has assigned reinforcement army to
     * countries
     */
    public GamePlay assignReinforcements(GamePlay p_gamePlay);

    /**
     * @param p_gamePlay gives the gameengine to retrieve player data
     * @return gives the string with player name and their armies
     */
    public String showReinforcementArmies(GamePlay p_gamePlay);

    /**
     * This is used to get Continents owned By Given Player
     *
     * @param p_player Object of Player For which continents are obtained
     * @param p_gamePlay Current Game play Object of the GameState
     * @return List of continents owned by Given Player
     */
    public List<Continent> continentsOwnedByPlayer(Player p_player, GamePlay p_gamePlay);
}
