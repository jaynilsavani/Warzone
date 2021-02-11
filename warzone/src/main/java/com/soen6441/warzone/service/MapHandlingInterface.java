package com.soen6441.warzone.service;


import com.soen6441.warzone.model.WarMap;
/**
 * Implementation of mapping data
 *
 * @author <a href="mailto:jenilsavani009@gmail.com">Jaynil Savani</a>
 */
public interface MapHandlingInterface {

    /**
     * This method will validate all command entered by user
     *
     * @return true if command is valid
     */
    boolean validateCommand();
    
    /**
     *This method will read map file and store data into  WarMap model object
     *
     *@return WarMap model
    */
    WarMap readMap();
}
