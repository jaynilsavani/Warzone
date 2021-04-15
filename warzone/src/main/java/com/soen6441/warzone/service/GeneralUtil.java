package com.soen6441.warzone.service;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.WarMap;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * This interface is used for Utility of General Functions and GeneralUtilImpl
 * is the implementation of it.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public interface GeneralUtil {

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
     * This method will prepare response of command entered by user
     *
     * @param p_isValid to check command is successfully executed or not
     * @param p_responeMessage response message of command
     */
    public void prepareResponse(boolean p_isValid, String p_responeMessage);

    /**
     * This method will prepare response of command entered by user
     *
     * @return command Response set by prepareResponse
     */
    public CommandResponse getResponse();

    /**
     * This method is used to check map is connected or not
     *
     * @param p_metricesOfMap metric of map
     * @param p_noOfCountries total number of map
     * @return returns true if map is connected
     */
    public boolean isConnected(int[][] p_metricesOfMap, int p_noOfCountries);

    /**
     * This method is used to traverse metric
     *
     * @param p_source : source node to check graph is connected or not
     * @param p_visited : boolean array to store visited node
     * @param p_noOfCountries : total number of countries in map
     * @param p_metricesOfMap : metrics of map
     */
    public void traverse(int p_source, boolean[] p_visited, int p_noOfCountries, int[][] p_metricesOfMap);

    /**
     * List all files from this given path and extension
     *
     * @param p_path directory path of files
     * @param p_fileExtension extension of file to be searched
     * @return list of files
     * @throws java.io.IOException throws input/output exception
     */
    public List<String> getListOfAllFiles(Path p_path, String p_fileExtension) throws IOException;

    /**
     * This method is will used to get all available map files
     *
     * @return it will return list of map file
     * @throws java.io.IOException throws IO Exception
     */
    public List<String> getAvailableMapFiles() throws IOException;

    /**
     * convert string into title case
     *
     * @param p_input input String
     * @return TitleCase String
     */
    public String toTitleCase(String p_input);

    /**
     * read map by type(conquest, domination)
     *
     * @param p_fileName Name of the file to read
     * @return Object of the WarMap that is read from the Given File
     * @throws IOException
     */
    public WarMap readMapByType(String p_fileName) throws IOException;

    /**
     * write map from warmap object
     *
     * @param p_warMap WarMap object to write into file
     * @param p_isConquest Whether Given Object needs to save as Conquest Format
     * or not
     * @return Whether File is being saved or not
     * @throws IOException
     */
    public boolean writeMapByType(WarMap p_warMap, boolean p_isConquest) throws IOException;
    /**
     * generate unique random number
     *
     * @param p_startNumber starting number of the Series
     * @param p_endNumber Ending Number of the Series
     * @return Random Number
     */
    public int uniqueRandomNumberGenerate(int p_startNumber, int p_endNumber);
}
