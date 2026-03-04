package protocol;

import java.io.Serializable;

/**
 * Protocolo de comunicacion Cliente-Servidor.
 * Representa un mensaje de solicitud del cliente al servidor.
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private String command;
    private String[] params;

    public Request(String command, String... params) {
        this.command = command;
        this.params = params;
    }

    public String getCommand() { return command; }
    public String[] getParams() { return params; }

    public String getParam(int index) {
        if (index >= 0 && index < params.length) {
            return params[index];
        }
        return "";
    }
}
