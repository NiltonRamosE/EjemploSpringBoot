package com.sistemas.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistemas.entidad.Nota;
import com.sistemas.entidad.NotaID;

import jakarta.transaction.Transactional;

public interface NotaRepository extends JpaRepository<Nota, NotaID> {

	public List<Nota> findByAlumnoId(Long idAlumno);
	
	@Query("SELECT n FROM Nota n JOIN FETCH n.alumno JOIN FETCH n.curso")
    List<Nota> findAllWithAlumnosAndCursos();
	
	@Transactional
    @Modifying
    @Query("UPDATE Nota n SET n.unidad1 = :unidad1, n.unidad2 = :unidad2, n.unidad3 = :unidad3 WHERE n.alumno.id = :idAlumno AND n.curso.id = :idCurso")
    void actualizarNotas(
        @Param("idAlumno") Long idAlumno,
        @Param("idCurso") Long idCurso,
        @Param("unidad1") Double unidad1,
        @Param("unidad2") Double unidad2,
        @Param("unidad3") Double unidad3
    );
}
