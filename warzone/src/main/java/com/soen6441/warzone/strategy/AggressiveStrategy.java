package com.soen6441.warzone.strategy;

import com.soen6441.warzone.model.Country;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.model.Order;
import com.soen6441.warzone.model.OrderTypes;
import com.soen6441.warzone.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class is used for Implementing Aggressive Strategy of a Player.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class AggressiveStrategy extends Strategy {

    /**
     * This is a default constructor used to initializes a list to
     * add specific orders which are allowed in this strategy
     */
    public AggressiveStrategy() {
        List<OrderTypes> l_allowedOrders = new ArrayList<>();
        l_allowedOrders.add( OrderTypes.DEPLOY );
        l_allowedOrders.add( OrderTypes.ADVANCE );
        l_allowedOrders.add( OrderTypes.AIRLIFT );
        l_allowedOrders.add( OrderTypes.BOMB );
        this.setD_allowedOrders( l_allowedOrders );
    }

    /**
     * This is a parameterize constructor used to invoke Constructor of Strategy Class
     *
     * @param p_gameData GameData Object needed for the player GameData
     * @param p_player   Player Object on which Strategy being Applied
     */
    public AggressiveStrategy(GameData p_gameData, Player p_player) {
        super( p_gameData, p_player );

    }

    /**
     * This method is used to get Strongest Country of a Player
     *
     * @return Strongest Country
     */
    public Country moveFrom() {
        return getStrongestCountry();
    }

    /**
     * This method is used choose opponent's country randomly to attack from a strongest country
     *
     * @param p_isNeighbour to check whether it is a neighbour country or not
     * @param p_fromCountry country from where to attack
     * @return randomly selected opponent's country
     */
    public Country moveTo(boolean p_isNeighbour, Country p_fromCountry) {
        List<Country> l_opponentCountries = getOpponentCountries( p_isNeighbour, p_fromCountry );
        int l_toCountryIndex = generateUniqueRandomNumber( 0, l_opponentCountries.size() - 1 );
        return l_opponentCountries.get( l_toCountryIndex );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Order createOrder() {
        Country l_fromCountry;
        int l_noOfArmies, l_a;

        if (d_player.getD_issuedNoOfArmies() > 0 && d_player.getD_ownedCountries() != null && d_player.getD_ownedCountries().size() != 0) {
            l_noOfArmies = generateUniqueRandomNumber( 1, d_player.getD_issuedNoOfArmies() );
            d_player.getOrderProcessor().processOrder( "deploy " + moveFrom().getD_countryName() + " " + l_noOfArmies, d_gameData );
            d_player.setD_issuedNoOfArmies( d_player.getD_issuedNoOfArmies() - l_noOfArmies );
        } else {

            if (d_player.getD_ownedCountries() == null || d_player.getD_ownedCountries().size() == 0) {
                l_a = 8;
            } else {
                l_a = generateUniqueRandomNumber( 2, this.d_allowedOrders.size() + 1 );
            }
            switch (l_a) {
                case 2:
                    l_fromCountry = moveFrom();
                    l_noOfArmies = generateUniqueRandomNumber( 1, l_fromCountry.getD_noOfArmies() );
                    d_player.getOrderProcessor().processOrder( "advance " + l_fromCountry.getD_countryName() + " " + moveTo( true, l_fromCountry ).getD_countryName() + " " + l_noOfArmies, d_gameData );
                    break;
                case 3:
                    l_fromCountry = moveFrom();
                    l_noOfArmies = generateUniqueRandomNumber( 1, l_fromCountry.getD_noOfArmies() );
                    d_player.getOrderProcessor().processOrder( "airlift " + l_fromCountry.getD_countryName() + " " + moveTo( false, l_fromCountry ).getD_countryName() + " " + l_noOfArmies, d_gameData );
                    break;
                case 4:
                    l_fromCountry = moveFrom();
                    l_noOfArmies = generateUniqueRandomNumber( 1, l_fromCountry.getD_noOfArmies() );
                    d_player.getOrderProcessor().processOrder( "bomb " + moveTo( false, l_fromCountry ).getD_countryName(), d_gameData );
                    break;
                default:
                    d_player.getOrderProcessor().setOrderString( "done" );
                    d_player.getOrderProcessor().processOrder( "done", d_gameData );
                    break;
            }
        }
        return d_player.getOrderProcessor().getOrder();
    }

    /**
     * This method is used to get Strongest Country from owned countries of a Player.
     *
     * @return Strongest country
     */
    private Country getStrongestCountry() {
        Country l_strongestCountry = null;
        if (d_player.getD_ownedCountries() != null && d_player.getD_ownedCountries().size() != 0) {
            l_strongestCountry = d_player.getD_ownedCountries().get( 0 );
        }
        for (Country l_country : d_player.getD_ownedCountries()) {
            if (l_country.getD_noOfArmies() > l_strongestCountry.getD_noOfArmies()) {
                l_strongestCountry = l_country;
            }
        }
        return l_strongestCountry;
    }

    /**
     * This method is used to get a list of opponent countries
     *
     * @param p_isNeighbour to check whether it is a neighbour country or not
     * @param p_fromCountry country from where to attack
     * @return list of opponent's countries to attack
     */
    private List<Country> getOpponentCountries(boolean p_isNeighbour, Country p_fromCountry) {
        List<Country> l_defenderCountries = new ArrayList<Country>();
        for (Country l_country : getAvailableCountries( d_gameData.getD_warMap() )) {
            if (p_fromCountry != null && (!d_player.getD_ownedCountries().contains( l_country ))) {
                if (p_isNeighbour && p_fromCountry.getD_neighbourCountries().contains( l_country )) {
                    l_defenderCountries.add( l_country );
                } else {
                    l_defenderCountries.add( l_country );
                }
            } else {
                if (!d_player.getD_ownedCountries().contains( l_country )) {
                    l_defenderCountries.add( l_country );
                }
            }
        }
        return l_defenderCountries;
    }
}
