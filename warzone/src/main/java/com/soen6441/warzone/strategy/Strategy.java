package com.soen6441.warzone.strategy;

import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.model.Order;
import com.soen6441.warzone.model.OrderTypes;
import com.soen6441.warzone.model.Player;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * This Abstract class is used as a Basic Structure for various Strategies.
 * Three annotations (Getter,Setter, NoArgsConstructor), you can see on the
 * top of the class are lombok dependencies to automatically generate getter,
 * setter method and default Constructor in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class Strategy {

    /**
     * This Parameterized constructor is used to initialize to GameData object and Player
     * object.
     *
     * @param p_gameData GameData Object needed for the player GameData
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
     * This method is used to create order specific to strategy.
     *
     * @return Created Order according to strategy
     */
    abstract public Order createOrder();

    /**
     * This method is used to get GameData from player.
     *
     * @return Updated GameData By the player
     */
    public GameData getGameData() {
        return d_gameData;
    }

}
