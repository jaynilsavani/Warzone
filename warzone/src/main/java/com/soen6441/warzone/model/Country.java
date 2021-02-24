package com.soen6441.warzone.model;

import java.util.List;
import java.util.Objects;
import lombok.EqualsAndHashCode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
//@EqualsAndHashCode
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
     * to use the hashcode for this object
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.d_countryName);
        return hash;
    }

    /**
     * compares the objects
     *
     * @param obj object that needs to comapre
     * @return result of comparision
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Country other = (Country) obj;
        if (!Objects.equals(this.d_countryName, other.d_countryName)) {
            return false;
        }
        return true;
    }

}
