package repository;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY PATTERN - Interfaz generica
 * Abstrae el acceso a datos, proporcionando una coleccion logica
 * de objetos del dominio sin exponer detalles de almacenamiento.
 *
 * Problema que resuelve: Desacopla la logica de negocio del acceso a datos,
 * facilitando pruebas y cambios en la capa de persistencia.
 *
 * Principios SOLID:
 * - DIP: Las capas superiores dependen de esta abstraccion, no de implementaciones.
 * - ISP: Interfaz enfocada en operaciones CRUD basicas.
 * - OCP: Se pueden crear nuevas implementaciones sin modificar el contrato.
 */
public interface Repository<T> {
    void save(T entity);
    Optional<T> findById(String id);
    List<T> findAll();
    void deleteById(String id);
    int count();
}
