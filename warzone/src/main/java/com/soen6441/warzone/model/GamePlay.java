package com.soen6441.warzone.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 *
 * This Class is used for String and manipulating Game Play Information 
 * Three annotations (Getter,Setter, toString), you can see on the top of the class
 * are lombok dependencies to automatically generate getter, setter and tostring
 * method in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Getter
@Setter
@ToString
@Component
public class GamePlay {

    /**
     * It'll manage list of player in that particular game
     */
    private List<Player> playerList;

    /**
     * It'll store name of game phase i.e. startUp, reinforcement, issue orders
     * , execute orders etc.
     */
    private String gamePhase;

    /**
     * name of the map file
     */
    private String fileName;

    /**
     * Map object for current Game
     */
    private WarMap d_warMap;
    /**
     * number of turn in one game
     */
    private int maxNumberOfTurns;
    /**
     * Winner player name
     */
    /**
     * index of the current player who is playing the game
     */
    private int currentPlayerId;
    /**
     * Winner Player Information
     */
    private Player winner;

    /**
     * current status of game
     */
    private String status;

}
