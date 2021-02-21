package com.soen6441.warzone.model;

import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

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
            System.out.println("hehe "+d_CountryName+" ^^ "+d_noOfArmies+" %% "+l_country.getD_countryName()+" ## "+d_player.getD_noOfArmies());
            if(l_country.getD_countryName().equalsIgnoreCase(d_CountryName) && (d_player.getD_noOfArmies()>=d_noOfArmies))
            {
                int l_getArmy=l_country.getD_noOfArmies();
                l_country.setD_noOfArmies(l_getArmy+d_noOfArmies);
                int l_playerArmy=d_player.getD_noOfArmies();
                d_player.setD_noOfArmies(l_playerArmy-d_noOfArmies);
                return true;
            }
        }
        return false;

    }

}
