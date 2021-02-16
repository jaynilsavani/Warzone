package com.soen6441.warzone.service;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GamePlay;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.model.WarMap;
import java.io.IOException;

/**
 *
 * This interface is used for all gaming Configuration for the Player to set and
 * view
 *
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">Yashkumar Vaghani</a>
 */
public interface GameConfigService {

    /**
     * This function is used to show the map of the countries of a particular
     * player
     *
     * @param p_player
     * @return commandresponse to show the map of countries a player has
     */
    public CommandResponse showPlayerMap(Player p_player);
    
    /**
     * This function is used to load file for game play
     * @param p_fileName : Name of the file that is being loaded
     * @return the loaded map as a command response if it is present
     * @throws java.io.IOException
     */
    public WarMap loadMap(String p_fileName)throws IOException;
    
    /**
     * This function is used to add or remove Player
     * @param p_currentGamePlay cuurent Gameplay Object
     * @param p_commnd player updation command
     * @return updated GamePlayCommand
     */
    public GamePlay updatePlayer(GamePlay p_currentGamePlay,String p_commnd);
}
