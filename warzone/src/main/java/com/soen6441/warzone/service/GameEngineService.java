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

    /**
     * used to get the list of player with the list of countries they owned
     *
     * @param p_gameData object to get the data of player
     * @return string of player to ownedcountries list with a formatting
     */
    public String playerOwnedCountries(GameData p_gameData);
}
