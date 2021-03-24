package com.soen6441.warzone.model;

import com.soen6441.warzone.service.OrderProcessor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This Class is used for storing and manipulating Player Information
 * <p>
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

    @Autowired
    OrderProcessor orderProcessor;

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
     * List of the Cards
     */
    private List<GameCard> d_cards = new ArrayList<GameCard>();
    /**
     * reinforcementPool Of the player
     */
    private int d_noOfArmies;

    /**
     * Name country for execution of the command for source Country
     */
    private String d_currentFromCountry;

    /**
     * Name for country for execution of the command
     */
    private String d_currentToCountry;
    /**
     * number of armies for the current command
     */
    private int d_currentNoOfArmiesToMove;

    /**
     * number of the command to set the order f that type
     */
    private String d_commandtype;
    /**
     * This is used to set Whether player won the battle or not in the current
     * round
     */
    private boolean d_isWinner;
    /**
     * Name of the player with current player wants to negotiate
     */
    private String d_negotiatePlayer;

    /**
     * List of the player with current player wants to negotiate
     */
    private List<Player> d_negotiatePlayerList;

    /**
     * add the order to the list of orders
     *
     */
    public void issue_order() {
        Order l_orderObj = orderProcessor.getOrder();
        l_orderObj.d_player = this;
        d_orders.add(l_orderObj);
    }

    /**
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

    /**
     * This is used to add the card in the player's list of the card
     *
     * @param p_gameCard GameCard
     */
    public void addCard(GameCard p_gameCard) {
        if (d_cards == null) {
            d_cards = new ArrayList<>();
        }
        d_cards.add(p_gameCard);
    }

    /**
     * used to check whether the player has a card or not
     * @param p_gameCard object having the list of card of the layer
     * @return true/false based on whether player has card or not
     */
    public boolean hasCard(GameCard p_gameCard) {
        return d_cards.contains(p_gameCard);
    }

    /**
     *
     * @param p_gameCard
     * @return
     */
    public boolean removeCard(GameCard p_gameCard) {
        if (d_cards != null && (!d_cards.isEmpty())) {
            return d_cards.remove(p_gameCard);
        }
        return false;
    }

    /**
     * checks if player has an order or not
     *
     * @return results in form of true/false
     */
    public boolean hasOrder() {
        return d_orders != null && !d_orders.isEmpty();
    }

    @Override
    public boolean equals(Object l_obj) {
        if (this == l_obj) {
            return true;
        }
        if (l_obj == null || getClass() != l_obj.getClass()) {
            return false;
        }
        Player l_player = (Player) l_obj;
        return d_noOfArmies == l_player.d_noOfArmies && d_currentFromCountry == l_player.d_currentFromCountry && d_currentNoOfArmiesToMove == l_player.d_currentNoOfArmiesToMove && d_playerName.equals(l_player.d_playerName) && Objects.equals(d_ownedCountries, l_player.d_ownedCountries) && Objects.equals(d_orders, l_player.d_orders) && Objects.equals(d_currentToCountry, l_player.d_currentToCountry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(d_playerName, d_ownedCountries, d_orders, d_noOfArmies, d_currentFromCountry, d_currentToCountry, d_currentNoOfArmiesToMove);
    }
}
