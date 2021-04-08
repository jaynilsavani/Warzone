package com.soen6441.warzone.service;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.Country;
import com.soen6441.warzone.model.WarMap;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This interface is used for Utility of Map Editor related command and
 * MapHandlingImpl is the implementation of it
 *
 * @author <a href="mailto:jenilsavani009@gmail.com">Jaynil Savani</a>
 */
public interface MapHandlingInterface {

    /**
     * This method will validate all command entered by user for MapEditor
     *
     * @param p_command contains command string entered by user
     * @return CommandResponse pertaining validity and message
     */
    public CommandResponse validateCommand(String p_command);


    /**
     * This method is used to show the map in 2D matrix containing Countries as
     * x-y axis
     *
     * @param p_warMap : Object of map
     * @return CommandResponse formatted in matrix form for better user
     * understanding
     */
    public CommandResponse showMap(WarMap p_warMap);

    /**
     * This function will validate map file
     *
     * @param p_warMap Object of warmap
     * @return return validity of map
     */
    public boolean validateMap(WarMap p_warMap);

    /**
     * used to get all countries available in the map
     *
     * @param p_continentMap is the object of WarMap model
     * @return array list of the country
     */
    public ArrayList<Country> getAvailableCountries(WarMap p_continentMap);
    
     
    }
