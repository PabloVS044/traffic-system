package model.traffic;

import java.io.Serializable;

/**
 * STATE PATTERN - Context
 * Representa un semaforo que delega su comportamiento al estado actual.
 * El semaforo no conoce la logica de cada color; cada estado se encarga.
 *
 * Principio SRP: El semaforo solo gestiona transiciones, no logica de color.
 * Principio OCP: Nuevos estados se agregan sin modificar TrafficLight.
 */
public class TrafficLight implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String location;
    private TrafficLightState currentState;

    public TrafficLight(String id, String location) {
        this.id = id;
        this.location = location;
        this.currentState = new RedState(); // inicia en rojo por seguridad
    }

    public void changeState() {
        this.currentState = currentState.next();
    }

    public void setState(TrafficLightState state) {
        this.currentState = state;
    }

    public String getId() { return id; }
    public String getLocation() { return location; }
    public TrafficLightState getCurrentState() { return currentState; }

    public String getStatus() {
        return "Semaforo [" + id + "] en " + location +
               " -> " + currentState.getColor() +
               " (" + currentState.getAction() + ", " +
               currentState.getDurationSeconds() + "s)";
    }

    @Override
    public String toString() {
        return getStatus();
    }
}
