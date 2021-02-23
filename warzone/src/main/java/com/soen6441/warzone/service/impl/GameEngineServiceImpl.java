package com.soen6441.warzone.service.impl;

import static com.soen6441.warzone.config.WarzoneConstants.*;

import com.soen6441.warzone.model.Continent;
import com.soen6441.warzone.model.GamePlay;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.service.GameEngineService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * This Class is used for manipulating the utilities for GameEngine
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Service
public class GameEngineServiceImpl implements GameEngineService {

    /**
     * {@inheritDoc }
     */
    @Override
    public GamePlay assignReinforcements(GamePlay p_gamePlay) {

        if (p_gamePlay.getD_playerList() != null && (!p_gamePlay.getD_playerList().isEmpty())) {

            for (Player l_player : p_gamePlay.getD_playerList()) {
                if (l_player.getD_ownedCountries() != null && (!l_player.getD_ownedCountries().isEmpty())) {
                    int l_noOfArmy = DEFAULT_ASSIGN_REINFORCEMENT_INITIAL;
                    if ((l_player.getD_ownedCountries().size() / DEFAULT_ASSIGN_REINFORCEMENT_DIVIDER) > DEFAULT_ASSIGN_REINFORCEMENT_INITIAL) {
                        l_noOfArmy = (l_player.getD_ownedCountries().size() / DEFAULT_ASSIGN_REINFORCEMENT_DIVIDER);
                    }
                    List<Continent> l_continentsOwnedByPlayer = continentsOwnedByPlayer(l_player, p_gamePlay);
                    if (l_continentsOwnedByPlayer.size() > 0) {
                        for (Continent continent : l_continentsOwnedByPlayer) {
                            l_noOfArmy += continent.getD_continentValue();
                        }
                    }
                    l_player.setD_noOfArmies(l_noOfArmy);

                }
            }
        }
        return p_gamePlay;
    }

    /**
     * This is used to get Continents owned By Given Player
     *
     * @param p_player   Object of Player For which continents are obtained
     * @param p_gamePlay Current Game play Object of the GameState
     * @return List of continents owned by Given Player
     */
    private List<Continent> continentsOwnedByPlayer(Player p_player, GamePlay p_gamePlay) {
        List<Continent> l_continents = new ArrayList<>();
        if (p_gamePlay.getD_warMap().getD_continents() != null) {
            for (Map.Entry<Integer, Continent> l_continentEntry : p_gamePlay.getD_warMap().getD_continents().entrySet()) {
                Continent l_continent = l_continentEntry.getValue();
                if (p_player.getD_ownedCountries().containsAll(l_continent.getD_countryList())) {
                    l_continents.add(l_continent);
                }
            }

        }
        return l_continents;
    }

    /**
     * @param p_gamePlay gives the gameengine to retrieve player data
     * @return gives the string with player name and their armies
     */
    @Override
    public String showReinforcementArmies(GamePlay p_gamePlay) {
        String l_armies = "";
        for (Player l_p : p_gamePlay.getD_playerList()) {
            l_armies = l_armies + l_p.getD_playerName() + " : " + l_p.getD_noOfArmies() + "\n";
        }
        return l_armies;
    }
}
