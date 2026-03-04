package repository;

import model.road.RoadComponent;

import java.util.*;

/**
 * REPOSITORY PATTERN - Implementacion concreta
 * Repositorio en memoria para componentes viales (segmentos y redes).
 *
 * Principio SRP: Solo se encarga de persistir y recuperar componentes viales.
 * Principio LSP: Almacena cualquier RoadComponent (segmento o red) de forma uniforme.
 */
public class RoadRepository implements Repository<RoadComponent> {
    private final Map<String, RoadComponent> storage = new LinkedHashMap<>();

    @Override
    public void save(RoadComponent component) {
        storage.put(component.getName(), component);
    }

    @Override
    public Optional<RoadComponent> findById(String name) {
        return Optional.ofNullable(storage.get(name));
    }

    @Override
    public List<RoadComponent> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(String name) {
        storage.remove(name);
    }

    @Override
    public int count() {
        return storage.size();
    }

    public List<RoadComponent> findByType(String type) {
        List<RoadComponent> result = new ArrayList<>();
        for (RoadComponent c : storage.values()) {
            if (c.getType().equalsIgnoreCase(type)) {
                result.add(c);
            }
        }
        return result;
    }
}
