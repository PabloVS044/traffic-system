package protocol;

import java.io.Serializable;

/**
 * Protocolo de comunicacion Cliente-Servidor.
 * Representa la respuesta del servidor al cliente.
 */
public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }

    @Override
    public String toString() {
        return (success ? "[OK] " : "[ERROR] ") + message;
    }
}
