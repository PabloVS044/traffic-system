package model.traffic;

import java.io.Serializable;

/**
 * STATE PATTERN - State Interface
 * Define el contrato para todos los estados posibles de un semaforo.
 *
 * Principios SOLID:
 * - OCP: Se pueden agregar nuevos estados sin modificar los existentes.
 * - SRP: Cada estado concreto maneja unicamente su propia logica.
 * - LSP: Cualquier estado puede sustituirse donde se espere TrafficLightState.
 */
public interface TrafficLightState extends Serializable {
    String getColor();
    String getAction(); // que deben hacer los vehiculos
    int getDurationSeconds();
    TrafficLightState next(); // transicion al siguiente estado
}
