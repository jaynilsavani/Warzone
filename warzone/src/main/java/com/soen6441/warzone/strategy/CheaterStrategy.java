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
 * This Class is used for Implementing Cheater Strategy of a Player. A NoArgsConstructor
 * annotation top of the class is a lombok dependency to automatically generate default
 * Constructor in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@NoArgsConstructor
public class CheaterStrategy extends Strategy {

    /**
     * This is a parameterize constructor used to invoke Constructor of Strategy Class
     * and it also initializes a list to add specific orders which are allowed in this
     * strategy
     *
     * @param p_gameData GameData Object needed for the player GameData
     * @param p_player Player Object on which Strategy being Applied
     */
    public CheaterStrategy(GameData p_gameData, Player p_player) {
        super(p_gameData, p_player);
        List<OrderTypes> l_allowedOrders = new ArrayList<>();
        l_allowedOrders.add(OrderTypes.DEPLOY);
        l_allowedOrders.add(OrderTypes.ADVANCE);
        l_allowedOrders.add(OrderTypes.AIRLIFT);
        l_allowedOrders.add(OrderTypes.BOMB);
        this.setD_allowedOrders(l_allowedOrders);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Order createOrder() {
        //implement create Order logic 

        return null;
    }

}
