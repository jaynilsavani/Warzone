package com.soen6441.warzone.service;


import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.Player;

/**
 *
 * This interface is used for all gaming Configuration for the Player to set and view
 *
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">Yashkumar Vaghani</a>
 */
public interface GameConfigService {
    /**
     *  This function is used to show the map of the countries of a particular player
     * @param p_player
     * @return commandresponse to show the map of countries a player has
     */
    public CommandResponse showPlayerMap(Player p_player);
}
