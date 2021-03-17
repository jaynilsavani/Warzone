package com.soen6441.warzone.observerpattern;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * This is the Log entry buffer class used for log application state
 *
 * @author <a href="mailto:jenilsavani009@gmail.com">Jaynil Savani</a>
 */
@Component
@Getter
@Setter
public class LogEntryBuffer extends Observable {

    /**
     * message of event passed by controller
     */
    public String d_message;

    /**
     * This method will call notify observer
     *
     * @param p_message message to write in log file
     */
    public void setLogEntryBuffer(String p_message) {
        this.setD_message(p_message);
        notifyObservers(this);
    }
}
