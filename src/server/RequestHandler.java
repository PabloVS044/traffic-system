package server;

import builder.HighwayBuilder;
import model.road.*;
import model.traffic.*;
import model.vehicle.Vehicle;
import protocol.Request;
import protocol.Response;
import repository.*;

/**
 * CLIENTE-SERVIDOR - Logica del servidor.
 * Procesa las solicitudes del cliente y coordina los patrones de diseno.
 *
 * Aqui se integran todos los patrones:
 * - Builder: para crear segmentos de carretera
 * - Composite: para gestionar la red vial jerarquica
 * - State: para manejar semaforos
 * - Repository: para almacenar y consultar entidades
 */
public class RequestHandler {
    private final VehicleRepository vehicleRepo;
    private final RoadRepository roadRepo;
    private final TrafficLightRepository trafficLightRepo;

    public RequestHandler() {
        this.vehicleRepo = new VehicleRepository();
        this.roadRepo = new RoadRepository();
        this.trafficLightRepo = new TrafficLightRepository();
        initializeDefaultData();
    }

    private void initializeDefaultData() {
        // BUILDER: Crear segmentos de carretera usando el patron Builder
        RoadSegment autopistaNorte = new HighwayBuilder()
                .withName("Autopista Norte KM 0-15")
                .withType("autopista")
                .withLength(15.0)
                .withLanes(4)
                .withSpeedLimit(120)
                .build();

        RoadSegment autopistaSur = new HighwayBuilder()
                .withName("Autopista Norte KM 15-30")
                .withType("autopista")
                .withLength(15.0)
                .withLanes(4)
                .withSpeedLimit(120)
                .build();

        RoadSegment avenidaCentral = new HighwayBuilder()
                .withName("Avenida Central")
                .withType("avenida")
                .withLength(5.0)
                .withLanes(3)
                .withSpeedLimit(60)
                .build();

        RoadSegment calleResidencial = new HighwayBuilder()
                .withName("Calle Residencial 1")
                .withType("calle")
                .withLength(2.0)
                .withLanes(2)
                .withSpeedLimit(30)
                .build();

        RoadSegment carreteraRural = new HighwayBuilder()
                .withName("Carretera Rural Este")
                .withType("carretera")
                .withLength(25.0)
                .withLanes(2)
                .withSpeedLimit(80)
                .build();

        // COMPOSITE: Crear redes viales jerarquicas
        RoadNetwork autopista = new RoadNetwork("Autopista Norte Completa", "autopista");
        autopista.addComponent(autopistaNorte);
        autopista.addComponent(autopistaSur);

        RoadNetwork zonaCentro = new RoadNetwork("Zona Centro", "red_urbana");
        zonaCentro.addComponent(avenidaCentral);
        zonaCentro.addComponent(calleResidencial);

        RoadNetwork redNacional = new RoadNetwork("Red Vial Nacional", "red_nacional");
        redNacional.addComponent(autopista);
        redNacional.addComponent(zonaCentro);
        redNacional.addComponent(carreteraRural);

        // REPOSITORY: Guardar en repositorios
        roadRepo.save(autopistaNorte);
        roadRepo.save(autopistaSur);
        roadRepo.save(avenidaCentral);
        roadRepo.save(calleResidencial);
        roadRepo.save(carreteraRural);
        roadRepo.save(autopista);
        roadRepo.save(zonaCentro);
        roadRepo.save(redNacional);

        // STATE: Crear semaforos (inician en ROJO por defecto)
        trafficLightRepo.save(new TrafficLight("SEM-001", "Autopista Norte KM 5"));
        trafficLightRepo.save(new TrafficLight("SEM-002", "Avenida Central / Calle 10"));
        trafficLightRepo.save(new TrafficLight("SEM-003", "Entrada Zona Residencial"));

        // Vehiculos iniciales
        vehicleRepo.save(new Vehicle("V-001", "ABC-123", "sedan", "Toyota"));
        vehicleRepo.save(new Vehicle("V-002", "XYZ-789", "camion", "Mercedes"));
        vehicleRepo.save(new Vehicle("V-003", "DEF-456", "motocicleta", "Yamaha"));
    }

    public Response handleRequest(Request request) {
        String cmd = request.getCommand().toUpperCase();

        switch (cmd) {
            case "LISTAR_VEHICULOS":
                return listVehicles();
            case "AGREGAR_VEHICULO":
                return addVehicle(request);
            case "ASIGNAR_VIA":
                return assignRoad(request);
            case "ELIMINAR_VEHICULO":
                return deleteVehicle(request);
            case "LISTAR_VIAS":
                return listRoads();
            case "CREAR_VIA":
                return createRoad(request);
            case "CREAR_RED":
                return createNetwork(request);
            case "AGREGAR_A_RED":
                return addToNetwork(request);
            case "DETALLE_RED":
                return networkDetail(request);
            case "LISTAR_SEMAFOROS":
                return listTrafficLights();
            case "CAMBIAR_SEMAFORO":
                return changeTrafficLight(request);
            case "ESTADO_SEMAFORO":
                return trafficLightStatus(request);
            case "RESUMEN":
                return systemSummary();
            case "HELP":
                return showHelp();
            default:
                return new Response(false, "Comando no reconocido: " + request.getCommand() +
                        ". Escriba HELP para ver los comandos disponibles.");
        }
    }

    private Response listVehicles() {
        var vehicles = vehicleRepo.findAll();
        if (vehicles.isEmpty()) {
            return new Response(true, "No hay vehiculos registrados.");
        }
        StringBuilder sb = new StringBuilder("=== VEHICULOS REGISTRADOS ===\n");
        for (var v : vehicles) {
            sb.append("  ").append(v).append("\n");
        }
        sb.append("Total: ").append(vehicles.size());
        return new Response(true, sb.toString());
    }

    private Response addVehicle(Request req) {
        if (req.getParams().length < 4) {
            return new Response(false, "Uso: AGREGAR_VEHICULO <id> <placa> <tipo> <marca>");
        }
        String id = req.getParam(0);
        if (vehicleRepo.findById(id).isPresent()) {
            return new Response(false, "Ya existe un vehiculo con ID: " + id);
        }
        Vehicle v = new Vehicle(id, req.getParam(1), req.getParam(2), req.getParam(3));
        vehicleRepo.save(v);
        return new Response(true, "Vehiculo registrado: " + v);
    }

    private Response assignRoad(Request req) {
        if (req.getParams().length < 3) {
            return new Response(false, "Uso: ASIGNAR_VIA <vehiculoId> <nombreVia> <velocidad>");
        }
        var vehicleOpt = vehicleRepo.findById(req.getParam(0));
        if (vehicleOpt.isEmpty()) {
            return new Response(false, "Vehiculo no encontrado: " + req.getParam(0));
        }
        Vehicle v = vehicleOpt.get();
        v.setCurrentRoad(req.getParam(1));
        try {
            v.setSpeed(Double.parseDouble(req.getParam(2)));
        } catch (NumberFormatException e) {
            return new Response(false, "Velocidad invalida: " + req.getParam(2));
        }
        vehicleRepo.save(v);
        return new Response(true, "Vehiculo asignado: " + v);
    }

    private Response deleteVehicle(Request req) {
        if (req.getParams().length < 1) {
            return new Response(false, "Uso: ELIMINAR_VEHICULO <id>");
        }
        String id = req.getParam(0);
        if (vehicleRepo.findById(id).isEmpty()) {
            return new Response(false, "Vehiculo no encontrado: " + id);
        }
        vehicleRepo.deleteById(id);
        return new Response(true, "Vehiculo eliminado: " + id);
    }

    private Response listRoads() {
        var roads = roadRepo.findAll();
        if (roads.isEmpty()) {
            return new Response(true, "No hay vias registradas.");
        }
        StringBuilder sb = new StringBuilder("=== COMPONENTES VIALES ===\n");
        for (var r : roads) {
            sb.append("  ").append(r.getDetails("  ")).append("\n");
        }
        return new Response(true, sb.toString());
    }

    // BUILDER: El cliente puede crear vias usando el patron Builder
    private Response createRoad(Request req) {
        if (req.getParams().length < 5) {
            return new Response(false, "Uso: CREAR_VIA <nombre> <tipo> <longitudKm> <carriles> <limiteVelocidad>");
        }
        try {
            RoadSegment segment = new HighwayBuilder()
                    .withName(req.getParam(0))
                    .withType(req.getParam(1))
                    .withLength(Double.parseDouble(req.getParam(2)))
                    .withLanes(Integer.parseInt(req.getParam(3)))
                    .withSpeedLimit(Integer.parseInt(req.getParam(4)))
                    .build();
            roadRepo.save(segment);
            return new Response(true, "Via creada: " + segment);
        } catch (NumberFormatException e) {
            return new Response(false, "Parametros numericos invalidos.");
        }
    }

    // COMPOSITE: Crear una red (composite) vacia
    private Response createNetwork(Request req) {
        if (req.getParams().length < 2) {
            return new Response(false, "Uso: CREAR_RED <nombre> <tipo>");
        }
        RoadNetwork network = new RoadNetwork(req.getParam(0), req.getParam(1));
        roadRepo.save(network);
        return new Response(true, "Red vial creada: " + network.getName());
    }

    // COMPOSITE: Agregar un componente a una red existente
    private Response addToNetwork(Request req) {
        if (req.getParams().length < 2) {
            return new Response(false, "Uso: AGREGAR_A_RED <nombreRed> <nombreComponente>");
        }
        var networkOpt = roadRepo.findById(req.getParam(0));
        var componentOpt = roadRepo.findById(req.getParam(1));

        if (networkOpt.isEmpty()) {
            return new Response(false, "Red no encontrada: " + req.getParam(0));
        }
        if (componentOpt.isEmpty()) {
            return new Response(false, "Componente no encontrado: " + req.getParam(1));
        }
        if (!(networkOpt.get() instanceof RoadNetwork)) {
            return new Response(false, req.getParam(0) + " no es una red (es un segmento).");
        }

        RoadNetwork network = (RoadNetwork) networkOpt.get();
        network.addComponent(componentOpt.get());
        return new Response(true, "Componente '" + req.getParam(1) +
                "' agregado a red '" + network.getName() + "'");
    }

    // COMPOSITE: Ver detalle jerarquico de una red
    private Response networkDetail(Request req) {
        if (req.getParams().length < 1) {
            return new Response(false, "Uso: DETALLE_RED <nombre>");
        }
        var opt = roadRepo.findById(req.getParam(0));
        if (opt.isEmpty()) {
            return new Response(false, "Componente no encontrado: " + req.getParam(0));
        }
        return new Response(true, "=== DETALLE ===\n" + opt.get().getDetails(""));
    }

    // STATE: Listar semaforos con su estado actual
    private Response listTrafficLights() {
        var lights = trafficLightRepo.findAll();
        if (lights.isEmpty()) {
            return new Response(true, "No hay semaforos registrados.");
        }
        StringBuilder sb = new StringBuilder("=== SEMAFOROS ===\n");
        for (var l : lights) {
            sb.append("  ").append(l.getStatus()).append("\n");
        }
        return new Response(true, sb.toString());
    }

    // STATE: Cambiar estado de un semaforo (transicion automatica)
    private Response changeTrafficLight(Request req) {
        if (req.getParams().length < 1) {
            return new Response(false, "Uso: CAMBIAR_SEMAFORO <id>");
        }
        var opt = trafficLightRepo.findById(req.getParam(0));
        if (opt.isEmpty()) {
            return new Response(false, "Semaforo no encontrado: " + req.getParam(0));
        }
        TrafficLight light = opt.get();
        String before = light.getCurrentState().getColor();
        light.changeState();
        String after = light.getCurrentState().getColor();
        return new Response(true, "Semaforo " + light.getId() +
                ": " + before + " -> " + after +
                " (" + light.getCurrentState().getAction() + ")");
    }

    private Response trafficLightStatus(Request req) {
        if (req.getParams().length < 1) {
            return new Response(false, "Uso: ESTADO_SEMAFORO <id>");
        }
        var opt = trafficLightRepo.findById(req.getParam(0));
        if (opt.isEmpty()) {
            return new Response(false, "Semaforo no encontrado: " + req.getParam(0));
        }
        return new Response(true, opt.get().getStatus());
    }

    private Response systemSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("   RESUMEN DEL SISTEMA DE TRANSITO\n");
        sb.append("========================================\n");
        sb.append("  Vehiculos registrados: ").append(vehicleRepo.count()).append("\n");
        sb.append("  Componentes viales:    ").append(roadRepo.count()).append("\n");
        sb.append("  Semaforos:             ").append(trafficLightRepo.count()).append("\n");
        sb.append("========================================");
        return new Response(true, sb.toString());
    }

    private Response showHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== COMANDOS DISPONIBLES ===\n\n");
        sb.append("--- Vehiculos (Repository) ---\n");
        sb.append("  LISTAR_VEHICULOS\n");
        sb.append("  AGREGAR_VEHICULO <id> <placa> <tipo> <marca>\n");
        sb.append("  ASIGNAR_VIA <vehiculoId> <nombreVia> <velocidad>\n");
        sb.append("  ELIMINAR_VEHICULO <id>\n\n");
        sb.append("--- Vias y Redes (Builder + Composite) ---\n");
        sb.append("  LISTAR_VIAS\n");
        sb.append("  CREAR_VIA <nombre> <tipo> <longitudKm> <carriles> <limiteVelocidad>\n");
        sb.append("  CREAR_RED <nombre> <tipo>\n");
        sb.append("  AGREGAR_A_RED <nombreRed> <nombreComponente>\n");
        sb.append("  DETALLE_RED <nombre>\n\n");
        sb.append("--- Semaforos (State) ---\n");
        sb.append("  LISTAR_SEMAFOROS\n");
        sb.append("  CAMBIAR_SEMAFORO <id>\n");
        sb.append("  ESTADO_SEMAFORO <id>\n\n");
        sb.append("--- Sistema ---\n");
        sb.append("  RESUMEN\n");
        sb.append("  HELP\n");
        sb.append("  SALIR\n");
        return new Response(true, sb.toString());
    }
}
