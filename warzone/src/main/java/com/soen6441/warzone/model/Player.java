package com.soen6441.warzone.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private int d_commandtype;

    /**
     * nName of the player with current player wants to negotiate
     */
    private String d_negotiatePlayer;

    /**
     * add the order to the list of orders
     */
    public void issue_order() {
        switch (d_commandtype)
        {
            case 1:
                {
                DeployOrder l_deplyOrder = new DeployOrder();
                l_deplyOrder.setD_CountryName(d_currentToCountry);
                l_deplyOrder.setD_noOfArmies(d_currentNoOfArmiesToMove);
                d_orders.add(l_deplyOrder);
                break;
                }
            case 2:
                {
                    AdvanceOrder l_advanceOrder=new AdvanceOrder();
                    l_advanceOrder.setD_CountryNameFrom(d_currentFromCountry);
                    l_advanceOrder.setD_CountryNameTo(d_currentToCountry);
                    l_advanceOrder.setD_noOfArmies(d_currentNoOfArmiesToMove);
                    d_orders.add(l_advanceOrder);
                    break;
                }
            case 3:
                {
                    BombOrder l_bombOrder=new BombOrder();
                    l_bombOrder.setD_countryName(d_currentToCountry);
                    d_orders.add(l_bombOrder);
                    break;
                }
            case 4:
                {
                    BlockadeOrder l_blockadeOrder=new BlockadeOrder();
                    l_blockadeOrder.setD_countryName(d_currentToCountry);
                    d_orders.add(l_blockadeOrder);
                    break;
                }
            case 5:
                {
                    AirliftOrder l_airliftOrder=new AirliftOrder();
                    l_airliftOrder.setD_sourceCountry(d_currentFromCountry);
                    l_airliftOrder.setD_targetCountry(d_currentToCountry);
                    l_airliftOrder.setD_noOfArmies(d_currentNoOfArmiesToMove);
                    d_orders.add(l_airliftOrder);
                    break;
                }
            case 6:
                {
                    NegotiateOrder l_negotiateOrder=new NegotiateOrder();
                    l_negotiateOrder.setD_playerName(d_negotiatePlayer);
                    d_orders.add(l_negotiateOrder);
                    break;
                }

        }
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
     * checks if player has an order or not
     *
     * @return results in form of true/false
     */
    public boolean hasOrder() {
        return d_orders != null && !d_orders.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return d_noOfArmies == player.d_noOfArmies && d_currentFromCountry == player.d_currentFromCountry && d_currentNoOfArmiesToMove == player.d_currentNoOfArmiesToMove && d_playerName.equals(player.d_playerName) && Objects.equals(d_ownedCountries, player.d_ownedCountries) && Objects.equals(d_orders, player.d_orders) && Objects.equals(d_currentToCountry, player.d_currentToCountry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(d_playerName, d_ownedCountries, d_orders, d_noOfArmies, d_currentFromCountry, d_currentToCountry, d_currentNoOfArmiesToMove);
    }
}
