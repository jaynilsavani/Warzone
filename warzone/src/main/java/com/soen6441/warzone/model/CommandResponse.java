/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soen6441.warzone.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * This class represents structure of response of all the commands .
 *
 * Three annotations (Getter, Setter and ToString), you can see on the top of
 * the class are lombok dependencies to automatically generate getter, setter
 * and tostring method in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Getter
@Setter
@Component
public class CommandResponse {

    /**
     * d_isValid will be used to verify whether command is valid or not
     */
    private boolean d_isValid;

    /**
     * d_responseString will be used for setting any message in response
     */
    private String d_responseString;

    @Override
    public String toString() {
        String l_validity ;
        if(d_isValid){
            l_validity = "Command Executed Sucessfully";
        }else{
            l_validity ="Command Execution failed";
        }
        return "Validity :: " + l_validity + "\n" + "Message :: " + d_responseString + "\n";
    }

}
