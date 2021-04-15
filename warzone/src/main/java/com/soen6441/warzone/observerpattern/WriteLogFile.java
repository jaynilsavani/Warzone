package com.soen6441.warzone.observerpattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * This class will write message to log file
 *
 * @author <a href="mailto:jenilsavani009@gmail.com">Jaynil Savani</a>
 */
@Component
public class WriteLogFile implements Observer {

    /**
     * logger object to write message in log file
     */
    private static final Logger d_logger = LogManager.getLogger(WriteLogFile.class.getName());

    /**
     * Constructor that attaches the controller to the observer.
     *
     * @param p_logEntryBuffer attach buffer to observer
     */
    public WriteLogFile(LogEntryBuffer p_logEntryBuffer) {
        p_logEntryBuffer.attach(this);
    }

    /**
     * Display the state of application
     *
     * @param p_observable object that contains the information to be displayed
     */
    public void update(Observable p_observable) {
        d_logger.info(((LogEntryBuffer) p_observable).getD_message());
    }
}
