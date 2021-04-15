package com.soen6441.warzone.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * This Class is used for Storing and manipulating tournament Information Three
 * annotations (Getter,Setter, toString), you can see on the top of the class
 * are lombok dependencies to automatically generate getter, setter and tostring
 * method in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Getter
@Setter
public class Tournament {

    /**
     * List of maps
     */
    private List<WarMap> d_maps;
    /**
     * List of players
     */
    private List<Player> d_players;
    /**
     * number of games
     */
    private int d_noOfGames;
    /**
     * maximum number of turns
     */
    private int d_maxnoOfTurns;
    /**
     * status of tournament
     */
    private boolean d_status;
    /**
     * List of gamedata
     */
    private List<GameData> d_gamedatas;

    /**
     * matrix of wineers
     */
    private ArrayList<ArrayList<String>> d_winners = new ArrayList<ArrayList<String>>();

    /**
     * This method is used to save winner list
     */
    public void setWinnerList() {
        for (int l_map = 0; l_map < d_maps.size(); l_map++) {
            ArrayList<String> l_t = new ArrayList<String>();
            for (int l_game = 0; l_game < d_noOfGames; l_game++) {
                l_t.add(l_game, "DRAW");

            }
            d_winners.add(l_t);
        }
    }

    /**
     * This method is used to declare winner
     *
     * @param p_mapNo map number
     * @param p_gameNo game number
     * @param p_name player name
     * @return boolean value
     */
    public boolean declareWinner(int p_mapNo, int p_gameNo, String p_name) {
        d_winners.get(p_mapNo).remove(p_gameNo);
        d_winners.get(p_mapNo).add(p_gameNo, p_name);
        return true;
    }

}
