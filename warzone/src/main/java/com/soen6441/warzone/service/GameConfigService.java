package com.soen6441.warzone.service;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.model.Tournament;
import com.soen6441.warzone.model.WarMap;

import java.io.IOException;
import java.util.AbstractMap;

/**
 * This interface is used for all game related Configuration for GameData
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">Yashkumar Vaghani</a>
 */
public interface GameConfigService {

    /**
     * This function is used to show the map of the countries of a given
     * Gameplay
     *
     * @param p_gameData gameplay phase of the player
     * @return command response to show the map of countries with players
     * details
     */
    public CommandResponse showPlayerMap(GameData p_gameData);

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
     * @param p_currentGameData cuurent Gameplay Object
     * @param p_commnd player updation command
     * @return updated GameData and command response
     */
    public AbstractMap.Entry<GameData, CommandResponse> updatePlayer(GameData p_currentGameData, String p_commnd);

    /**
     * To assign the countries on the first time when map was loaded
     *
     * @param p_gameData : object of GameData Model
     * @return commanresponse stating the detail of each player with their
     * countries
     */
    public CommandResponse assignCountries(GameData p_gameData);


    /**
     *
     * @return CommandResponse
     */
    public CommandResponse getResponse();
}
