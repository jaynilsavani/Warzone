package com.soen6441.warzone.controller;

import com.soen6441.warzone.config.FxmlView;
import com.soen6441.warzone.config.StageManager;
import com.soen6441.warzone.service.MapHandlingInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * This Class is made to handle Game Config controller request
 *
 * @author <a href="mailto:patelvicky1995@gmail.com">Vicky Patel</a>
 */

@Controller
public class GameConfigController implements Initializable {

    @FXML
    private TextField d_CommandLine;


    @Lazy
    @Autowired
    private StageManager d_stageManager;


    @Autowired
    private MapHandlingInterface d_maphandlinginterface;

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
     * This method will redirect user to the Home Screen
     *
     * @param event represents value send from view
     */

    @FXML
    void backToWelcome(ActionEvent event) {

        d_stageManager.switchScene( FxmlView.HOME, null );
    }

    /**
     * This method will redirect user Game Start Screen
     *
     * @param event represent value send from view
     */
    @FXML
    void toStartGame(ActionEvent event) {

        d_stageManager.switchScene( FxmlView.GAMEENGINE, null );
    }

    /**
     * This method is used to get fire command from user and put it as a parameter in validation
     *
     * @param event
     */

    public void getData(ActionEvent event) {

        String toTestConsole = d_CommandLine.getText();
        System.out.println( toTestConsole );  // Just Testing Purpose
        d_maphandlinginterface.validateCommand( toTestConsole );
        d_CommandLine.clear();
    }

}

