package com.soen6441.warzone.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class is used for The negotiate order Command Three annotations
 * (Getter,Setter, toString), you can see on the top of the class are lombok
 * dependencies to automatically generate getter, setter and tostring method in
 * the code.
 *
 * @author <a href="mailto:manthan.p.moradiya@gmail.com">Manthan Moradiya</a>
 */
@Getter
@Setter
@ToString
public class NegotiateOrder extends Order {
    /**
     * Player in this order with whom current player wants to negotiate
     */
    private String d_playerName;

    /**
     * No of mandatory fields It always needs to have after all necessary fields
     */
    public int d_mandatoryField = 1;

    @Override
    public boolean executeOrder() {
        Player l_player = this.getPlayerFromName(d_playerName);
        if (l_player != null) {
            if (addPlayerToNegotiateList(d_player, l_player) && addPlayerToNegotiateList(l_player, d_player)) {
                return true;
            }
        }

        return false;
    }

    /**
     * This method will add player into negotiate player list
     *
     * @param p_orderPlayer     player in which negotiate player will be added
     * @param p_negotiatePlayer player to be added in negotiate player list
     * @return return false if exception occurs otherwise true
     */
    public boolean addPlayerToNegotiateList(Player p_orderPlayer, Player p_negotiatePlayer) {
        try {
            // set neighbour into player object
            if (p_orderPlayer.getD_negotiatePlayerList() != null) {
                p_orderPlayer.getD_negotiatePlayerList().add(p_negotiatePlayer);
            } else {
                List<Player> l_playerList = new ArrayList();
                l_playerList.add(p_negotiatePlayer);
                p_orderPlayer.setD_negotiatePlayerList(l_playerList);
            }

            // set player in game data object
            List<Integer> l_playerIndex = new ArrayList<>();
            for (Player l_gamePlayer : d_gameData.getD_playerList()) {
                if (l_gamePlayer.getD_playerName() == p_orderPlayer.getD_playerName()) {
                    l_playerIndex.add(d_gameData.getD_playerList().indexOf(l_gamePlayer));
                    break;
                }
            }
            if (!l_playerIndex.isEmpty()) {
                d_gameData.getD_playerList().set(l_playerIndex.get(0), p_orderPlayer);
            }

            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * @param p_playerName County name in the Command
     * @return validity Of Command
     */
    public boolean validateAndSetData(String p_playerName) {
        if (!p_playerName.isEmpty()) {
            this.setD_playerName(p_playerName);
            return true;
        }
        return false;
    }

    /**
     * This method will return player object from player name
     *
     * @param p_playerName find player name from player list
     * @return player object
     */
    public Player getPlayerFromName(String p_playerName) {
        Player l_player = null;
        for (Player l_gamePlayer : d_gameData.getD_playerList()) {
            if (p_playerName.equalsIgnoreCase(l_gamePlayer.getD_playerName())) {
                l_player = l_gamePlayer;
            }
        }

        return l_player;
    }
}
