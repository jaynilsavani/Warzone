package com.soen6441.warzone.model;

import java.util.List;
import java.util.Random;

/**
 * This Enum is used for managing different types of orders
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public enum OrderTypes {
    /**
     * enum variable for DEPLOY order
     */
    DEPLOY,
    /**
     * enum variable for ADVANCE order
     */
    ADVANCE,
    /**
     * enum variable for AIRLIFT order
     */
    AIRLIFT,
    /**
     * enum variable for BOMB order
     */
    BOMB,
    /**
     * enum variable for BLOCKADE order
     */
    BLOCKADE,
      /**
     * enum variable for diplomacy(negotiate) order
     */
    DIPLOMACY;


    /**
     * This method is used to pick the random game card
     *
     * @param p_allowedOrder List of allowed Order
     * @return returns the OrderTypes object
     */
    public static OrderTypes randomGameCard(List<OrderTypes> p_allowedOrder) {
        int l_pick = new Random().nextInt( p_allowedOrder.size() );
        return OrderTypes.values()[l_pick];
    }
}
