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
     * enum variable of order type used to select order
     */
     DEPLOY,ADVANCE,AIRLIFT,BOMB, BLOCKADE;
      /**
     * used to pick the random value of enum
     *
     * @param p_allowedOrder List of allowed ORder
     * @return returns the OrderTypes object
     */
    public static OrderTypes randomGameCard(List<OrderTypes> p_allowedOrder) {
        int l_pick = new Random().nextInt(p_allowedOrder.size());
        return OrderTypes.values()[l_pick];
    }
}
