package model.traffic;

/**
 * STATE PATTERN - Concrete State
 * Estado Verde: Los vehiculos pueden avanzar.
 */
public class GreenState implements TrafficLightState {
    private static final long serialVersionUID = 1L;

    @Override
    public String getColor() { return "VERDE"; }

    @Override
    public String getAction() { return "AVANZAR"; }

    @Override
    public int getDurationSeconds() { return 25; }

    @Override
    public TrafficLightState next() {
        return new YellowState();
    }
}
