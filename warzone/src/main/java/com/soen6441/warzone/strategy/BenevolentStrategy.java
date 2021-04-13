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
public class BenevolentStrategy extends Strategy {

    /**
     * This is a parameterize constructor used to invoke Constructor of Strategy Class
     * and it also initializes a list to add specific orders which are allowed in this
     * strategy
     *
     * @param p_gameData GameData Object needed for the player GameData
     * @param p_player Player Object on which Strategy being Applied
     */
    public BenevolentStrategy(GameData p_gameData, Player p_player) {
        super(p_gameData, p_player);
    }

    /**
     * This is a default constructor used to initializes a list to
     * add specific orders which are allowed in this strategy
     *
     */
    public BenevolentStrategy() {
        List<OrderTypes> l_allowedOrders = new ArrayList<>();
        l_allowedOrders.add(OrderTypes.DEPLOY);
        l_allowedOrders.add(OrderTypes.ADVANCE);
        l_allowedOrders.add(OrderTypes.AIRLIFT);
        l_allowedOrders.add(OrderTypes.BLOCKADE);
        l_allowedOrders.add(OrderTypes.DIPLOMACY);
        this.setD_allowedOrders(l_allowedOrders);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Order createOrder() {
        Country l_fromCountry;
        int l_noOfArmies,l_index;
        int a;
        if (d_player.getD_issuedNoOfArmies() > 0 && d_player.getD_ownedCountries()!=null && d_player.getD_ownedCountries().size()!=0) {
            l_noOfArmies = generateUniqueRandomNumber(1, d_player.getD_issuedNoOfArmies());
            d_player.getOrderProcessor().processOrder("deploy " + moveFromCountry().getD_countryName() + " " + l_noOfArmies, d_gameData);
            d_player.setD_issuedNoOfArmies(d_player.getD_issuedNoOfArmies() - l_noOfArmies);
        } else {
            if(d_player.getD_ownedCountries()==null && d_player.getD_ownedCountries().size()==0)
            {
                a=6;
            }
            else {
                a = generateUniqueRandomNumber(2, this.d_allowedOrders.size() + 1);
            }
            switch (a) {
                case 2:
                    l_fromCountry = moveFromCountry();
                    l_noOfArmies = generateUniqueRandomNumber(1, l_fromCountry.getD_noOfArmies());
                    d_player.getOrderProcessor().processOrder("advance " + l_fromCountry.getD_countryName() + " " + moveToCountry(l_fromCountry).getD_countryName() + " " + l_noOfArmies, d_gameData);
                    break;
                case 3:
                    l_fromCountry = moveFromCountry();
                    l_noOfArmies = generateUniqueRandomNumber(1, l_fromCountry.getD_noOfArmies());
                    d_player.getOrderProcessor().processOrder("airlift " + l_fromCountry.getD_countryName() + " " + moveToCountry(l_fromCountry).getD_countryName() + " " + l_noOfArmies, d_gameData);
                    break;
                case 4:
                    l_fromCountry = moveFromCountry();
                    d_player.getOrderProcessor().processOrder("blockade " + l_fromCountry.getD_countryName(), d_gameData);
                    break;
                case 5:
                    while(true) {
                        l_index = generateUniqueRandomNumber(0, d_gameData.getD_playerList().size() - 1);
                        if(!d_gameData.getD_playerList().get(l_index).getD_playerName().equalsIgnoreCase(d_player.getD_playerName()))
                        {
                            break;
                        }
                    }
                    d_player.getOrderProcessor().processOrder("negotiate " + d_gameData.getD_playerList().get(l_index).getD_playerName(), d_gameData);
                    break;
                case 6:
                    d_player.getOrderProcessor().setOrderString("done");
                    d_player.getOrderProcessor().processOrder("done", d_gameData);
                    break;
            }
        }
        return d_player.getOrderProcessor().getOrder();
    }
    /**
     * This method is used get random owned country to move army from that country
     *
     * @param p_country weakest country
     * @return randomly selected opponent's country
     */
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
        if(l_countryToList.size()>0) {
            l_country = l_countryToList.get(l_index);
        }
        if(l_country==null)
        {
            l_country=p_country;
        }
        return l_country;
    }
    /**
     * This method is used to get weakest Country of a Player
     *
     * @return weakest country
     */
    public Country moveFromCountry() {
        Country l_country=null;
        if(d_player.getD_ownedCountries()!=null && d_player.getD_ownedCountries().size()!=0)
        {
            l_country=d_player.getD_ownedCountries().get(0);
        }
        for(Country l_c:d_player.getD_ownedCountries())
        {
            if(l_c.getD_noOfArmies()<l_country.getD_noOfArmies())
            {
                l_country=l_c;
            }
        }
        return l_country;
    }

}
