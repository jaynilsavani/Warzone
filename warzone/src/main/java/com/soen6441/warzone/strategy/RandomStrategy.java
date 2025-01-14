package com.soen6441.warzone.strategy;

import com.soen6441.warzone.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class is used for Implementing Random Strategy of a Player. A NoArgsConstructor
 * annotation top of the class is a lombok dependency to automatically generate default
 * Constructor in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */

public class RandomStrategy extends Strategy {

    /**
     * This is a default constructor used to initializes a list to
     * add specific orders which are allowed in this strategy
     *
     */
    public RandomStrategy() {

        List<OrderTypes> l_allowedOrders = new ArrayList<>();
        l_allowedOrders.add(OrderTypes.DEPLOY);
        l_allowedOrders.add(OrderTypes.ADVANCE);
        l_allowedOrders.add(OrderTypes.AIRLIFT);
        l_allowedOrders.add(OrderTypes.BOMB);
        l_allowedOrders.add(OrderTypes.BLOCKADE);
        l_allowedOrders.add(OrderTypes.DIPLOMACY);
        this.setD_allowedOrders(l_allowedOrders);
    }

    /**
     * This is a parameterize constructor used to invoke Constructor of Strategy Class
     * and it also initializes a list to add specific orders which are allowed in this
     * strategy
     *
     * @param p_gameData GameData Object needed for the player GameData
     * @param p_player   Player Object on which Strategy being Applied
     */
    public RandomStrategy(GameData p_gameData, Player p_player) {
        super(p_gameData, p_player);

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Order createOrder() {
        Country l_fromCountry;
        int l_noOfArmies, l_index;
        int l_orderChoice;
        if (d_player.getD_orders().size() > 3) {
            l_orderChoice = generateUniqueRandomNumber(1, this.d_allowedOrders.size() + 1);
        } else {
            l_orderChoice = generateUniqueRandomNumber(1, this.d_allowedOrders.size());
        }
        List<Country> l_randomCountries = getRandomCountries();
        if(l_randomCountries==null || l_randomCountries.size()==0 || d_player.getD_ownedCountries().size()>3 )
        {
            l_orderChoice =7;
        }
        switch (l_orderChoice) {
            case 1:
                l_noOfArmies = generateUniqueRandomNumber(1, d_player.getD_noOfArmies());
                d_player.getOrderProcessor().processOrder("deploy " + l_randomCountries.get(0).getD_countryName() + " " + l_noOfArmies, d_gameData);
                break;
            case 2:
                if(l_randomCountries.size()==2) {
                    l_noOfArmies = generateUniqueRandomNumber(1, l_randomCountries.get(0).getD_noOfArmies());
                    d_player.getOrderProcessor().processOrder("advance " + l_randomCountries.get(0).getD_countryName() + " " + l_randomCountries.get(1).getD_countryName() + " " + l_noOfArmies, d_gameData);
                    break;
                }
            case 3:
                if(l_randomCountries.size()==2) {
                    l_noOfArmies = generateUniqueRandomNumber(1, l_randomCountries.get(0).getD_noOfArmies());
                    d_player.getOrderProcessor().processOrder("airlift " + l_randomCountries.get(0).getD_countryName() + " " + l_randomCountries.get(1).getD_countryName() + " " + l_noOfArmies, d_gameData);
                    break;
                }
            case 4:
                List<Country> l_opponentCountries=getOpponentCountries();
                l_index=generateUniqueRandomNumber(0,l_opponentCountries.size()-1);
                d_player.getOrderProcessor().processOrder("bomb " + l_opponentCountries.get(l_index).getD_countryName(), d_gameData);
                break;
            case 5:
                l_index = generateUniqueRandomNumber(0, d_player.getD_ownedCountries().size() - 1);
                d_player.getOrderProcessor().processOrder("blockade " + d_player.getD_ownedCountries().get(l_index).getD_countryName(), d_gameData);
                break;
            case 6:
                while(true) {
                l_index = generateUniqueRandomNumber(0, d_gameData.getD_playerList().size() - 1);
                if(!d_gameData.getD_playerList().get(l_index).getD_playerName().equalsIgnoreCase(d_player.getD_playerName()))
                        {
                            break;
                        }
                      }
                d_player.getOrderProcessor().processOrder("negotiate " + d_gameData.getD_playerList().get(l_index).getD_playerName(), d_gameData);
                break;
            case 7:
                d_player.getOrderProcessor().setOrderString("done");
                d_player.getOrderProcessor().processOrder("done", d_gameData);
                break;
        }
        return d_player.getOrderProcessor().getOrder();
    }

    /**
     * This method is used to get list of random countries deploy or attack
     *
     * @return list of random countries
     */
    private List<Country> getRandomCountries() {
        List<Country> l_randomCountries = new ArrayList<Country>();
        List<Country> l_getOpponentCountries = new ArrayList<>();
        if(d_player.getD_ownedCountries()!=null && d_player.getD_ownedCountries().size()!=0) {
            int l_index = generateUniqueRandomNumber(0, d_player.getD_ownedCountries().size() - 1);
            l_randomCountries.add(d_player.getD_ownedCountries().get(l_index));
            Country l_moveFromCountry = l_randomCountries.get(0);
            for (Country l_countries : getAvailableCountries(d_gameData.getD_warMap())) {
                if ((d_player.getD_ownedCountries().contains(l_countries) || l_moveFromCountry.getD_neighbourCountries().contains(l_countries.getD_countryName())) && (l_countries != l_moveFromCountry)) {
                    l_getOpponentCountries.add(l_countries);
                }
            }
            if (l_getOpponentCountries.size() > 1) {
                l_index = generateUniqueRandomNumber(0, l_getOpponentCountries.size() - 1);
                l_randomCountries.add(l_getOpponentCountries.get(l_index));
            }
        }
        return l_randomCountries;
    }

    /**
     * This method is used to get list of opponent's country
     *
     * @return list of opponent country
     */
    private List<Country> getOpponentCountries() {
        List<Country> l_getOpponentCountries = new ArrayList<>();
        for (Country l_countries : getAvailableCountries(d_gameData.getD_warMap())) {
            if ((!d_player.getD_ownedCountries().contains(l_countries))) {
                l_getOpponentCountries.add(l_countries);
            }
        }
        return l_getOpponentCountries;
    }

}
