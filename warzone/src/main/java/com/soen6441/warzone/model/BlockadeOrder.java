package com.soen6441.warzone.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * This Class is used for The Blockade Command Three annotations (Getter,Setter,
 * toString), you can see on the top of the class are lombok dependencies to
 * automatically generate getter, setter and tostring method in the code.
 *
 * @author <a href="mailto:manthan.p.moradiya@gmail.com">Manthan Moradiya</a>
 */
@Getter
@Setter
@ToString
@Component
public class BlockadeOrder extends Order {

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
     *
     *
     */
    @Override
    public boolean executeOrder() {
        Country l_countryName = null;
        boolean l_status = false;
        for (Country l_country : d_player.getD_ownedCountries()) {
            if (l_country.getD_countryName().equalsIgnoreCase(d_countryName)) {
                l_countryName = getCountryObjectByCountryName(d_countryName);
                if (l_countryName != null) {
                    int l_army = l_countryName.getD_noOfArmies() * 3;
                    l_countryName.setD_noOfArmies(l_army);
                    l_status = true;
                }
            } else {
                d_orderResponse.setD_isValid(false);
                d_orderResponse.setD_responseString("Given Country does not Owned By Player");
            }
        }
        if (l_status) {
            List<Country> l_countryList = d_player.getD_ownedCountries();
            l_countryList.remove(l_countryName);
            d_player.setD_ownedCountries(l_countryList);

            //set player object into gamedata
            List<Integer> l_playerIndex = new ArrayList<>();
            for (Player l_player : d_gameData.getD_playerList()) {
                if (l_player.getD_playerName().equalsIgnoreCase(d_player.getD_playerName())) {
                    l_playerIndex.add(d_gameData.getD_playerList().indexOf(l_player));
                    break;
                }
            }
            if (!l_playerIndex.isEmpty()) {
                d_gameData.getD_playerList().set(l_playerIndex.get(0), d_player);
            }
        }
        return l_status;
    }

    /**
     * used to validate the data of this class
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
