package repository;

import model.traffic.TrafficLight;

import java.util.*;

/**
 * REPOSITORY PATTERN - Implementacion concreta
 * Repositorio en memoria para semaforos.
 */
public class TrafficLightRepository implements Repository<TrafficLight> {
    private final Map<String, TrafficLight> storage = new LinkedHashMap<>();

    @Override
    public void save(TrafficLight light) {
        storage.put(light.getId(), light);
    }

    @Override
    public Optional<TrafficLight> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<TrafficLight> findAll() {
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
}
