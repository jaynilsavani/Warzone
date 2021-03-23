package com.soen6441.warzone.model;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This Class is used for The Bomb order Command Three annotations
 * (Getter,Setter, toString), you can see on the top of the class are lombok
 * dependencies to automatically generate getter, setter and tostring method in
 * the code.
 *
 * @author <a href="mailto:manthan.p.moradiya@gmail.com">Manthan Moradiya</a>
 */
@Getter
@Setter
@ToString
public class BombOrder extends Order {
    //First list the mandatory fields followed by count . Also validateAndSetData method have same argument as the order of declaration of the field

    /**
     * Country in this order
     */
    private String d_countryName;
    /**
     * No of mandatory fields It always needs to have after all necessary fields
     */
    public int d_mandatoryField = 1;

    /**
     * {@inheritDoc }
     * <p>
     * This method reduce 50% armies of the targeted country(Bomb Command)
     */
    @Override
    public boolean executeOrder() {
        Country l_countryName;
        //checking that target country is opponent country
        for (Country l_country : d_player.getD_ownedCountries()) {
            if (l_country.getD_countryName().equalsIgnoreCase(d_countryName)) {
                return false;
            }
        }
        for (Country l_country : d_player.getD_ownedCountries()) {

                //checkcing adjacency 
                for (String l_neighbour : l_country.getD_neighbourCountries()) {
                    if (d_countryName.equalsIgnoreCase(l_neighbour)) {
                        l_countryName = getCountryObjectByCountryName(d_countryName);
                        if (l_countryName != null && l_countryName.getD_noOfArmies() > 0) {
                            int l_army = l_countryName.getD_noOfArmies();
                            l_army = l_army / 2;
                            l_countryName.setD_noOfArmies(l_army);
                            return true;
                        }
                    }

            }
        }
        return false;
    }

    /**
     *
     * @param p_countryName County name in the Command
     * @return validity Of Command
     */
    public boolean validateAndSetData(String p_countryName) {
        if (!p_countryName.isEmpty()) {
            this.setD_countryName(p_countryName);
            return true;
        }
        return false;
    }

    /**
     * This method return Country Object
     *
     * @param p_countryName : name of the country
     * @return Country object
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
