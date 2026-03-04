package model.road;

import java.io.Serializable;
import java.util.List;

/**
 * COMPOSITE PATTERN - Component
 * Interfaz base para todos los elementos de la red vial.
 * Permite tratar segmentos individuales y redes completas de forma uniforme.
 *
 * Principios SOLID:
 * - ISP: Interfaz enfocada solo en operaciones de componentes viales.
 * - LSP: Cualquier implementacion puede sustituirse donde se use RoadComponent.
 * - DIP: El sistema depende de esta abstraccion, no de clases concretas.
 */
public interface RoadComponent extends Serializable {
    String getName();
    String getType();
    double getTotalLength();
    int getTotalSegments();
    String getDetails(String indent);
}
