package saf.cgmaig.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saf.cgmaig.usermanagement.entity.Permission;
import saf.cgmaig.usermanagement.entity.PermissionAction;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByNombreAndRecursoAndActivoTrue(String nombre, String recurso);

    List<Permission> findBySistemaAndActivoTrueOrderByRecursoAscNombreAsc(String sistema);

    List<Permission> findByRecursoAndActivoTrue(String recurso);

    List<Permission> findByAccionAndActivoTrue(PermissionAction accion);

    List<Permission> findByActivoTrueOrderBySistemaAscRecursoAscNombreAsc();

    @Query("SELECT p FROM Permission p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(p.recurso) LIKE LOWER(CONCAT('%', :termino, '%')))")
    List<Permission> buscarPermisos(@Param("termino") String termino);

    @Query("SELECT p FROM Permission p WHERE p.sistema = :sistema AND p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(p.recurso) LIKE LOWER(CONCAT('%', :termino, '%')))")
    List<Permission> buscarPermisosPorSistema(@Param("sistema") String sistema, 
                                            @Param("termino") String termino);

    @Query("SELECT p FROM Permission p JOIN p.roles r WHERE r.id = :roleId AND p.activo = true")
    List<Permission> findByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT DISTINCT p FROM Permission p JOIN p.roles r JOIN r.users u " +
           "WHERE u.curp = :curp AND u.activo = true AND r.activo = true AND p.activo = true")
    List<Permission> findByUserCurp(@Param("curp") String curp);

    @Query("SELECT DISTINCT p.sistema FROM Permission p WHERE p.activo = true ORDER BY p.sistema")
    List<String> findAllSistemasActivos();

    @Query("SELECT DISTINCT p.recurso FROM Permission p WHERE p.sistema = :sistema AND p.activo = true ORDER BY p.recurso")
    List<String> findRecursosBySistema(@Param("sistema") String sistema);

    @Query("SELECT COUNT(r) FROM Role r JOIN r.permissions p WHERE p.id = :permissionId AND r.activo = true")
    Long countRolesByPermissionId(@Param("permissionId") Long permissionId);

    boolean existsByNombreAndRecursoAndActivoTrue(String nombre, String recurso);

    @Query("SELECT p FROM Permission p WHERE p.recurso = :recurso AND p.accion = :accion AND p.sistema = :sistema AND p.activo = true")
    Optional<Permission> findByRecursoAndAccionAndSistema(@Param("recurso") String recurso, 
                                                         @Param("accion") PermissionAction accion, 
                                                         @Param("sistema") String sistema);
}