package com.soen6441.warzone.strategy;

import com.soen6441.warzone.model.Continent;
import com.soen6441.warzone.model.Country;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.model.Order;
import com.soen6441.warzone.model.OrderTypes;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.model.WarMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * This Abstract class is used as a Basic Structure for various Strategies.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class Strategy {

    /**
     *
     * @param p_gameData GameData Object needed for the player GameDa
     * @param p_player Player Object on which Strategy being Applied
     */
    public Strategy(GameData p_gameData, Player p_player) {
        this.d_gameData = p_gameData;
        this.d_player = p_player;
    }

    /**
     * GameData Object needed for the player
     */
    public GameData d_gameData;

    /**
     * Player Object on which Strategy being Applied
     */
    public Player d_player;

    /**
     * List of allowed Order
     */
    public List<OrderTypes> d_allowedOrders;

    /**
     *
     * @return Created ORder according to strategy
     */
    abstract public Order createOrder();

    /**
     *
     * @return Updated GameData By the player
     */
    public GameData getGameData() {
        return d_gameData;
    }

    /**
     *
     * @param p_startNumber starting number of the Series
     * @param p_endNumber Ending Number of the Series
     * @return Random Number
     */
    public int generateUniqueRandomNumber(int p_startNumber, int p_endNumber) {
        ArrayList<Integer> l_numbers = new ArrayList<Integer>();
        for (int l_number = p_startNumber; l_number <= p_endNumber; l_number++) {
            l_numbers.add(l_number);
        }
        if (l_numbers.size() > 0) {
            Collections.shuffle(l_numbers);
            return l_numbers.get(0);
        }
        return p_startNumber;
    }

    public ArrayList<Country> getAvailableCountries(WarMap p_gameMap) {

        List<Country> l_countries = new ArrayList<Country>();
        l_countries.clear();
        for (Map.Entry<Integer, Continent> l_entry : p_gameMap.getD_continents().entrySet()) {
            if (l_entry.getValue().getD_countryList() != null) {
                for (Country l_country : l_entry.getValue().getD_countryList()) {
                    l_countries.add(l_country);
                }
            }
        }
        return (ArrayList<Country>) l_countries;
    }

}
