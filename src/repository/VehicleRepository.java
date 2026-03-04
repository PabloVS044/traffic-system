package repository;

import model.vehicle.Vehicle;

import java.util.*;

/**
 * REPOSITORY PATTERN - Implementacion concreta
 * Repositorio en memoria para vehiculos.
 * Encapsula la logica de almacenamiento y busqueda de vehiculos.
 *
 * Principio SRP: Solo se encarga de persistir y recuperar vehiculos.
 */
public class VehicleRepository implements Repository<Vehicle> {
    private final Map<String, Vehicle> storage = new LinkedHashMap<>();

    @Override
    public void save(Vehicle vehicle) {
        storage.put(vehicle.getId(), vehicle);
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Vehicle> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }

    @Override
    public int count() {
        return storage.size();
    }

    public List<Vehicle> findByType(String type) {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle v : storage.values()) {
            if (v.getType().equalsIgnoreCase(type)) {
                result.add(v);
            }
        }
        return result;
    }

    public List<Vehicle> findByRoad(String roadName) {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle v : storage.values()) {
            if (v.getCurrentRoad().equalsIgnoreCase(roadName)) {
                result.add(v);
            }
        }
        return result;
    }
}
