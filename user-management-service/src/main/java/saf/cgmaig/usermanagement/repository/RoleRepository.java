package saf.cgmaig.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saf.cgmaig.usermanagement.entity.Role;
import saf.cgmaig.usermanagement.entity.RoleLevel;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNombreAndDependenciaAndActivoTrue(String nombre, String dependencia);

    List<Role> findByDependenciaAndActivoTrueOrderByNombre(String dependencia);

    List<Role> findByNivelAndActivoTrue(RoleLevel nivel);

    List<Role> findByActivoTrueOrderByDependenciaAscNombreAsc();

    @Query("SELECT r FROM Role r WHERE r.activo = true AND " +
           "(LOWER(r.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(r.descripcion) LIKE LOWER(CONCAT('%', :termino, '%')))")
    List<Role> buscarRoles(@Param("termino") String termino);

    @Query("SELECT r FROM Role r WHERE r.dependencia = :dependencia AND r.activo = true AND " +
           "(LOWER(r.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(r.descripcion) LIKE LOWER(CONCAT('%', :termino, '%')))")
    List<Role> buscarRolesPorDependencia(@Param("dependencia") String dependencia, 
                                        @Param("termino") String termino);

    @Query("SELECT r FROM Role r JOIN r.permissions p WHERE p.id = :permissionId AND r.activo = true")
    List<Role> findByPermissionId(@Param("permissionId") Long permissionId);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.id = :roleId AND u.activo = true")
    Long countUsuariosByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT DISTINCT r.dependencia FROM Role r WHERE r.activo = true ORDER BY r.dependencia")
    List<String> findAllDependenciasConRoles();

    boolean existsByNombreAndDependenciaAndActivoTrue(String nombre, String dependencia);
}