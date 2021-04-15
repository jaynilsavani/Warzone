package com.soen6441.warzone.service;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.model.Order;

/**
 * This Class is used to process the order based on user input
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public interface OrderProcessor {

    /**
     * This method is used to process the user order
     *
     * @param p_orderCommand string of issue order
     * @param p_gameData data used for manipulation of string
     * @return result of issuing order execution
     */
    public CommandResponse processOrder(String p_orderCommand, GameData p_gameData);

    /**
     * Used to replay the order from player's list
     *
     * @return return the order
     */
    public Order getOrder();
    /**
     * returns order string
     *
     * @return order string
     */
    public String getOrderString();
    
     /**
     * set order string
      *
     * @param p_order order string
     */
    public void setOrderString(String p_order);

}
