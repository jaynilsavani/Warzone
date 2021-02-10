package com.soen6441.warzone.model;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class represents all the continents in the map and it is having list of territories that continent owned.
 *
 * Three annotations (Getter, Setter and ToString) you can see on the top of the class are lombok dependencies to
 * automatically generate getter, setter and tostring method in the code.
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">Yashkumar Vaghani</a>
 */
@Getter
@Setter
@ToString
public class Continent {

    /**
     * 4 data members of Continent class will be used to store continent data to map.
     * continentIndex will store index of the continent
     */
    private int d_continentIndex;

    /**
     * It'll represents name of the continent
     */
    private String d_continentName;

    /**
     * It'll represents value of the continent
     */
    private int d_continentValue;

    /**
     * List of territories this continent is having
     */
    private List<Territory> d_territoryList = new ArrayList<>();

}

