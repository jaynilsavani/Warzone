package com.soen6441.warzone.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 *
 * This Class is used for storing and manipulating Player Information
 *
 * Three annotations (Getter,Setter, toString), you can see on the top of the
 * class are lombok dependencies to automatically generate getter, setter and
 * tostring method in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Getter
@Setter
@ToString
@Component
public class Player {

    /**
     * Stores the id of player
     */
    private int d_playerId;

    /**
     * Stores the name of player
     */
    private String d_playerName;

    /**
     * list of countries owned by the player
     */
    private List<Country> d_ownedCountries = new ArrayList<>();

    /**
     * list of orders of the Player
     */
    private List<Order> d_orders = new ArrayList<>();
    /**
     * reinforcementPool Of the player
     */
    private int d_noOfArmies;

    /**
     * index for country for execution of the command for source Country
     */
    private int d_currentFromCountry;

    /**
     * index for country for execution of the command
     */
    private int d_currentToCountry;
    /**
     * number of armies for the current command
     */
    private int d_currentNoOfArmiesToMove;

    /**
     * add the order to the list of orders
     */
    public void issue_order() {
        DeployOrder d_deplyOrder = new DeployOrder();
        d_deplyOrder.setD_CountryIndex(d_currentToCountry);
        d_deplyOrder.setD_noOfArmies(d_currentNoOfArmiesToMove);
        d_orders.add(d_deplyOrder);
    }

    /**
     *
     * @return The last order of order list
     */
    public Order next_order() {
        if (d_orders != null && !d_orders.isEmpty()) {
            Order d_lastOrder = d_orders.get(d_orders.size() - 1);
            d_orders.remove(d_lastOrder);
            return d_lastOrder;
        } else {
            return null;
        }
    }

}
