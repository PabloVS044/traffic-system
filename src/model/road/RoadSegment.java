package model.road;

/**
 * COMPOSITE PATTERN - Leaf (Hoja)
 * Representa un segmento individual de carretera.
 * Es la unidad basica e indivisible de la red vial.
 */
public class RoadSegment implements RoadComponent {
    private static final long serialVersionUID = 1L;

    private String name;
    private String type; // "autopista", "avenida", "calle", "carretera"
    private double lengthKm;
    private int lanes;
    private int speedLimit;

    public RoadSegment(String name, String type, double lengthKm, int lanes, int speedLimit) {
        this.name = name;
        this.type = type;
        this.lengthKm = lengthKm;
        this.lanes = lanes;
        this.speedLimit = speedLimit;
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getType() { return type; }

    @Override
    public double getTotalLength() { return lengthKm; }

    @Override
    public int getTotalSegments() { return 1; }

    public int getLanes() { return lanes; }

    public int getSpeedLimit() { return speedLimit; }

    @Override
    public String getDetails(String indent) {
        return indent + "[Segmento] " + name + " (" + type + ") - " +
               lengthKm + " km, " + lanes + " carriles, " + speedLimit + " km/h";
    }

    @Override
    public String toString() {
        return getDetails("");
    }
}
