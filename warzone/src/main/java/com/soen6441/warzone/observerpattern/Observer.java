package com.soen6441.warzone.observerpattern;

/**
 * Interface class for the Observer, which forces all views to implement the
 * update method.
 *
 * @author <a href="mailto:jenilsavani009@gmail.com">Jaynil Savani</a>
 */
public interface Observer {

    /**
     * method to be implemented that reacts to the notification generally by
     * interrogating the model object and displaying its newly updated state.
     *
     * @param p_state Object that is passed by the subject (observable).
     */
    public void update(Observable p_state);
}
