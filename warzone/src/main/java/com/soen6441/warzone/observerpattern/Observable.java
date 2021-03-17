package com.soen6441.warzone.observerpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements the connection/disconnection mechanism between
 * observers and observables (subject). It also implements the notification
 * mechanism that the observable will trigger when its state changes.
 *
 * @author <a href="mailto:jenilsavani009@gmail.com">Jaynil Savani</a>
 */
public class Observable {
    private List<Observer> d_observersList = new ArrayList<Observer>();

    /**
     * attach a view to the model.
     *
     * @param p_observer view to be added to the list of observers to be notified.
     */
    public void attach(Observer p_observer) {
        this.d_observersList.add(p_observer);
    }

    /**
     * detach a view from the model.
     *
     * @param p_observer view to be removed from the list of observers.
     */
    public void detach(Observer p_observer) {
        if (!d_observersList.isEmpty()) {
            d_observersList.remove(p_observer);
        }
    }

    /**
     * Notify all the views attached to the model.
     *
     * @param p_observable object that contains the information to be observed.
     */
    public void notifyObservers(Observable p_observable) {
        for (Observer l_observer : d_observersList) {
            l_observer.update(p_observable);
        }
    }
}
