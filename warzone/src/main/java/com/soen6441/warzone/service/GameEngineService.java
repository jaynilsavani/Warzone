package com.soen6441.warzone.service;

import com.soen6441.warzone.model.Continent;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.model.Player;
import java.util.List;

/**
 * This interface is used for Function for Game playing and
 * GameEngineServiceImpl is the implementation of it.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public interface GameEngineService {

    /**
     * This method used to apply assign Reinforcement on provided GameData
     * Object
     *
     * @param p_gameData Current Game Play object on which assign Reinforcement
     * need to apply
     * @return updated Gameplay Which has assigned reinforcement army to
     * countries
     */
    public GameData assignReinforcements(GameData p_gameData);

    /**
     * @param p_gameData gives the gameengine to retrieve player data
     * @return gives the string with player name and their armies
     */
    public String showReinforcementArmies(GameData p_gameData);

    /**
     * This is used to get Continents owned By Given Player
     *
     * @param p_player Object of Player For which continents are obtained
     * @param p_gameData Current Game play Object of the GameState
     * @return List of continents owned by Given Player
     */
    public List<Continent> continentsOwnedByPlayer(Player p_player, GameData p_gameData);
}
