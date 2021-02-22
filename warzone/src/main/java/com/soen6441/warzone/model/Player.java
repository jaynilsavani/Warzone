package com.soen6441.warzone.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private List<Order> d_orders = new ArrayList<Order>();
    /**
     * reinforcementPool Of the player
     */
    private int d_noOfArmies;

    /**
     * index for country for execution of the command for source Country
     */
    private int d_currentFromCountry;

    /**
     * Name for country for execution of the command
     */
    private String d_currentToCountry;
    /**
     * number of armies for the current command
     */
    private int d_currentNoOfArmiesToMove;

    /**
     * add the order to the list of orders
     */
    public void issue_order() {
        DeployOrder d_deplyOrder = new DeployOrder();
        d_deplyOrder.setD_CountryName(d_currentToCountry);
        d_deplyOrder.setD_noOfArmies(d_currentNoOfArmiesToMove);
        d_orders.add(d_deplyOrder);
    }

    /**
     *
     * @return The last order of order list
     */
    public Order next_order() {
        if (d_orders != null && !d_orders.isEmpty()) {
            Order d_lastOrder = d_orders.get(0);
            d_orders.remove(0);
            return d_lastOrder;
        } else {
            return null;
        }
    }

    public boolean hasOrder() {
        if (d_orders != null && !d_orders.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return d_noOfArmies == player.d_noOfArmies && d_currentFromCountry == player.d_currentFromCountry && d_currentNoOfArmiesToMove == player.d_currentNoOfArmiesToMove && d_playerName.equals(player.d_playerName) && Objects.equals(d_ownedCountries, player.d_ownedCountries) && Objects.equals(d_orders, player.d_orders) && Objects.equals(d_currentToCountry, player.d_currentToCountry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(d_playerName, d_ownedCountries, d_orders, d_noOfArmies, d_currentFromCountry, d_currentToCountry, d_currentNoOfArmiesToMove);
    }
}
