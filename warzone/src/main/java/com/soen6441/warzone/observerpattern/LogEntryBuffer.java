package com.soen6441.warzone.observerpattern;

import org.springframework.stereotype.Component;

/**
 * This is the Log entry buffer class used for log application state
 *
 * @author <a href="mailto:jenilsavani009@gmail.com">Jaynil Savani</a>
 */
@Component
public class LogEntryBuffer extends Observable {
    /**
     * This method will call notify observer
     *
     * @param p_message message to write in log file
     */
    public void setLogEntryBuffer(String p_message) {
        notifyObservers(this, p_message);
    }
}
