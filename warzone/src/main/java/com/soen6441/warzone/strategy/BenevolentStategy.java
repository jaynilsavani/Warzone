package com.soen6441.warzone.strategy;

import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.model.Order;
import com.soen6441.warzone.model.OrderTypes;
import com.soen6441.warzone.model.Player;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

/**
 *
 * This Class is used for Implementing BenevolentStategy
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@NoArgsConstructor
public class BenevolentStategy extends Strategy {
 
    public BenevolentStategy(GameData p_gameData, Player p_player) {
        super(p_gameData, p_player);
        List<OrderTypes> l_allowedOrders = new ArrayList<>();
        l_allowedOrders.add(OrderTypes.DEPLOY);
        l_allowedOrders.add(OrderTypes.ADVANCE);
        l_allowedOrders.add(OrderTypes.AIRLIFT);
        l_allowedOrders.add(OrderTypes.BOMB);
        this.setD_allowedOrders(l_allowedOrders);
    }

    @Override
    public Order createOrder() {
        //implement create Order logic 
        return null;
    }

}
