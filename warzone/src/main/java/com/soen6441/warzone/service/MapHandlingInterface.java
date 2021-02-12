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
     * @param p_command contains command string entered by user
     * @return true if command is valid
     */
    boolean validateCommand(String p_command);

    /**
     * This function is used to check whether string is empty or not
     *
     * @param p_str string passed by user
     * @return true if string is not null
     */
    boolean isNullOrEmpty(String p_str);

    /**
     * This method will validate the I/O given from GUI or terminal
     *
     * @param p_string string you want to validate
     * @param p_regex regex for validation
     * @return true if string matches with regex
     */
    boolean validateIOString(String p_string, String p_regex);
    
    /**
     *This method will read map file and store data into  WarMap model object
     *
     *@return WarMap model
    */
    WarMap readMap(String p_fileName);
}
