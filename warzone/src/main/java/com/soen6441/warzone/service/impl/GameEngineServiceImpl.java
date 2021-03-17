package com.soen6441.warzone.service.impl;

import static com.soen6441.warzone.config.WarzoneConstants.*;
import com.soen6441.warzone.model.Continent;
import com.soen6441.warzone.model.Country;
import com.soen6441.warzone.model.GameData;
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
    public GameData assignReinforcements(GameData p_gameData) {

        if (p_gameData.getD_playerList() != null && (!p_gameData.getD_playerList().isEmpty())) {

            for (Player l_player : p_gameData.getD_playerList()) {
                if (l_player.getD_ownedCountries() != null && (!l_player.getD_ownedCountries().isEmpty())) {
                    int l_noOfArmy = DEFAULT_ASSIGN_REINFORCEMENT_INITIAL;
                    //This is used to check and assign noOf armies According to Warzone rule based on owned countries
                    if ((l_player.getD_ownedCountries().size() / DEFAULT_ASSIGN_REINFORCEMENT_DIVIDER) > DEFAULT_ASSIGN_REINFORCEMENT_INITIAL) {
                        l_noOfArmy = (l_player.getD_ownedCountries().size() / DEFAULT_ASSIGN_REINFORCEMENT_DIVIDER);
                    }
                    List<Continent> l_continentsOwnedByPlayer = continentsOwnedByPlayer(l_player, p_gameData);
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
        return p_gameData;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Continent> continentsOwnedByPlayer(Player p_player, GameData p_gameData) {
        List<Continent> l_continents = new ArrayList<>();
        if (p_gameData.getD_warMap().getD_continents() != null) {
            for (Map.Entry<Integer, Continent> l_continentEntry : p_gameData.getD_warMap().getD_continents().entrySet()) {
                Continent l_continent = l_continentEntry.getValue();
                //Check whether player owned all countries of continent or not
                if (p_player.getD_ownedCountries().containsAll(l_continent.getD_countryList())) {
                    l_continents.add(l_continent);
                }
            }
        }
        return l_continents;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String showReinforcementArmies(GameData p_gameData) {
        String l_armies = "";
        for (Player l_p : p_gameData.getD_playerList()) {
            l_armies = l_armies + l_p.getD_playerName() + " : " + l_p.getD_noOfArmies() + "\n";
        }
        return l_armies;
    }

    public String playerOwnedCountries(GameData p_gameData) {
        String l_responseString = "\n";
        for (Player l_player : p_gameData.getD_playerList()) {
            l_responseString += l_player.getD_playerName() + " : [";
            for (Country l_cn : l_player.getD_ownedCountries()) {
                l_responseString += l_cn.getD_countryName() + " , ";
            }
            l_responseString += " ] \n ";
        }
        return l_responseString;
    }
}
