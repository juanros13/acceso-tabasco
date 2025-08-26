package saf.cgmaig.usermanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import saf.cgmaig.usermanagement.entity.User;
import saf.cgmaig.usermanagement.entity.UserStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByCurpAndActivoTrue(String curp);

    List<User> findByDependenciaAndActivoTrue(String dependencia);

    Page<User> findByDependenciaAndActivoTrue(String dependencia, Pageable pageable);

    List<User> findByJefeInmediatoCurpAndActivoTrue(String jefeInmediatoCurp);

    List<User> findByStatusUsuarioAndActivoTrue(UserStatus statusUsuario);

    @Query("SELECT u FROM User u WHERE u.activo = true AND " +
           "(LOWER(u.nombres) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(u.apellidoPaterno) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(u.apellidoMaterno) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :termino, '%')))")
    Page<User> buscarUsuarios(@Param("termino") String termino, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.dependencia = :dependencia AND u.activo = true AND " +
           "(LOWER(u.nombres) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(u.apellidoPaterno) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(u.apellidoMaterno) LIKE LOWER(CONCAT('%', :termino, '%')))")
    Page<User> buscarUsuariosPorDependencia(@Param("dependencia") String dependencia, 
                                           @Param("termino") String termino, 
                                           Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.nombre = :rolNombre AND u.activo = true")
    List<User> findByRolNombre(@Param("rolNombre") String rolNombre);

    @Query("SELECT u FROM User u WHERE u.dependencia = :dependencia AND u.activo = true AND " +
           "EXISTS (SELECT r FROM u.roles r WHERE r.nombre = :rolNombre)")
    List<User> findByDependenciaAndRolNombre(@Param("dependencia") String dependencia, 
                                           @Param("rolNombre") String rolNombre);

    @Query("SELECT COUNT(u) FROM User u WHERE u.dependencia = :dependencia AND u.activo = true")
    Long countByDependenciaAndActivoTrue(@Param("dependencia") String dependencia);

    @Query("SELECT COUNT(u) FROM User u WHERE u.statusUsuario = :status AND u.activo = true")
    Long countByStatusUsuarioAndActivoTrue(@Param("status") UserStatus status);

    @Query("SELECT u FROM User u WHERE u.ultimoAcceso < :fecha AND u.activo = true")
    List<User> findUsuariosSinAccesoReciente(@Param("fecha") LocalDate fecha);

    @Query("SELECT u FROM User u WHERE u.cuentaBloqueada = true AND u.activo = true")
    List<User> findUsuariosBloqueados();

    @Query("SELECT DISTINCT u.dependencia FROM User u WHERE u.activo = true ORDER BY u.dependencia")
    List<String> findAllDependenciasActivas();

    @Query("SELECT u FROM User u WHERE u.fechaIngreso BETWEEN :fechaInicio AND :fechaFin AND u.activo = true")
    List<User> findByFechaIngresoEntre(@Param("fechaInicio") LocalDate fechaInicio, 
                                      @Param("fechaFin") LocalDate fechaFin);

    boolean existsByEmailAndActivoTrue(String email);

    @Query("SELECT u FROM User u WHERE u.jefeInmediatoCurp IS NULL AND u.activo = true")
    List<User> findJefesSuperiores();

    @Query("SELECT u FROM User u WHERE SIZE(u.subordinados) > 0 AND u.activo = true")
    List<User> findJefesConSubordinados();
}