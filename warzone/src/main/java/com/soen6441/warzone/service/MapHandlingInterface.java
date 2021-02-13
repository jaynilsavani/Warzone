package com.soen6441.warzone.service;

import com.soen6441.warzone.model.CommandResponse;
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
    public CommandResponse validateCommand(String p_command);

    /**
     * This function is used to check whether string is empty or not
     *
     * @param p_str string passed by user
     * @return true if string is not null
     */
    public boolean isNullOrEmpty(String p_str);

    /**
     * This method will validate the I/O given from GUI or terminal
     *
     * @param p_string string you want to validate
     * @param p_regex regex for validation
     * @return true if string matches with regex
     */
    public boolean validateIOString(String p_string, String p_regex);
    
    /**
     * This method will store WarMap model into file
     * 
     * @param p_warMap is the object of WarMap model
     * @return true if map is successfully write to the file
     */
    boolean writeMapToFile(WarMap p_warMap);

     /**
     *This method will read map file and store data into  WarMap model object
     *
     *@param p_fileName fileName to read Map
     * 
     *@return WarMap model
    */
    public WarMap readMap(String p_fileName);

    /**
     * This method is used to show the map in 2D matrix containing Countries as x-y axis
     * @return
     */
    public CommandResponse showMap();
}
