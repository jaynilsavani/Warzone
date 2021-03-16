package com.soen6441.warzone.controller;

import com.soen6441.warzone.config.StageManager;
import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.observerpattern.LogEntryBuffer;
import com.soen6441.warzone.observerpattern.WriteLogFile;
import com.soen6441.warzone.service.MapHandlingInterface;
import com.soen6441.warzone.view.FxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * This class provides map management functionalities such as add an existing map, create a new map.
 *
 * @author <a href="mailto:patelvicky1995@gmail.com">Vicky Patel</a>
 */
@Controller
public class MapController implements Initializable {

    @Lazy
    @Autowired
    StageManager d_stageManager;
    @FXML
    private TextField d_ExecuteCommand;
    @FXML
    private TextArea d_commandResponse;
    @Autowired
    private MapHandlingInterface d_maphandlinginterface;

    private LogEntryBuffer d_logEntryBuffer = new LogEntryBuffer();
    private WriteLogFile d_writeLogFile = new WriteLogFile(d_logEntryBuffer);

    /**
     * This is the initialization method of this controller
     *
     * @param p_location of the FXML file
     * @param p_resources is properties information
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL p_location, ResourceBundle p_resources) {
        d_commandResponse.setStyle("-fx-font-family: monospace");
    }

    /**
     * This method will redirect user to Home page
     *
     * @param p_event will represents value sent from view
     */
    @FXML
    void backToWelcome(ActionEvent p_event) {
        d_stageManager.switchScene(FxmlView.HOME, null,"");
    }

    /**
     * This method is used to get data from user and put it as a parameter in
     * validation
     *
     * @param p_event will represents value sent from view
     */
    @FXML
    public void getData(ActionEvent p_event) {
        String l_s = d_ExecuteCommand.getText().trim();

        d_logEntryBuffer.setLogEntryBuffer("Command ::" + l_s);
        CommandResponse l_commandRespose = d_maphandlinginterface.validateCommand(l_s);
        d_logEntryBuffer.setLogEntryBuffer("Response ::" + l_commandRespose.getD_responseString());

        d_ExecuteCommand.clear();
        d_commandResponse.setText(l_commandRespose.toString());
    }
}
