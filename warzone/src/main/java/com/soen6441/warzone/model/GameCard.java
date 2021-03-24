package com.soen6441.warzone.model;

import java.util.Random;

/**
 * This Enum is used for Managing Type of the card
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public enum GameCard {
    /**
     * enum variable of cards used to add in players card list
     */
    BOMB, BLOCKADE, AIRLIFT, DIPLOMACY;

    /**
     * used to add the mapping of card to command
     * @param p_order order from the players order list
     * @return returns the gamecard object with the card related to the order
     */
    public static GameCard commandToGameCardMapper(Order p_order) {
        if (p_order instanceof AirliftOrder) {
            return AIRLIFT;
        } else if (p_order instanceof BlockadeOrder) {
            return BLOCKADE;
        } else if (p_order instanceof BombOrder) {
            return BOMB;
        } else if (p_order instanceof NegotiateOrder) {
            return DIPLOMACY;
        }
        return null;
    }

    /**
     * used to pick the random value of enum
     * @return returns the gamecard object with the card related
     */
    public static GameCard randomGameCard() {
        int l_pick = new Random().nextInt(GameCard.values().length);
        return GameCard.values()[l_pick];
    }
}
