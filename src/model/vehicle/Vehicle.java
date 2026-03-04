package model.vehicle;

import java.io.Serializable;

/**
 * Representa un vehiculo que transita por la red vial.
 */
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String plate;
    private String type; // "sedan", "camion", "motocicleta", "bus"
    private String brand;
    private String currentRoad;
    private double speed;

    public Vehicle(String id, String plate, String type, String brand) {
        this.id = id;
        this.plate = plate;
        this.type = type;
        this.brand = brand;
        this.currentRoad = "ninguna";
        this.speed = 0;
    }

    public String getId() { return id; }
    public String getPlate() { return plate; }
    public String getType() { return type; }
    public String getBrand() { return brand; }
    public String getCurrentRoad() { return currentRoad; }
    public double getSpeed() { return speed; }

    public void setCurrentRoad(String currentRoad) { this.currentRoad = currentRoad; }
    public void setSpeed(double speed) { this.speed = speed; }

    @Override
    public String toString() {
        return "Vehiculo [" + id + "] " + brand + " (" + type + ") Placa: " + plate +
               " | Via: " + currentRoad + " | Velocidad: " + speed + " km/h";
    }
}
