package saf.cgmaig.usermanagement.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import saf.cgmaig.usermanagement.dto.RoleCreateRequest;
import saf.cgmaig.usermanagement.dto.RoleResponse;
import saf.cgmaig.usermanagement.dto.RoleUpdateRequest;
import saf.cgmaig.usermanagement.entity.Permission;
import saf.cgmaig.usermanagement.entity.Role;
import saf.cgmaig.usermanagement.entity.RoleLevel;
import saf.cgmaig.usermanagement.service.RoleService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    public ResponseEntity<RoleResponse> crearRole(@Valid @RequestBody RoleCreateRequest request,
                                                Authentication authentication) {
        Role role = convertirARole(request);
        Role roleCreado = roleService.crearRole(role, getUserCurp(authentication));
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertirARoleResponse(roleCreado));
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<RoleResponse> obtenerRole(@PathVariable Long roleId) {
        return roleService.obtenerRolePorId(roleId)
                .map(role -> ResponseEntity.ok(convertirARoleResponse(role)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{roleId}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public ResponseEntity<RoleResponse> actualizarRole(@PathVariable Long roleId,
                                                     @Valid @RequestBody RoleUpdateRequest request,
                                                     Authentication authentication) {
        Role roleActualizado = convertirARoleUpdate(request);
        Role role = roleService.actualizarRole(roleId, roleActualizado, getUserCurp(authentication));
        
        return ResponseEntity.ok(convertirARoleResponse(role));
    }

    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    public ResponseEntity<Void> desactivarRole(@PathVariable Long roleId,
                                             Authentication authentication) {
        roleService.desactivarRole(roleId, getUserCurp(authentication));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_ASSIGN')")
    public ResponseEntity<Void> asignarPermisos(@PathVariable Long roleId,
                                              @RequestBody Set<Long> permissionIds,
                                              Authentication authentication) {
        roleService.asignarPermisos(roleId, permissionIds, getUserCurp(authentication));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_ASSIGN')")
    public ResponseEntity<Void> agregarPermiso(@PathVariable Long roleId,
                                             @PathVariable Long permissionId,
                                             Authentication authentication) {
        roleService.agregarPermiso(roleId, permissionId, getUserCurp(authentication));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PERMISSION_ASSIGN')")
    public ResponseEntity<Void> removerPermiso(@PathVariable Long roleId,
                                             @PathVariable Long permissionId,
                                             Authentication authentication) {
        roleService.removerPermiso(roleId, permissionId, getUserCurp(authentication));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<List<RoleResponse>> listarRoles() {
        List<Role> roles = roleService.obtenerTodosLosRoles();
        List<RoleResponse> rolesResponse = roles.stream()
                .map(this::convertirARoleResponse)
                .toList();
        
        return ResponseEntity.ok(rolesResponse);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<List<RoleResponse>> buscarRoles(@RequestParam String q) {
        List<Role> roles = roleService.buscarRoles(q);
        List<RoleResponse> rolesResponse = roles.stream()
                .map(this::convertirARoleResponse)
                .toList();
        
        return ResponseEntity.ok(rolesResponse);
    }

    @GetMapping("/dependencia/{dependencia}")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<List<RoleResponse>> obtenerRolesPorDependencia(@PathVariable String dependencia) {
        List<Role> roles = roleService.obtenerRolesPorDependencia(dependencia);
        List<RoleResponse> rolesResponse = roles.stream()
                .map(this::convertirARoleResponse)
                .toList();
        
        return ResponseEntity.ok(rolesResponse);
    }

    @GetMapping("/nivel/{nivel}")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<List<RoleResponse>> obtenerRolesPorNivel(@PathVariable RoleLevel nivel) {
        List<Role> roles = roleService.obtenerRolesPorNivel(nivel);
        List<RoleResponse> rolesResponse = roles.stream()
                .map(this::convertirARoleResponse)
                .toList();
        
        return ResponseEntity.ok(rolesResponse);
    }

    @GetMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('ROLE_READ') or hasAuthority('PERMISSION_READ')")
    public ResponseEntity<List<Permission>> obtenerPermisosPorRole(@PathVariable Long roleId) {
        List<Permission> permisos = roleService.obtenerPermisosPorRole(roleId);
        return ResponseEntity.ok(permisos);
    }

    @GetMapping("/dependencias")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<List<String>> obtenerDependenciasConRoles() {
        List<String> dependencias = roleService.obtenerDependenciasConRoles();
        return ResponseEntity.ok(dependencias);
    }

    private Role convertirARole(RoleCreateRequest request) {
        Role role = new Role();
        role.setNombre(request.getNombre());
        role.setDescripcion(request.getDescripcion());
        role.setDependencia(request.getDependencia());
        role.setNivel(request.getNivel());
        
        return role;
    }

    private Role convertirARoleUpdate(RoleUpdateRequest request) {
        Role role = new Role();
        role.setNombre(request.getNombre());
        role.setDescripcion(request.getDescripcion());
        role.setDependencia(request.getDependencia());
        role.setNivel(request.getNivel());
        
        return role;
    }

    private RoleResponse convertirARoleResponse(Role role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setNombre(role.getNombre());
        response.setDescripcion(role.getDescripcion());
        response.setDependencia(role.getDependencia());
        response.setNivel(role.getNivel());
        response.setActivo(role.getActivo());
        response.setFechaCreacion(role.getFechaCreacion());
        response.setFechaActualizacion(role.getFechaActualizacion());
        
        if (role.getUsers() != null) {
            response.setCantidadUsuarios(role.getUsers().size());
        }
        
        if (role.getPermissions() != null) {
            response.setCantidadPermisos(role.getPermissions().size());
        }
        
        return response;
    }

    private String getUserCurp(Authentication authentication) {
        return authentication.getName();
    }
}