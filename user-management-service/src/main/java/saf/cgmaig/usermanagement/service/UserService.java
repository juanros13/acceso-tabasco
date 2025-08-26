package saf.cgmaig.usermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saf.cgmaig.usermanagement.entity.*;
import saf.cgmaig.usermanagement.repository.RoleRepository;
import saf.cgmaig.usermanagement.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuditService auditService;

    @Autowired
    public UserService(UserRepository userRepository, 
                      RoleRepository roleRepository,
                      AuditService auditService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.auditService = auditService;
    }

    public User crearUsuario(User usuario, String creadoPor) {
        validarUsuario(usuario);
        
        if (userRepository.existsById(usuario.getCurp())) {
            throw new IllegalArgumentException("Ya existe un usuario con CURP: " + usuario.getCurp());
        }
        
        if (usuario.getEmail() != null && userRepository.existsByEmailAndActivoTrue(usuario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con email: " + usuario.getEmail());
        }

        usuario.setCreadoPor(creadoPor);
        usuario.setActualizadoPor(creadoPor);
        usuario.setActivo(true);
        usuario.setStatusUsuario(UserStatus.ACTIVO);
        usuario.setIntentosFallidos(0);
        usuario.setCuentaBloqueada(false);

        User usuarioCreado = userRepository.save(usuario);
        auditService.registrarAccion("USER_CREATED", creadoPor, "Usuario creado: " + usuario.getCurp());
        
        return usuarioCreado;
    }

    public User actualizarUsuario(String curp, User usuarioActualizado, String actualizadoPor) {
        User usuarioExistente = obtenerUsuarioPorCurp(curp)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + curp));

        if (!usuarioExistente.getActivo()) {
            throw new IllegalArgumentException("No se puede actualizar un usuario inactivo: " + curp);
        }

        validarUsuario(usuarioActualizado);

        if (usuarioActualizado.getEmail() != null && 
            !usuarioActualizado.getEmail().equals(usuarioExistente.getEmail()) &&
            userRepository.existsByEmailAndActivoTrue(usuarioActualizado.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con email: " + usuarioActualizado.getEmail());
        }

        actualizarCamposUsuario(usuarioExistente, usuarioActualizado, actualizadoPor);
        
        User usuarioGuardado = userRepository.save(usuarioExistente);
        auditService.registrarAccion("USER_UPDATED", actualizadoPor, "Usuario actualizado: " + curp);
        
        return usuarioGuardado;
    }

    public void desactivarUsuario(String curp, String motivoBaja, String desactivadoPor) {
        User usuario = obtenerUsuarioPorCurp(curp)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + curp));

        usuario.setActivo(false);
        usuario.setStatusUsuario(UserStatus.INACTIVO);
        usuario.setFechaBaja(LocalDate.now());
        usuario.setMotivoBaja(motivoBaja);
        usuario.setActualizadoPor(desactivadoPor);

        userRepository.save(usuario);
        auditService.registrarAccion("USER_DEACTIVATED", desactivadoPor, 
                "Usuario desactivado: " + curp + " - Motivo: " + motivoBaja);
    }

    public void reactivarUsuario(String curp, String reactivadoPor) {
        User usuario = userRepository.findById(curp)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + curp));

        if (usuario.getActivo()) {
            throw new IllegalArgumentException("El usuario ya está activo: " + curp);
        }

        usuario.setActivo(true);
        usuario.setStatusUsuario(UserStatus.ACTIVO);
        usuario.setFechaBaja(null);
        usuario.setMotivoBaja(null);
        usuario.setCuentaBloqueada(false);
        usuario.setIntentosFallidos(0);
        usuario.setActualizadoPor(reactivadoPor);

        userRepository.save(usuario);
        auditService.registrarAccion("USER_REACTIVATED", reactivadoPor, "Usuario reactivado: " + curp);
    }

    public void asignarRoles(String curp, Set<Long> roleIds, String asignadoPor) {
        User usuario = obtenerUsuarioPorCurp(curp)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + curp));

        Set<Role> nuevosRoles = Set.copyOf(roleRepository.findAllById(roleIds));
        
        if (nuevosRoles.size() != roleIds.size()) {
            throw new IllegalArgumentException("Algunos roles especificados no existen");
        }

        usuario.setRoles(nuevosRoles);
        usuario.setActualizadoPor(asignadoPor);

        userRepository.save(usuario);
        auditService.registrarAccion("ROLES_ASSIGNED", asignadoPor, 
                "Roles asignados al usuario: " + curp + " - Roles: " + roleIds);
    }

    public void bloquearUsuario(String curp, String bloqueadoPor) {
        User usuario = obtenerUsuarioPorCurp(curp)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + curp));

        usuario.setCuentaBloqueada(true);
        usuario.setFechaBloqueo(LocalDateTime.now());
        usuario.setActualizadoPor(bloqueadoPor);

        userRepository.save(usuario);
        auditService.registrarAccion("USER_BLOCKED", bloqueadoPor, "Usuario bloqueado: " + curp);
    }

    public void desbloquearUsuario(String curp, String desbloqueadoPor) {
        User usuario = obtenerUsuarioPorCurp(curp)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + curp));

        usuario.setCuentaBloqueada(false);
        usuario.setFechaBloqueo(null);
        usuario.setIntentosFallidos(0);
        usuario.setActualizadoPor(desbloqueadoPor);

        userRepository.save(usuario);
        auditService.registrarAccion("USER_UNBLOCKED", desbloqueadoPor, "Usuario desbloqueado: " + curp);
    }

    public void actualizarUltimoAcceso(String curp) {
        Optional<User> usuarioOpt = obtenerUsuarioPorCurp(curp);
        if (usuarioOpt.isPresent()) {
            User usuario = usuarioOpt.get();
            usuario.setUltimoAcceso(LocalDateTime.now());
            userRepository.save(usuario);
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> obtenerUsuarioPorCurp(String curp) {
        return userRepository.findByCurpAndActivoTrue(curp);
    }

    @Transactional(readOnly = true)
    public List<User> obtenerUsuariosPorDependencia(String dependencia) {
        return userRepository.findByDependenciaAndActivoTrue(dependencia);
    }

    @Transactional(readOnly = true)
    public Page<User> obtenerUsuariosPaginados(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<User> buscarUsuarios(String termino, Pageable pageable) {
        return userRepository.buscarUsuarios(termino, pageable);
    }

    @Transactional(readOnly = true)
    public List<User> obtenerSubordinados(String jefeCurp) {
        return userRepository.findByJefeInmediatoCurpAndActivoTrue(jefeCurp);
    }

    @Transactional(readOnly = true)
    public List<User> obtenerUsuariosPorRol(String nombreRol) {
        return userRepository.findByRolNombre(nombreRol);
    }

    @Transactional(readOnly = true)
    public List<User> obtenerUsuariosBloqueados() {
        return userRepository.findUsuariosBloqueados();
    }

    @Transactional(readOnly = true)
    public List<String> obtenerDependenciasActivas() {
        return userRepository.findAllDependenciasActivas();
    }

    private void validarUsuario(User usuario) {
        if (usuario.getCurp() == null || usuario.getCurp().trim().isEmpty()) {
            throw new IllegalArgumentException("CURP es obligatorio");
        }

        if (usuario.getNombres() == null || usuario.getNombres().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombres son obligatorios");
        }

        if (!usuario.getCurp().matches("^[A-Z]{1}[AEIOUX]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[HM]{1}[A-Z]{2}[BCDFGHJKLMNPQRSTVWXYZ]{3}[0-9A-Z]{1}$")) {
            throw new IllegalArgumentException("CURP no tiene formato válido");
        }
    }

    private void actualizarCamposUsuario(User existente, User actualizado, String actualizadoPor) {
        existente.setNombres(actualizado.getNombres());
        existente.setApellidoPaterno(actualizado.getApellidoPaterno());
        existente.setApellidoMaterno(actualizado.getApellidoMaterno());
        existente.setEmail(actualizado.getEmail());
        existente.setEmailPersonal(actualizado.getEmailPersonal());
        existente.setTelefono(actualizado.getTelefono());
        existente.setDependencia(actualizado.getDependencia());
        existente.setPuesto(actualizado.getPuesto());
        existente.setNivelOrganizacional(actualizado.getNivelOrganizacional());
        existente.setJefeInmediatoCurp(actualizado.getJefeInmediatoCurp());
        
        if (actualizado.getStatusUsuario() != null) {
            existente.setStatusUsuario(actualizado.getStatusUsuario());
        }
        
        existente.setActualizadoPor(actualizadoPor);
    }
}