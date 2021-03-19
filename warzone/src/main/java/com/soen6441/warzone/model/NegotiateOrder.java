package com.soen6441.warzone.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This Class is used for The negotiate order Command Three annotations
 * (Getter,Setter, toString), you can see on the top of the class are lombok
 * dependencies to automatically generate getter, setter and tostring method in
 * the code.
 *
 * @author <a href="mailto:manthan.p.moradiya@gmail.com">Manthan Moradiya</a>
 */
@Getter
@Setter
@ToString
public class NegotiateOrder extends Order{

    /**
     * player in this order with whom current player wants to negotiate
     */
    private String d_playerName;
    /**
     * Player in this order
     */
    private Player d_player;

    @Override
    public boolean executeOrder() {
        return false;
    }

    @Override
    public GameData getGameData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
