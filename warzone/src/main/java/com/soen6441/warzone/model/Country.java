package com.soen6441.warzone.model;

import java.util.List;
import java.util.Objects;

import lombok.*;

import org.springframework.stereotype.Component;

/**
 * This class represents Country model and property of country from the map
 * file. It's also having list of neighbourCountries.
 *
 * @author <a href="mailto:y_vaghan">yashkumar vaghani</a>
 * @see com.soen6441.warzone.model.Continent
 */
@Getter
@Setter
@ToString
@Component
@NoArgsConstructor
public class Country {

    /**
     * It'll store index of country
     */
    private int d_countryIndex;

    /**
     * It'll store Name of country
     */
    private String d_countryName;

    /**
     * It'll store index of continent in which this country is located
     */
    private int d_continentIndex;

    /**
     * List of neighbor countries of this country
     */
    private List<String> d_neighbourCountries;

    /**
     * number of armies in this country
     */
    private int d_noOfArmies;

    /**
     * compares the objects
     *
     * @param p_obj object that needs to comapre
     * @return result of comparision
     */
    @Override
    public boolean equals(Object p_obj) {
        if (this == p_obj) return true;
        if (p_obj == null || getClass() != p_obj.getClass()) return false;
        Country l_country = (Country) p_obj;
        return d_continentIndex == l_country.d_continentIndex && d_countryName.equals(l_country.d_countryName);
    }

    /**
     * to use the hashcode for this object
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(d_countryName, d_continentIndex);
    }

    public Country(Country p_country) {
        this.d_countryIndex = p_country.d_countryIndex;
        this.d_countryName = p_country.d_countryName;
        this.d_continentIndex = p_country.d_continentIndex;
        this.d_neighbourCountries = p_country.d_neighbourCountries;
        this.d_noOfArmies = p_country.d_noOfArmies;
    }

}
