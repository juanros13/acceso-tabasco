package saf.cgmaig.usermanagement.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import saf.cgmaig.usermanagement.dto.UserCreateRequest;
import saf.cgmaig.usermanagement.dto.UserResponse;
import saf.cgmaig.usermanagement.dto.UserUpdateRequest;
import saf.cgmaig.usermanagement.entity.User;
import saf.cgmaig.usermanagement.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<UserResponse> crearUsuario(@Valid @RequestBody UserCreateRequest request,
                                                   Authentication authentication) {
        User usuario = convertirAUsuario(request);
        User usuarioCreado = userService.crearUsuario(usuario, getUserCurp(authentication));
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertirAUserResponse(usuarioCreado));
    }

    @GetMapping("/{curp}")
    @PreAuthorize("hasAuthority('USER_READ') or #curp == authentication.name")
    public ResponseEntity<UserResponse> obtenerUsuario(@PathVariable String curp) {
        return userService.obtenerUsuarioPorCurp(curp)
                .map(user -> ResponseEntity.ok(convertirAUserResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{curp}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<UserResponse> actualizarUsuario(@PathVariable String curp,
                                                        @Valid @RequestBody UserUpdateRequest request,
                                                        Authentication authentication) {
        User usuarioActualizado = convertirAUsuarioUpdate(request);
        User usuario = userService.actualizarUsuario(curp, usuarioActualizado, getUserCurp(authentication));
        
        return ResponseEntity.ok(convertirAUserResponse(usuario));
    }

    @DeleteMapping("/{curp}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable String curp,
                                                @RequestParam(required = false) String motivo,
                                                Authentication authentication) {
        String motivoBaja = motivo != null ? motivo : "Desactivado por administrador";
        userService.desactivarUsuario(curp, motivoBaja, getUserCurp(authentication));
        
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{curp}/reactivate")
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public ResponseEntity<Void> reactivarUsuario(@PathVariable String curp,
                                               Authentication authentication) {
        userService.reactivarUsuario(curp, getUserCurp(authentication));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{curp}/roles")
    @PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('ROLE_ASSIGN')")
    public ResponseEntity<Void> asignarRoles(@PathVariable String curp,
                                           @RequestBody Set<Long> roleIds,
                                           Authentication authentication) {
        userService.asignarRoles(curp, roleIds, getUserCurp(authentication));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{curp}/block")
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public ResponseEntity<Void> bloquearUsuario(@PathVariable String curp,
                                              Authentication authentication) {
        userService.bloquearUsuario(curp, getUserCurp(authentication));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{curp}/unblock")
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public ResponseEntity<Void> desbloquearUsuario(@PathVariable String curp,
                                                 Authentication authentication) {
        userService.desbloquearUsuario(curp, getUserCurp(authentication));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<Page<UserResponse>> listarUsuarios(Pageable pageable) {
        Page<User> usuarios = userService.obtenerUsuariosPaginados(pageable);
        Page<UserResponse> usuariosResponse = usuarios.map(this::convertirAUserResponse);
        
        return ResponseEntity.ok(usuariosResponse);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<Page<UserResponse>> buscarUsuarios(@RequestParam String q,
                                                           Pageable pageable) {
        Page<User> usuarios = userService.buscarUsuarios(q, pageable);
        Page<UserResponse> usuariosResponse = usuarios.map(this::convertirAUserResponse);
        
        return ResponseEntity.ok(usuariosResponse);
    }

    @GetMapping("/dependencia/{dependencia}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<UserResponse>> obtenerUsuariosPorDependencia(@PathVariable String dependencia) {
        List<User> usuarios = userService.obtenerUsuariosPorDependencia(dependencia);
        List<UserResponse> usuariosResponse = usuarios.stream()
                .map(this::convertirAUserResponse)
                .toList();
        
        return ResponseEntity.ok(usuariosResponse);
    }

    @GetMapping("/{curp}/subordinados")
    @PreAuthorize("hasAuthority('USER_READ') or #curp == authentication.name")
    public ResponseEntity<List<UserResponse>> obtenerSubordinados(@PathVariable String curp) {
        List<User> subordinados = userService.obtenerSubordinados(curp);
        List<UserResponse> subordinadosResponse = subordinados.stream()
                .map(this::convertirAUserResponse)
                .toList();
        
        return ResponseEntity.ok(subordinadosResponse);
    }

    @GetMapping("/role/{roleName}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<UserResponse>> obtenerUsuariosPorRol(@PathVariable String roleName) {
        List<User> usuarios = userService.obtenerUsuariosPorRol(roleName);
        List<UserResponse> usuariosResponse = usuarios.stream()
                .map(this::convertirAUserResponse)
                .toList();
        
        return ResponseEntity.ok(usuariosResponse);
    }

    @GetMapping("/blocked")
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public ResponseEntity<List<UserResponse>> obtenerUsuariosBloqueados() {
        List<User> usuarios = userService.obtenerUsuariosBloqueados();
        List<UserResponse> usuariosResponse = usuarios.stream()
                .map(this::convertirAUserResponse)
                .toList();
        
        return ResponseEntity.ok(usuariosResponse);
    }

    @GetMapping("/dependencias")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<String>> obtenerDependenciasActivas() {
        List<String> dependencias = userService.obtenerDependenciasActivas();
        return ResponseEntity.ok(dependencias);
    }

    private User convertirAUsuario(UserCreateRequest request) {
        User usuario = new User();
        usuario.setCurp(request.getCurp());
        usuario.setNombres(request.getNombres());
        usuario.setApellidoPaterno(request.getApellidoPaterno());
        usuario.setApellidoMaterno(request.getApellidoMaterno());
        usuario.setEmail(request.getEmail());
        usuario.setEmailPersonal(request.getEmailPersonal());
        usuario.setTelefono(request.getTelefono());
        usuario.setDependencia(request.getDependencia());
        usuario.setPuesto(request.getPuesto());
        usuario.setNivelOrganizacional(request.getNivelOrganizacional());
        usuario.setJefeInmediatoCurp(request.getJefeInmediatoCurp());
        usuario.setFechaIngreso(request.getFechaIngreso());
        
        return usuario;
    }

    private User convertirAUsuarioUpdate(UserUpdateRequest request) {
        User usuario = new User();
        usuario.setNombres(request.getNombres());
        usuario.setApellidoPaterno(request.getApellidoPaterno());
        usuario.setApellidoMaterno(request.getApellidoMaterno());
        usuario.setEmail(request.getEmail());
        usuario.setEmailPersonal(request.getEmailPersonal());
        usuario.setTelefono(request.getTelefono());
        usuario.setDependencia(request.getDependencia());
        usuario.setPuesto(request.getPuesto());
        usuario.setNivelOrganizacional(request.getNivelOrganizacional());
        usuario.setJefeInmediatoCurp(request.getJefeInmediatoCurp());
        usuario.setStatusUsuario(request.getStatusUsuario());
        
        return usuario;
    }

    private UserResponse convertirAUserResponse(User usuario) {
        UserResponse response = new UserResponse();
        response.setCurp(usuario.getCurp());
        response.setNombreCompleto(usuario.getNombreCompleto());
        response.setNombres(usuario.getNombres());
        response.setApellidoPaterno(usuario.getApellidoPaterno());
        response.setApellidoMaterno(usuario.getApellidoMaterno());
        response.setEmail(usuario.getEmail());
        response.setEmailPersonal(usuario.getEmailPersonal());
        response.setTelefono(usuario.getTelefono());
        response.setDependencia(usuario.getDependencia());
        response.setPuesto(usuario.getPuesto());
        response.setNivelOrganizacional(usuario.getNivelOrganizacional());
        response.setJefeInmediatoCurp(usuario.getJefeInmediatoCurp());
        response.setStatusUsuario(usuario.getStatusUsuario());
        response.setFechaIngreso(usuario.getFechaIngreso());
        response.setFechaBaja(usuario.getFechaBaja());
        response.setActivo(usuario.getActivo());
        response.setCuentaBloqueada(usuario.getCuentaBloqueada());
        response.setUltimoAcceso(usuario.getUltimoAcceso());
        response.setFechaCreacion(usuario.getFechaCreacion());
        response.setFechaActualizacion(usuario.getFechaActualizacion());
        
        if (usuario.getRoles() != null) {
            response.setRoles(usuario.getRoles().stream()
                    .map(role -> role.getNombre())
                    .toList());
        }
        
        response.setEsJefe(usuario.isJefe());
        response.setCantidadSubordinados(usuario.getSubordinados() != null ? 
                usuario.getSubordinados().size() : 0);
        
        return response;
    }

    private String getUserCurp(Authentication authentication) {
        return authentication.getName();
    }
}