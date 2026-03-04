package model.traffic;

/**
 * STATE PATTERN - Concrete State
 * Estado Amarillo: Los vehiculos deben prepararse para detenerse.
 */
public class YellowState implements TrafficLightState {
    private static final long serialVersionUID = 1L;

    @Override
    public String getColor() { return "AMARILLO"; }

    @Override
    public String getAction() { return "PRECAUCION"; }

    @Override
    public int getDurationSeconds() { return 5; }

    @Override
    public TrafficLightState next() {
        return new RedState();
    }
}
