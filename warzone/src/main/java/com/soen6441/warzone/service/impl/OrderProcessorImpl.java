package com.soen6441.warzone.service.impl;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.model.Order;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.service.GeneralUtil;
import com.soen6441.warzone.service.OrderProcessor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * This Class is used for
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Service
public class OrderProcessorImpl implements OrderProcessor {

    Order d_order;

    @Autowired
    GeneralUtil d_generalUtil;

    @Override
    public CommandResponse processOrder(String p_orderCommand,GameData p_gameData) {
        CommandResponse l_commandResponse = new CommandResponse();
        List<String> l_commandData = Arrays.asList(p_orderCommand.split(" "));
        String l_orderName = d_generalUtil.toTitleCase(l_commandData.get(0).toLowerCase());
        List<String> l_args = l_commandData.subList(1, l_commandData.size());
        l_orderName += "Order";
        Class l_classObj = null;
        Object l_orderObj = null;
        try {
            l_classObj = Class.forName("com.soen6441.warzone.model." + l_orderName);
            l_orderObj = l_classObj.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            //Invalid Command 
            l_commandResponse.setD_isValid(false);
            l_commandResponse.setD_responseString("Command Is not valid");
        }

        Field l_mandatoryField = null;
        Field l_player = null;
        Field l_gameData = null;

        int l_noOfMandatoryFields = 0;
        Field[] l_fields = l_classObj.getDeclaredFields();
        System.out.printf("%d fields:%n", l_fields.length);
        List<Object> l_obj=new ArrayList<>();
        List<Class<?>> l_fieldTypes = new ArrayList<>();
        try {
            l_mandatoryField = l_classObj.getField("d_mandatoryField");
            l_gameData = l_classObj.getField("d_gameData");

            l_noOfMandatoryFields = (int) l_mandatoryField.get(l_orderObj);
            for (int i = 0; i < l_noOfMandatoryFields; i++) {
                /*System.out.printf("%s %s %s%n",
                        Modifier.toString(l_fields[i].getModifiers()),
                        l_fields[i].getType().getSimpleName(),
                        l_fields[i].getName()
                );*/

                l_fieldTypes.add(l_fields[i].getType());

            }

            int l_i=0;
            for (Class<?> l_field: l_fieldTypes) {
                if(l_field.getTypeName().equalsIgnoreCase("int"))
                {
                    int l_m=Integer.parseInt(l_args.get(l_i));
                    l_obj.add(l_m);
                }
                else
                {
                    l_obj.add(l_args.get(l_i));
                }
                l_i++;

            }

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            //Invalid Command Implementation 
            l_commandResponse.setD_isValid(false);
            l_commandResponse.setD_responseString("Command does not have valid Implementation");
        }

        Method l_validateMethod = null;
        boolean invoke = false;
        try {
            l_validateMethod = l_classObj.getDeclaredMethod("validateAndSetData", l_fieldTypes.toArray(new Class<?>[l_fieldTypes.size()]));
            invoke = (boolean) l_validateMethod.invoke(l_orderObj, l_obj.toArray());
            l_gameData.set(l_orderObj, p_gameData);
            d_order = (Order) l_orderObj;
            l_commandResponse.setD_isValid(true);
            l_commandResponse.setD_responseString("executed successfuly");
            return l_commandResponse;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            // Invalid Command arguments
            l_commandResponse.setD_isValid(false);
            l_commandResponse.setD_responseString("Command's argument is not valid");
        }
        return l_commandResponse;
    }

    @Override
    public Order getOrder() {
        return d_order;
    }

}
