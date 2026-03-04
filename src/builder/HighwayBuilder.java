package builder;

import model.road.RoadSegment;

/**
 * BUILDER PATTERN
 * Permite construir objetos RoadSegment complejos paso a paso,
 * separando la construccion de la representacion.
 *
 * Problema que resuelve: Evitar constructores con muchos parametros
 * (telescoping constructor) y permitir configuraciones flexibles.
 *
 * Principios SOLID:
 * - SRP: El Builder solo se encarga de construir segmentos de carretera.
 * - OCP: Se puede extender para nuevos atributos sin modificar el codigo existente.
 */
public class HighwayBuilder {
    private String name = "Sin nombre";
    private String type = "calle";
    private double lengthKm = 1.0;
    private int lanes = 2;
    private int speedLimit = 60;

    public HighwayBuilder() {}

    public HighwayBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public HighwayBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public HighwayBuilder withLength(double lengthKm) {
        this.lengthKm = lengthKm;
        return this;
    }

    public HighwayBuilder withLanes(int lanes) {
        this.lanes = lanes;
        return this;
    }

    public HighwayBuilder withSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
        return this;
    }

    public RoadSegment build() {
        return new RoadSegment(name, type, lengthKm, lanes, speedLimit);
    }
}
