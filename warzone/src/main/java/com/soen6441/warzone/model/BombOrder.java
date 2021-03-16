package com.soen6441.warzone.model;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This Class is used for The deploy order Command Three annotations
 * (Getter,Setter, toString), you can see on the top of the class are lombok
 * dependencies to automatically generate getter, setter and tostring method in
 * the code.
 *
 * @author <a href="mailto:manthan.p.moradiya@gmail.com">Manthan Moradiya</a>
 */
@Getter
@Setter
@ToString
public class BombOrder implements Order {

    GameData d_gameData;
    /**
     * Country in this order
     */
    private String d_countryName;
    /**
     * Player in this order
     */
    private Player d_player;

    /**
     * {@inheritDoc }
     * <p>
     * This method deploy armies to the country
     */
    @Override
    public boolean executeOrder() {
        Country l_countryName;
        for (Country l_country : d_player.getD_ownedCountries()) {
            if (l_country.getD_countryName().equals(d_countryName)) {
                return false;
            } else {
                for (String l_neighbour : l_country.getD_neighbourCountries()) {
                    if (d_countryName.equalsIgnoreCase(l_neighbour)) {
                        l_countryName = getCountryObjectByCountryName(d_countryName);
                        if (l_countryName != null && !(l_countryName.getD_noOfArmies() == 0)) {
                            int l_army = l_countryName.getD_noOfArmies();
                            l_army = l_army / 2;
                            l_countryName.setD_noOfArmies(l_army);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * This method return Country Model Object
     *
     * @param p_countryName : name of the country
     * @return Country model object
     */
    public Country getCountryObjectByCountryName(String p_countryName) {
        Country l_countryName = null;
        Map<Integer, Continent> l_continentMap = d_gameData.getD_warMap().getD_continents();
        for (Map.Entry<Integer, Continent> l_entry : l_continentMap.entrySet()) {
            for (Country l_country : l_entry.getValue().getD_countryList()) {
                if (p_countryName.equalsIgnoreCase(l_country.getD_countryName())) {
                    l_countryName = l_country;
                }
            }
        }
        return l_countryName;
    }
}
