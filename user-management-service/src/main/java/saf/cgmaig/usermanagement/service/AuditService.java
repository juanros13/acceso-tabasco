package saf.cgmaig.usermanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AuditService {

    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void registrarAccion(String accion, String usuario, String detalle) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] Usuario: %s | Acción: %s | Detalle: %s", 
                                         timestamp, usuario, accion, detalle);
        
        auditLogger.info(logMessage);
    }

    public void registrarAccesoSistema(String usuario, String ip, boolean exitoso) {
        String resultado = exitoso ? "EXITOSO" : "FALLIDO";
        String detalle = String.format("Acceso al sistema desde IP: %s - Resultado: %s", ip, resultado);
        registrarAccion("SYSTEM_ACCESS", usuario, detalle);
    }

    public void registrarCambioPassword(String usuario, boolean exitoso) {
        String resultado = exitoso ? "EXITOSO" : "FALLIDO";
        registrarAccion("PASSWORD_CHANGE", usuario, "Cambio de contraseña - Resultado: " + resultado);
    }

    public void registrarOperacionMasiva(String accion, String usuario, int cantidadAfectada) {
        String detalle = String.format("Operación masiva - Registros afectados: %d", cantidadAfectada);
        registrarAccion(accion, usuario, detalle);
    }
}