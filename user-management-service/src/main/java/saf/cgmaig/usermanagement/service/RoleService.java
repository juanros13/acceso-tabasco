package saf.cgmaig.usermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saf.cgmaig.usermanagement.entity.Permission;
import saf.cgmaig.usermanagement.entity.Role;
import saf.cgmaig.usermanagement.entity.RoleLevel;
import saf.cgmaig.usermanagement.repository.PermissionRepository;
import saf.cgmaig.usermanagement.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final AuditService auditService;

    @Autowired
    public RoleService(RoleRepository roleRepository, 
                      PermissionRepository permissionRepository,
                      AuditService auditService) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.auditService = auditService;
    }

    public Role crearRole(Role role, String creadoPor) {
        validarRole(role);
        
        if (roleRepository.existsByNombreAndDependenciaAndActivoTrue(role.getNombre(), role.getDependencia())) {
            throw new IllegalArgumentException(
                String.format("Ya existe un rol con nombre '%s' en la dependencia '%s'", 
                            role.getNombre(), role.getDependencia()));
        }

        role.setCreadoPor(creadoPor);
        role.setActualizadoPor(creadoPor);
        role.setActivo(true);

        Role roleCreado = roleRepository.save(role);
        auditService.registrarAccion("ROLE_CREATED", creadoPor, 
                "Rol creado: " + role.getNombre() + " - Dependencia: " + role.getDependencia());
        
        return roleCreado;
    }

    public Role actualizarRole(Long roleId, Role roleActualizado, String actualizadoPor) {
        Role roleExistente = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + roleId));

        if (!roleExistente.getActivo()) {
            throw new IllegalArgumentException("No se puede actualizar un rol inactivo: " + roleId);
        }

        validarRole(roleActualizado);

        if (!roleExistente.getNombre().equals(roleActualizado.getNombre()) ||
            !roleExistente.getDependencia().equals(roleActualizado.getDependencia())) {
            if (roleRepository.existsByNombreAndDependenciaAndActivoTrue(
                    roleActualizado.getNombre(), roleActualizado.getDependencia())) {
                throw new IllegalArgumentException(
                    String.format("Ya existe un rol con nombre '%s' en la dependencia '%s'", 
                                roleActualizado.getNombre(), roleActualizado.getDependencia()));
            }
        }

        roleExistente.setNombre(roleActualizado.getNombre());
        roleExistente.setDescripcion(roleActualizado.getDescripcion());
        roleExistente.setDependencia(roleActualizado.getDependencia());
        roleExistente.setNivel(roleActualizado.getNivel());
        roleExistente.setActualizadoPor(actualizadoPor);

        Role roleGuardado = roleRepository.save(roleExistente);
        auditService.registrarAccion("ROLE_UPDATED", actualizadoPor, "Rol actualizado: " + roleId);
        
        return roleGuardado;
    }

    public void desactivarRole(Long roleId, String desactivadoPor) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + roleId));

        Long usuariosAsignados = roleRepository.countUsuariosByRoleId(roleId);
        if (usuariosAsignados > 0) {
            throw new IllegalArgumentException(
                String.format("No se puede desactivar el rol. Tiene %d usuarios asignados", usuariosAsignados));
        }

        role.setActivo(false);
        role.setActualizadoPor(desactivadoPor);

        roleRepository.save(role);
        auditService.registrarAccion("ROLE_DEACTIVATED", desactivadoPor, "Rol desactivado: " + roleId);
    }

    public void asignarPermisos(Long roleId, Set<Long> permissionIds, String asignadoPor) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + roleId));

        if (!role.getActivo()) {
            throw new IllegalArgumentException("No se pueden asignar permisos a un rol inactivo: " + roleId);
        }

        Set<Permission> nuevosPermisos = Set.copyOf(permissionRepository.findAllById(permissionIds));
        
        if (nuevosPermisos.size() != permissionIds.size()) {
            throw new IllegalArgumentException("Algunos permisos especificados no existen");
        }

        role.setPermissions(nuevosPermisos);
        role.setActualizadoPor(asignadoPor);

        roleRepository.save(role);
        auditService.registrarAccion("PERMISSIONS_ASSIGNED", asignadoPor, 
                "Permisos asignados al rol: " + roleId + " - Permisos: " + permissionIds);
    }

    public void agregarPermiso(Long roleId, Long permissionId, String asignadoPor) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + roleId));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Permiso no encontrado: " + permissionId));

        if (!role.getActivo() || !permission.getActivo()) {
            throw new IllegalArgumentException("No se puede asignar permiso inactivo a rol inactivo");
        }

        role.agregarPermiso(permission);
        role.setActualizadoPor(asignadoPor);

        roleRepository.save(role);
        auditService.registrarAccion("PERMISSION_ADDED", asignadoPor, 
                "Permiso agregado al rol: " + roleId + " - Permiso: " + permissionId);
    }

    public void removerPermiso(Long roleId, Long permissionId, String removidoPor) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + roleId));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Permiso no encontrado: " + permissionId));

        role.removerPermiso(permission);
        role.setActualizadoPor(removidoPor);

        roleRepository.save(role);
        auditService.registrarAccion("PERMISSION_REMOVED", removidoPor, 
                "Permiso removido del rol: " + roleId + " - Permiso: " + permissionId);
    }

    @Transactional(readOnly = true)
    public Optional<Role> obtenerRolePorId(Long roleId) {
        return roleRepository.findById(roleId);
    }

    @Transactional(readOnly = true)
    public Optional<Role> obtenerRolePorNombreYDependencia(String nombre, String dependencia) {
        return roleRepository.findByNombreAndDependenciaAndActivoTrue(nombre, dependencia);
    }

    @Transactional(readOnly = true)
    public List<Role> obtenerRolesPorDependencia(String dependencia) {
        return roleRepository.findByDependenciaAndActivoTrueOrderByNombre(dependencia);
    }

    @Transactional(readOnly = true)
    public List<Role> obtenerRolesPorNivel(RoleLevel nivel) {
        return roleRepository.findByNivelAndActivoTrue(nivel);
    }

    @Transactional(readOnly = true)
    public List<Role> obtenerTodosLosRoles() {
        return roleRepository.findByActivoTrueOrderByDependenciaAscNombreAsc();
    }

    @Transactional(readOnly = true)
    public List<Role> buscarRoles(String termino) {
        return roleRepository.buscarRoles(termino);
    }

    @Transactional(readOnly = true)
    public List<Role> buscarRolesPorDependencia(String dependencia, String termino) {
        return roleRepository.buscarRolesPorDependencia(dependencia, termino);
    }

    @Transactional(readOnly = true)
    public List<String> obtenerDependenciasConRoles() {
        return roleRepository.findAllDependenciasConRoles();
    }

    @Transactional(readOnly = true)
    public List<Permission> obtenerPermisosPorRole(Long roleId) {
        return permissionRepository.findByRoleId(roleId);
    }

    private void validarRole(Role role) {
        if (role.getNombre() == null || role.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre del rol es obligatorio");
        }

        if (role.getDependencia() == null || role.getDependencia().trim().isEmpty()) {
            throw new IllegalArgumentException("Dependencia del rol es obligatoria");
        }

        if (role.getNivel() == null) {
            role.setNivel(RoleLevel.OPERATIVO);
        }
    }
}