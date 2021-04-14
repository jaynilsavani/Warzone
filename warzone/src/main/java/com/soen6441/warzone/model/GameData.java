package com.soen6441.warzone.model;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;
import lombok.NoArgsConstructor;

/**
 * This Class is used for String and manipulating Game Play Information Three
 * annotations (Getter,Setter, toString), you can see on the top of the class
 * are lombok dependencies to automatically generate getter, setter and tostring
 * method in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Getter
@Setter
@ToString
@Component
@NoArgsConstructor
public class GameData {

    /**
     * It'll manage list of player in that particular game
     */
    private List<Player> d_playerList;

    /**
     * It'll store name of game phase i.e. startUp, reinforcement, issue orders
     * , execute orders etc.
     */
    private String d_gamePhase;

    /**
     * name of the map file
     */
    private String d_fileName;

    /**
     * Map object for current Game
     */
    private WarMap d_warMap;
    /**
     * number of turn in one game
     */
    private int d_maxNumberOfTurns;
    /**
     * index of the current player who is playing the game
     */
    private int d_currentPlayerId;
    /**
     * Winner Player Information
     */
    private Player d_winner;

    /**
     * current status of game
     */
    private String d_status;

    /**
     * mode of the game
     */
    private int d_gameMode;

    public GameData(GameData p_gameData) {

        this.d_gamePhase = p_gameData.d_gamePhase;
        this.d_fileName = p_gameData.d_fileName;
        this.d_warMap = p_gameData.d_warMap;
        this.d_maxNumberOfTurns = p_gameData.d_maxNumberOfTurns;
        this.d_currentPlayerId = p_gameData.d_currentPlayerId;
        this.d_winner = p_gameData.d_winner;
        this.d_status = p_gameData.d_status;
        this.d_playerList = new ArrayList<Player>();
        if (p_gameData.d_playerList != null) {
            p_gameData.d_playerList.forEach(player -> {
                this.d_playerList.add(player);
            });
        }
    }

}
