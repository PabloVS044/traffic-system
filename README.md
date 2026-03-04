# Sistema de Gestion de Transito Vial

Sistema cliente-servidor en Java para gestionar vehiculos, vias y semaforos, implementando multiples patrones de diseno.

## Que hace?

Simula un sistema de transito donde desde un cliente interactivo podes:

- Registrar y administrar **vehiculos** (agregar, listar, asignar a vias, eliminar)
- Crear **vias y redes viales** con estructura jerarquica (autopistas, avenidas, calles)
- Controlar **semaforos** con cambios de estado automaticos (rojo -> verde -> amarillo -> rojo)
- Ver un **resumen** general del sistema

El servidor escucha en el puerto `5000` y soporta multiples clientes conectados al mismo tiempo.

## Patrones de diseno utilizados

| Patron | Donde se aplica | Para que? |
|---|---|---|
| **State** | Semaforos (`TrafficLight`, `GreenState`, `YellowState`, `RedState`) | Cada estado encapsula su color, duracion y transicion al siguiente |
| **Builder** | Creacion de vias (`HighwayBuilder`) | Construir segmentos de via paso a paso con interfaz fluida |
| **Composite** | Red vial (`RoadComponent`, `RoadSegment`, `RoadNetwork`) | Tratar segmentos individuales y redes compuestas de forma uniforme |
| **Repository** | Acceso a datos (`VehicleRepository`, `RoadRepository`, `TrafficLightRepository`) | Abstraer el almacenamiento de datos con operaciones CRUD genericas |
| **Client-Server** | Comunicacion (`TrafficServer`, `TrafficClient`) | Separar la interfaz de usuario de la logica de negocio via sockets TCP |

## Estructura del proyecto

```
src/
  client/          -> Cliente interactivo (TrafficClient)
  server/          -> Servidor TCP y manejador de solicitudes (TrafficServer, RequestHandler)
  model/
    vehicle/       -> Modelo de vehiculo
    traffic/       -> Semaforos y sus estados (State Pattern)
    road/          -> Vias y redes viales (Composite Pattern)
  builder/         -> Constructor de vias (Builder Pattern)
  repository/      -> Repositorios en memoria (Repository Pattern)
  protocol/        -> Mensajes Request/Response serializables
```

## Como ejecutarlo

### Opcion 1: Con Docker (recomendado)

No necesitas tener Java instalado, solo Docker.

**Terminal 1 - Iniciar el servidor:**
```bash
docker compose up server
```

**Terminal 2 - Conectar un cliente:**
```bash
docker compose run --rm client
```

Podes abrir multiples terminales de cliente repitiendo el segundo comando.

**Para parar todo:**
```bash
docker compose down
```

### Opcion 2: Manual con Java

Requiere JDK 17 o superior.

**Compilar:**
```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

**Terminal 1 - Iniciar el servidor:**
```bash
cd out
java server.TrafficServer
```

**Terminal 2 - Conectar un cliente:**
```bash
cd out
java client.TrafficClient
```

## Comandos disponibles

Una vez conectado el cliente, escribi `HELP` para ver todos los comandos. Aqui un resumen:

### Vehiculos
| Comando | Descripcion |
|---|---|
| `LISTAR_VEHICULOS` | Lista todos los vehiculos registrados |
| `AGREGAR_VEHICULO <id> <placa> <tipo> <marca>` | Agrega un vehiculo nuevo |
| `ASIGNAR_VIA <vehiculoId> <nombreVia> <velocidad>` | Asigna un vehiculo a una via con velocidad |
| `ELIMINAR_VEHICULO <id>` | Elimina un vehiculo |

### Vias y redes
| Comando | Descripcion |
|---|---|
| `LISTAR_VIAS` | Lista todas las vias y redes |
| `CREAR_VIA <nombre> <tipo> <longitudKm> <carriles> <limiteVelocidad>` | Crea un segmento de via |
| `CREAR_RED <nombre> <tipo>` | Crea una red vial vacia |
| `AGREGAR_A_RED <nombreRed> <nombreComponente>` | Agrega una via o sub-red a una red existente |
| `DETALLE_RED <nombre>` | Muestra la estructura jerarquica de una red |

### Semaforos
| Comando | Descripcion |
|---|---|
| `LISTAR_SEMAFOROS` | Lista todos los semaforos con su estado actual |
| `CAMBIAR_SEMAFORO <id>` | Cambia el estado del semaforo al siguiente |
| `ESTADO_SEMAFORO <id>` | Muestra el estado actual de un semaforo |

### Sistema
| Comando | Descripcion |
|---|---|
| `RESUMEN` | Muestra cantidad de vehiculos, vias y semaforos |
| `HELP` | Muestra los comandos disponibles |
| `SALIR` | Desconecta del servidor |

## Ejemplo de uso

```
> HELP
> LISTAR_VEHICULOS
> AGREGAR_VEHICULO V004 GHI-789 sedan Toyota
> ASIGNAR_VIA V004 Autopista-Norte 80
> LISTAR_SEMAFOROS
> CAMBIAR_SEMAFORO SEM-001
> ESTADO_SEMAFORO SEM-001
> DETALLE_RED Red-Nacional
> RESUMEN
> SALIR
```
