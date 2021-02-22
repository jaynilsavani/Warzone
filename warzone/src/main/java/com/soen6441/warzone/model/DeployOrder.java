package com.soen6441.warzone.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * This Class is used for The deploy order Command 
 * Three annotations (Getter,Setter, toString), you can see on the top of the class are lombok
 * dependencies to automatically generate getter, setter and tostring method in
 * the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Getter
@Setter
@ToString
@Component
public class DeployOrder implements Order {

    private int d_noOfArmies;
    private String d_CountryName;
    private Player d_player;



    /**
     * {@inheritDoc }
     *
     * This method deploy armies to the country
     */
    @Override
    public boolean executeOrder() {
        for(Country l_country:d_player.getD_ownedCountries())
        {
            if(l_country.getD_countryName().equalsIgnoreCase(d_CountryName) && (d_player.getD_noOfArmies()>=d_noOfArmies))
            {
                int l_getArmy=l_country.getD_noOfArmies();

                int l_playerArmy=d_player.getD_noOfArmies();
                d_player.setD_noOfArmies(l_playerArmy-d_noOfArmies);

                d_noOfArmies=d_noOfArmies+l_getArmy;
                l_country.setD_noOfArmies(d_noOfArmies);

                return true;
            }
            else if(l_country.getD_countryName().equalsIgnoreCase(d_CountryName) && (d_player.getD_noOfArmies()<d_noOfArmies) && d_player.getD_noOfArmies()!=0)
            {
                int l_getArmy=l_country.getD_noOfArmies();
                d_noOfArmies = d_player.getD_noOfArmies()+l_getArmy;
                l_country.setD_noOfArmies(d_noOfArmies);

                d_player.setD_noOfArmies(0);
                return true;
            }
        }
        return false;

    }

}
