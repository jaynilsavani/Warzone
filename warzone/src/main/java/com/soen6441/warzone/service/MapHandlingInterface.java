package com.soen6441.warzone.service;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.WarMap;
import java.io.IOException;

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
     * This method will store WarMap model into file
     *
     * @param p_warMap is the object of WarMap model
     * @return true if map is successfully write to the file
     */
    boolean writeMapToFile(WarMap p_warMap);

    /**
     * This method will read map file and store data into WarMap model object
     *
     * @param p_fileName fileName to read Map
     *
     * @return WarMap model
     * @throws java.io.IOException throws input/output exception
     */
    public WarMap readMap(String p_fileName) throws IOException;

    /**
     * This method is used to show the map in 2D matrix containing Countries as
     * x-y axis
     *@param d_warMap : object of map model
     * @return CommandResponse object
     */
    public CommandResponse showMap(WarMap d_warMap);
}
