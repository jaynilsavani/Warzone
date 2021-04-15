package com.soen6441.warzone.model;

import static com.soen6441.warzone.model.Strategies.strategyToObjectMapper;
import static com.soen6441.warzone.model.Strategies.stringToStrategyMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.soen6441.warzone.state.IssueOrderPhase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * This Class is used for Creating Tournament Object
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Getter
@Setter

public class Tournament {
    
    private List<WarMap> d_maps;
    private List<Player> d_players;
    private int d_noOfGames;
    private int d_maxnoOfTurns;
    private boolean d_status;
    private List<GameData> d_gamedatas;
    
    private ArrayList<ArrayList<String>> d_winners = new ArrayList<ArrayList<String>>();

    /**
     * This method is used to save winner list
     */
    public void setWinnerList()
    {
        for(int l_map=0;l_map<d_maps.size();l_map++)
        {
            ArrayList<String> l_t=new ArrayList<String>();
            for(int l_game=0;l_game<d_noOfGames;l_game++)
            {
                l_t.add(l_game,"DRAW");

            }
            d_winners.add(l_t);
        }
//        System.out.println(d_winners);
    }

    /**
     * This method is used to declare winner
     *
     * @param p_mapNo map number
     * @param p_gameNo game number
     * @param p_name player name
     * @return boolean value
     */
    public boolean declareWinner(int p_mapNo, int p_gameNo,String p_name) {
        d_winners.get(p_mapNo).remove(p_gameNo);
        d_winners.get(p_mapNo).add(p_gameNo,p_name);
        return true;
    }
    
}
