package com.soen6441.warzone.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * This Class is used for The deploy order Command Three annotations
 * (Getter,Setter, toString), you can see on the top of the class are lombok
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

    /**
     * number of armies in this order
     */
    private int d_noOfArmies;
    /**
     * country in this order
     */
    private String d_CountryName;
    /**
     * player in this order
     */
    private Player d_player;

    /**
     * {@inheritDoc }
     * <p>
     * This method deploy armies to the country
     */
    @Override
    public boolean executeOrder() {
        for (Country l_country : d_player.getD_ownedCountries()) {          //loop for countries owned by player
            if (l_country.getD_countryName().equalsIgnoreCase(d_CountryName) && (d_player.getD_noOfArmies() >= d_noOfArmies)) {  //checks the country,and no. of armies from player is greater than armies in the command

                int l_playerArmy = d_player.getD_noOfArmies();
                d_player.setD_noOfArmies(l_playerArmy - d_noOfArmies);        //reduce the number of armies from player

                int l_getArmy = l_country.getD_noOfArmies();
                d_noOfArmies = d_noOfArmies + l_getArmy;
                l_country.setD_noOfArmies(d_noOfArmies);                      //add the no. of armies to the country owned by player

                return true;
            } else if (l_country.getD_countryName().equalsIgnoreCase(d_CountryName) && (d_player.getD_noOfArmies() < d_noOfArmies) && d_player.getD_noOfArmies() != 0) {   //checks the country,and no. of armies from player is lesser than armies in the command

                int l_getArmy = l_country.getD_noOfArmies();
                d_noOfArmies = d_player.getD_noOfArmies() + l_getArmy;
                l_country.setD_noOfArmies(d_noOfArmies);

                d_player.setD_noOfArmies(0);
                return true;
            }
        }
        d_player.setD_noOfArmies(d_player.getD_noOfArmies() - d_noOfArmies);   //deduct the armies from player if the country mentioned inn the order is not owned by player

        return false;

    }

}
