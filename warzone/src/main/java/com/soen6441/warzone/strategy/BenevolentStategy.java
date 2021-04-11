package com.soen6441.warzone.strategy;

import com.soen6441.warzone.model.*;

import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

/**
 *
 * This Class is used for Implementing Benevolent Strategy of a Player.A NoArgsConstructor
 * annotation top of the class is a lombok dependency to automatically generate default
 * Constructor in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class BenevolentStategy extends Strategy {

    /**
     * This is a parameterize constructor used to invoke Constructor of Strategy Class
     * and it also initializes a list to add specific orders which are allowed in this
     * strategy
     *
     * @param p_gameData GameData Object needed for the player GameData
     * @param p_player Player Object on which Strategy being Applied
     */
    public BenevolentStategy(GameData p_gameData, Player p_player) {
        super(p_gameData, p_player);
    }


    public BenevolentStategy() {
        List<OrderTypes> l_allowedOrders = new ArrayList<>();
        l_allowedOrders.add(OrderTypes.DEPLOY);
        l_allowedOrders.add(OrderTypes.ADVANCE);
        this.setD_allowedOrders(l_allowedOrders);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Order createOrder() {
        Country l_fromCountry;
        int l_noOfArmies;
        int l_turns=9;
        int a;
        if (d_player.getD_issuedNoOfArmies() > 0) {
            l_noOfArmies = generateUniqueRandomNumber(1, d_player.getD_issuedNoOfArmies());
            System.out.println(l_noOfArmies);
            d_player.getOrderProcessor().processOrder("deploy " + moveFromCountry().getD_countryName() + " " + l_noOfArmies, d_gameData);
            d_player.setD_issuedNoOfArmies(d_player.getD_issuedNoOfArmies() - l_noOfArmies);
        } else {
            int l_round=d_player.getD_orders().size();
            if(d_player.getD_orders().size()<l_turns)
            {
                a =2;
            }
            else
            {
                a =  generateUniqueRandomNumber(2, this.d_allowedOrders.size()+1);
            }
            switch (a) {
                case 2:
                    l_fromCountry = moveFromCountry();
                    l_noOfArmies = generateUniqueRandomNumber(1, l_fromCountry.getD_noOfArmies());
                    d_player.getOrderProcessor().processOrder("advance " + l_fromCountry.getD_countryName() + " " + moveToCountry(l_fromCountry).getD_countryName() + " " + l_noOfArmies, d_gameData);
                    break;
                default:
                    d_player.getOrderProcessor().setOrderString("done");
                    d_player.getOrderProcessor().processOrder("done", d_gameData);
                    break;
            }
        }
        return d_player.getOrderProcessor().getOrder();
    }

    public Country moveToCountry(Country p_country) {
        Country l_country=null;
        List<Country> l_countryToList=new ArrayList<>();
        for(Country l_c:d_player.getD_ownedCountries())
        {
            if(!l_c.getD_countryName().equalsIgnoreCase(p_country.getD_countryName()))
            {
                l_countryToList.add(l_c);
            }
        }
        int l_index=generateUniqueRandomNumber(0,l_countryToList.size()-1);
        l_country=l_countryToList.get(l_index);
        return l_country;
    }
    public Country moveFromCountry() {
        Country l_country=null;
        if(d_player.getD_ownedCountries()!=null && d_player.getD_ownedCountries().size()!=0)
        {
            l_country=d_player.getD_ownedCountries().get(0);
        }
        for(Country l_c:d_player.getD_ownedCountries())
        {
            System.out.println("**"+l_c.getD_noOfArmies());
            if(l_c.getD_noOfArmies()<l_country.getD_noOfArmies())
            {
                l_country=l_c;
                System.out.println("##"+l_country.getD_noOfArmies());
            }
        }
        return l_country;
    }
    private List<Country> getRandomCountries() {
        List<Country> l_randomCountries = new ArrayList<Country>();
        List<Country> l_getOpponentCountries = new ArrayList<>();
        int l_index = generateUniqueRandomNumber(0, d_player.getD_ownedCountries().size() - 1);
        l_randomCountries.add(d_player.getD_ownedCountries().get(l_index));
        Country l_moveFromCountry = l_randomCountries.get(0);
        Country l_moveToCountry = null;
        for (Country l_countries : getAvailableCountries(d_gameData.getD_warMap())) {
            if ((d_player.getD_ownedCountries().contains(l_countries) || l_moveFromCountry.getD_neighbourCountries().contains(l_countries.getD_countryName())) && (l_countries != l_moveFromCountry)) {
                l_getOpponentCountries.add(l_countries);
            }
        }
        l_index = generateUniqueRandomNumber(0, l_getOpponentCountries.size() - 1);
        l_randomCountries.add(l_getOpponentCountries.get(l_index));
        return l_randomCountries;
    }

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
