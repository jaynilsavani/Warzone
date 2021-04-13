package com.soen6441.warzone.model;

import static com.soen6441.warzone.model.Strategies.strategyToObjectMapper;
import static com.soen6441.warzone.model.Strategies.stringToStrategyMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
@NoArgsConstructor
public class Tournament {
    
    private List<WarMap> d_maps;
    private List<Player> d_players;
    private int d_noOfGames;
    private int d_maxnoOfTurns;
    private boolean d_status;
    private List<GameData> d_gamedatas;
    
    private ArrayList<ArrayList<TournamentWinner>> d_winners = new ArrayList<ArrayList<TournamentWinner>>();
    
   
    
    public boolean declareWinner(int p_mapNo, int p_gameNo) {
        return false;
    }
    
}
