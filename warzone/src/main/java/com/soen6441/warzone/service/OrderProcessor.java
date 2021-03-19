package com.soen6441.warzone.service;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.model.Order;

/**
 *
 * This Class is used for
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public interface OrderProcessor {

    /**
     * 
     * @param p_orderCommand
     * @param p_gameData
     * @return 
     */
    public CommandResponse processOrder(String p_orderCommand, GameData p_gameData);

    /**
     * 
     * @return 
     */
    public Order getOrder();

}
