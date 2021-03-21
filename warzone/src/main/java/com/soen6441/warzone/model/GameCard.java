package com.soen6441.warzone.model;

/**
 * This Enum is used for Managing Type of the card
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public enum GameCard {
    BOMB, BLOCKADE, AIRLIFT, DIPLOMACY;

    public GameCard commandToGameCardMapper(Order p_order) {
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
}
