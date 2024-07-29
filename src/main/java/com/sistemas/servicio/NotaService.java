package com.sistemas.servicio;

import java.util.List;

import com.sistemas.entidad.Nota;
import com.sistemas.entidad.NotaID;

public interface NotaService extends iGenericoService<Nota, NotaID> {

	public List<Nota> listarPorAlumno(Long idAlumno);
	
	public List<Nota> listarTodasConAlumnosYCursos();
	
    public void actualizarNotas(Long idAlumno, Long idCurso, Double unidad1, Double unidad2, Double unidad3);
    
    public List<Object[]> findAverageGradesPerCourse();
    
    public List<Object[]> calculateFinalAverages();
}
