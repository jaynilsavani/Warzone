package com.soen6441.warzone.model;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * This class represents all the continents in the map and it has a list of territories that continent owned.
 *
 * Three annotations (Getter, Setter and ToString), you can see on the top of the class are lombok dependencies to
 * automatically generate getter, setter and tostring method in the code.
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">Yashkumar Vaghani</a>
 */
@Getter
@Setter
@ToString
@Component
public class Continent {

    /**
     * Four data members of Continent class will be used to store continent data to map.
     * d_continentIndex will store index of the continent
     */
    private int d_continentIndex;

    /**
     * d_continentName represents name of the continent
     */
    private String d_continentName;

    /**
     * d_continentValue represents value of the continent
     */
    private int d_continentValue;

    /**
     * List of countries represents all the countries of this continent
     */
    private List<Country> d_countryList = new ArrayList<>();

}

