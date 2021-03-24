package com.soen6441.warzone.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import java.util.Map;

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
public class DeployOrder extends Order {

    /**
     * country in this order
     */
    private String d_CountryName;

    /**
     * number of armies in this order
     */
    private int d_noOfArmies;

    /**
     * No of mandatory fields It always needs to have after all necessary fields
     */
    public int d_mandatoryField = 2;

    /**
     * {@inheritDoc }
     * <p>
     * This method deploy armies to the country
     */
    @Override
    public boolean executeOrder() {
        int l_playerIndex=d_gameData.getD_playerList().indexOf(d_player);
        for (Country l_country : d_player.getD_ownedCountries()) {          //loop for countries owned by player
            if (l_country.getD_countryName().equalsIgnoreCase(d_CountryName) && (d_player.getD_noOfArmies() >= d_noOfArmies)) {  //checks the country,and no. of armies from player is greater than armies in the command

                int l_playerArmy = d_player.getD_noOfArmies();
                d_player.setD_noOfArmies(l_playerArmy - d_noOfArmies);        //reduce the number of armies from player

                int l_getArmy = l_country.getD_noOfArmies();
                d_noOfArmies = d_noOfArmies + l_getArmy;
                l_country.setD_noOfArmies(d_noOfArmies);                      //add the no. of armies to the country owned by player
                d_gameData.getD_playerList().remove(l_playerIndex);
                d_gameData.getD_playerList().add(l_playerIndex,d_player);

                if (d_gameData.getD_warMap().getD_continents() != null) {
                    for (Map.Entry<Integer, Continent> l_entry : d_gameData.getD_warMap().getD_continents().entrySet()) {
                        for (Country l_countries : l_entry.getValue().getD_countryList()) {
                            if (l_countries.getD_countryName().equalsIgnoreCase(l_country.getD_countryName())) {
                                l_countries.setD_noOfArmies(d_noOfArmies);                              //sets the no. of armies to the country of map
                            }
                        }
                    }
                }
                return true;
            } else if (l_country.getD_countryName().equalsIgnoreCase(d_CountryName) && (d_player.getD_noOfArmies() < d_noOfArmies) && d_player.getD_noOfArmies() != 0) {   //checks the country,and no. of armies from player is lesser than armies in the command

                int l_getArmy = l_country.getD_noOfArmies();
                d_noOfArmies = d_player.getD_noOfArmies() + l_getArmy;
                l_country.setD_noOfArmies(d_noOfArmies);

                d_player.setD_noOfArmies(0);
                d_gameData.getD_playerList().remove(l_playerIndex);
                d_gameData.getD_playerList().add(l_playerIndex,d_player);

                return true;
            }
        }
        if(d_noOfArmies > d_player.getD_noOfArmies())  //deduct the armies from player if the country mentioned inn the order is not owned by player
        {
            d_player.setD_noOfArmies(0);
        }
        else
        {
            d_player.setD_noOfArmies(d_player.getD_noOfArmies() - d_noOfArmies);

        }

        d_gameData.getD_playerList().remove(l_playerIndex);
        d_gameData.getD_playerList().add(l_playerIndex,d_player);
        return false;

    }

    /**
     *
     * @param p_countryName County name in the Command
     * @return validity Of Command
     */
    public boolean validateAndSetData(String p_countryName,int p_noOfArmies) {
        if (!p_countryName.isEmpty()) {
            this.setD_CountryName(p_countryName);
            this.setD_noOfArmies(p_noOfArmies);
            return true;
        }
        return false;
    }
}
