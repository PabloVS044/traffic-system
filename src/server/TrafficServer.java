package server;

import protocol.Request;
import protocol.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * CLIENTE-SERVIDOR - Componente Servidor
 * Escucha conexiones de clientes via sockets TCP y delega
 * el procesamiento de solicitudes al RequestHandler.
 *
 * Principio SRP: El servidor solo se encarga de la comunicacion de red.
 * La logica de negocio esta delegada al RequestHandler.
 */
public class TrafficServer {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        RequestHandler handler = new RequestHandler();

        System.out.println("========================================");
        System.out.println(" Sistema de Gestion de Transito Vial");
        System.out.println(" Servidor iniciado en puerto " + PORT);
        System.out.println("========================================");
        System.out.println("Esperando conexiones de clientes...\n");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                // Manejar cada cliente en un hilo separado
                new Thread(() -> handleClient(clientSocket, handler)).start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket, RequestHandler handler) {
        try (
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            while (true) {
                Request request = (Request) in.readObject();

                if ("SALIR".equalsIgnoreCase(request.getCommand())) {
                    out.writeObject(new Response(true, "Desconectado del servidor. Hasta luego!"));
                    out.flush();
                    break;
                }

                System.out.println("Solicitud recibida: " + request.getCommand());
                Response response = handler.handleRequest(request);
                out.writeObject(response);
                out.flush();
            }
        } catch (EOFException e) {
            System.out.println("Cliente desconectado.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Conexion con cliente terminada: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // ignorar
            }
        }
    }
}
