package model.road;

import java.util.ArrayList;
import java.util.List;

/**
 * COMPOSITE PATTERN - Composite
 * Representa una red vial que puede contener segmentos individuales
 * y/o sub-redes completas, formando una estructura jerarquica tipo arbol.
 *
 * Principio OCP: Se pueden agregar nuevos tipos de componentes
 * sin modificar esta clase.
 */
public class RoadNetwork implements RoadComponent {
    private static final long serialVersionUID = 1L;

    private String name;
    private String type;
    private List<RoadComponent> components;

    public RoadNetwork(String name, String type) {
        this.name = name;
        this.type = type;
        this.components = new ArrayList<>();
    }

    public void addComponent(RoadComponent component) {
        components.add(component);
    }

    public void removeComponent(RoadComponent component) {
        components.remove(component);
    }

    public List<RoadComponent> getComponents() {
        return components;
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getType() { return type; }

    @Override
    public double getTotalLength() {
        double total = 0;
        for (RoadComponent c : components) {
            total += c.getTotalLength();
        }
        return total;
    }

    @Override
    public int getTotalSegments() {
        int total = 0;
        for (RoadComponent c : components) {
            total += c.getTotalSegments();
        }
        return total;
    }

    @Override
    public String getDetails(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("[Red] ").append(name).append(" (").append(type).append(")")
          .append(" - ").append(String.format("%.1f", getTotalLength())).append(" km total, ")
          .append(getTotalSegments()).append(" segmentos\n");
        for (RoadComponent c : components) {
            sb.append(c.getDetails(indent + "  ")).append("\n");
        }
        return sb.toString().stripTrailing();
    }

    @Override
    public String toString() {
        return getDetails("");
    }
}
