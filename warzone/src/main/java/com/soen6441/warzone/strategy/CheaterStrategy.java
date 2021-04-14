package com.soen6441.warzone.strategy;

import com.soen6441.warzone.model.Country;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.model.Order;
import com.soen6441.warzone.model.OrderTypes;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.model.Continent;

import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This Class is used for Implementing Cheater Strategy of a Player. A
 * NoArgsConstructor annotation top of the class is a lombok dependency to
 * automatically generate default Constructor in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@NoArgsConstructor
@Getter
@Setter
public class CheaterStrategy extends Strategy {
    /**
     * maximum number of turn allowed
     */
    public static int d_noOfTurns = 10;

    /**
     * This is a parameterize constructor used to invoke Constructor of Strategy
     * Class and it also initializes a list to add specific orders which are
     * allowed in this strategy
     *
     * @param p_gameData GameData Object needed for the player GameData
     * @param p_player   Player Object on which Strategy being Applied
     */
    public CheaterStrategy(GameData p_gameData, Player p_player) {
        super( p_gameData, p_player );
        List<OrderTypes> l_allowedOrders = new ArrayList<>();
        l_allowedOrders.add( OrderTypes.DEPLOY );
        l_allowedOrders.add( OrderTypes.ADVANCE );
        l_allowedOrders.add( OrderTypes.AIRLIFT );
        l_allowedOrders.add( OrderTypes.BOMB );
        this.setD_allowedOrders( l_allowedOrders );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Order createOrder() {
        if (CheaterStrategy.d_noOfTurns > 0) {
            Set<String> l_allNeighorsName = new HashSet<String>();
            Set<Country> l_allNeighors = new HashSet<Country>();
            //Get all Neighbours Countries
            d_player.getD_ownedCountries().stream().map( l_country -> l_allNeighorsName.addAll( l_country.getD_neighbourCountries() ) ).collect( Collectors.toSet() );

            for (String l_neighbour : l_allNeighorsName) {
                l_allNeighors.add( getCountryByCountryName( l_neighbour ) );
            }
            for (Country l_country : l_allNeighors) {
                if (d_player.getD_ownedCountries().contains( l_country )) {
                } else {
                    //Remove from another player 
                    for (Player l_player : d_gameData.getD_playerList()) {
                        if (l_player.getD_ownedCountries().contains( l_country )) {
                            l_player.getD_ownedCountries().remove( l_country );
                            break;
                        }
                    }
                    //Add to owned list
                    d_player.getD_ownedCountries().add( l_country );
                }
            }
            //DoubleArmies in Outsider Countries
            for (Country l_country : d_player.getD_ownedCountries()) {
                if (isEnemyNeighbour( l_country )) {
                    l_country.setD_noOfArmies( l_country.getD_noOfArmies() * 2 );
                }
            }
            d_player.getOrderProcessor().setOrderString( "Cheater Player Order" );
            d_player.getOrderProcessor().processOrder( "cheater", d_gameData );
            CheaterStrategy.d_noOfTurns--;
            return null;
        } else {
            d_player.getOrderProcessor().setOrderString( "done" );
            d_player.getOrderProcessor().processOrder( "done", d_gameData );
        }
        return null;
    }

    /**
     * This method is used to check whether given neighbour country is opponent's
     * country or not.
     *
     * @param p_country owned country
     * @return true or false based on checking the neighbour condition
     */
    public boolean isEnemyNeighbour(Country p_country) {
        for (Country l_country : d_player.getD_ownedCountries()) {
            if (!l_country.getD_neighbourCountries().contains( p_country.getD_countryName() )) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method is used get country by country name
     *
     * @param p_countryName string having the name of county
     * @return gives the country object from the given name
     */
    private Country getCountryByCountryName(String p_countryName) {
        for (Map.Entry<Integer, Continent> l_entry : d_gameData.getD_warMap().getD_continents().entrySet()) {
            Continent l_continent = l_entry.getValue();
            //getting all countries from the continents
            List<Country> l_countryList = l_continent.getD_countryList();
            if (l_countryList != null) {
                for (Country l_country : l_countryList) {
                    if (l_country != null) {
                        //comparing the county name with given l_country name
                        if (p_countryName.equalsIgnoreCase( l_country.getD_countryName() )) {
                            return l_country;
                        }
                    }
                }
            }
        }
        return null;
    }
}
