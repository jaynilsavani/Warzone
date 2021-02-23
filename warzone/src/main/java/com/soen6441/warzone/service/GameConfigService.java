package com.soen6441.warzone.service;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GamePlay;
import com.soen6441.warzone.model.WarMap;
import java.io.IOException;

/**
 *
 * This interface is used for all game related Configuration for GamePlay
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">Yashkumar Vaghani</a>
 */
public interface GameConfigService {

    /**
     * This function is used to show the map of the countries of a given
     * Gameplay
     *
     * @param p_gamePlay gameplay phase of the player
     * @return commandresponse to show the map of countries with players details
     */
    public CommandResponse showPlayerMap(GamePlay p_gamePlay);

    /**
     * This function is used to load file for game play
     *
     * @param p_fileName : Name of the file that is being loaded
     * @return the loaded map as a command response if it is present
     * @throws java.io.IOException indicates input/output exception
     */
    public WarMap loadMap(String p_fileName) throws IOException;

    /**
     * This function is used to add or remove Player
     *
     * @param p_currentGamePlay cuurent Gameplay Object
     * @param p_commnd player updation command
     * @return updated GamePlay
     */
    public GamePlay updatePlayer(GamePlay p_currentGamePlay, String p_commnd);

    /**
     * To assign the countries on the first time when map was loaded
     *
     * @param p_gamePlay : object of GamePlay Model
     * @return commanresponse stating the detail of each player with their
     * countries
     * @throws java.io.IOException indicates input/output exception
     */
    public CommandResponse assignCountries(GamePlay p_gamePlay) throws IOException;
}
