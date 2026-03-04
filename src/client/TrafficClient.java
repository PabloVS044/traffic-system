package client;

import protocol.Request;
import protocol.Response;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * CLIENTE-SERVIDOR - Componente Cliente
 * Se conecta al servidor via socket TCP, envia comandos del usuario
 * y muestra las respuestas recibidas.
 *
 * Principio SRP: Solo se encarga de la interfaz de usuario y comunicacion.
 */
public class TrafficClient {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Sistema de Gestion de Transito Vial");
        System.out.println("         Cliente Interactivo");
        System.out.println("========================================");
        System.out.println("Conectando al servidor...\n");

        try (
            Socket socket = new Socket(HOST, PORT);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Conectado al servidor exitosamente!");
            System.out.println("Escriba HELP para ver los comandos disponibles.");
            System.out.println("Escriba SALIR para desconectarse.\n");

            while (true) {
                System.out.print("\n> ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) continue;

                // Parsear el comando y parametros
                String[] parts = input.split("\\s+");
                String command = parts[0];
                String[] params = new String[parts.length - 1];
                System.arraycopy(parts, 1, params, 0, params.length);

                // Enviar solicitud al servidor
                Request request = new Request(command, params);
                out.writeObject(request);
                out.flush();

                // Recibir respuesta
                Response response = (Response) in.readObject();
                System.out.println("\n" + response);

                if ("SALIR".equalsIgnoreCase(command)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error de conexion: " + e.getMessage());
            System.err.println("Asegurese de que el servidor este ejecutandose.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al recibir respuesta: " + e.getMessage());
        }
    }
}
