package com.soen6441.warzone.controller;


import com.soen6441.warzone.service.MapHandlingInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * This Class is made to handle Game Engine controller request
 *
 * @author <a href="mailto:patelvicky1995@gmail.com">Vicky Patel</a>
 */


@Controller
public class GameEngineController implements Initializable {
    @FXML
    public TextArea d_TerritoryListText;
    public TextArea d_ContinentText;
    public TextArea d_TerritoryPlayerArmyText;
    @FXML
    Button d_BtnExit;
    @FXML
    private TextField d_CommandLine;
    @Autowired
    private MapHandlingInterface d_maphandlinginterface;

    /**
     * This method will exit the game and close the stage
     *
     * @param event will represents value sent from view
     */
    @FXML
    public void exitGame(ActionEvent event) {

        Stage stage = (Stage) d_BtnExit.getScene().getWindow();
        stage.close();
    }

    /**
     * This is the initialization method of this controller
     *
     * @param location  of the FXML file
     * @param resources is properties information
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * This method is used to get fire command from user and put it as a parameter in validation
     *
     * @param event
     */

    public void getData(ActionEvent event) {
        String s = d_CommandLine.getText();
        System.out.println( s );
        d_maphandlinginterface.validateCommand( s );
        d_CommandLine.clear();
    }

}
