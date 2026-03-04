package model.traffic;

/**
 * STATE PATTERN - Concrete State
 * Estado Rojo: Los vehiculos deben detenerse.
 */
public class RedState implements TrafficLightState {
    private static final long serialVersionUID = 1L;

    @Override
    public String getColor() { return "ROJO"; }

    @Override
    public String getAction() { return "DETENERSE"; }

    @Override
    public int getDurationSeconds() { return 30; }

    @Override
    public TrafficLightState next() {
        return new GreenState();
    }
}
