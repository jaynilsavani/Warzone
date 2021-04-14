package com.soen6441.warzone.config;

/**
 *
 * This Class is used for storing Constant of Game Engine used in the warzone
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class WarzoneConstants {

    //Main Game Loop Constants
    public static final int DEFAULT_ASSIGN_REINFORCEMENT_INITIAL = 3;
    public static final int DEFAULT_ASSIGN_REINFORCEMENT_DIVIDER = 3;
    public static final int DEFAULT_ASSIGN_REINFORCEMENT_IN_TURN = 5;
    //Constants of Map Editor
    public static final String MAP_DEF_PATH = "./src/main/resources/maps/";
    public static final String GAME_DEF_PATH = "./src/main/resources/maps/";
    public static final String NAME = "name";
    public static final String FILES = "[files]";
    public static final String MAP = "[Map]";
    public static final String CONTINENTS = "[continents]";
    public static final String COUNTRIES = "[countries]";
    public static final String TERRITORIES = "[territories]";
    public static final String BORDERS = "[borders]";
    public static final String PLAYERS = "[players]";
    public static final String OWNED_COUNTRIES = "[owned_countries]";
    public static final String ORDERS = "[orders]";
    public static final String CARDS = "[cards]";
    public static final String NEGOTIATE_PLAYERS = "[negotiate_players]";
    public static final String PLAYER_FLAG = "[player_flag]";
    public static final String GAME_PHASE = "[game_phase]";
    public static final String MAP_NAME = "[map_name]";

    public static final String PHASE_MAP = "MAP";
    public static final String PHASE_GAME_START_UP = "GAMESTARTUP";
    public static final String PHASE_ISSUE_ORDER = "ISSUE_ORDER";
    public static final String PHASE_EXECUTE = "EXECUTE";

}
