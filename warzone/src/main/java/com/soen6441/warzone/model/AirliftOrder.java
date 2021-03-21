package com.soen6441.warzone.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;


/**
 * This Class is used for The airlift order Command Three annotations
 * (Getter,Setter, toString), you can see on the top of the class are lombok
 * dependencies to automatically generate getter, setter and tostring method in
 * the code.
 *
 * @author <a href="mailto:manthan.p.moradiya@gmail.com">Manthan Moradiya</a>
 */
@Getter
@Setter
@ToString
public class AirliftOrder extends Order{
    /**
     * source country in this order
     */
    private String d_CountryNameFrom;

    /**
     * country on which armies have to deployed
     */
    private String d_CountryNameTo;

    /**
     * number of armies in this order
     */
    private int d_noOfArmies;

    /**
     * No of mandatory fields It always needs to have after all necessary fields
     */
    public int d_mandatoryField = 3;


    @Override
    public boolean executeOrder() {
        return false;
    }

    @Override
    public GameData getGameData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
