package com.sistemas.servicio;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemas.entidad.Nota;
import com.sistemas.entidad.NotaID;
import com.sistemas.repositorio.NotaRepository;

@Service
public class NotaServiceImpl implements NotaService {

	@Autowired
	private NotaRepository notaRepository;

	@Override
	public Nota agregar(Nota entidad) {
		return notaRepository.save(entidad);
	}

	@Override
	public List<Nota> listarTodos() {
		return notaRepository.findAll();
	}

	@Override
	public Nota buscar(NotaID id) {
		return notaRepository.findById(id).orElse(null);
	}

	@Override
	public Nota actualizar(Nota entidad) {
		return notaRepository.save(entidad);
	}

	@Override
	public void eliminar(NotaID id) {
		notaRepository.deleteById(id);
	}

	@Override
	public List<Nota> listarPorAlumno(Long idAlumno) {
		return notaRepository.findByAlumnoId(idAlumno);
	}
	
	@Override
    public List<Nota> listarTodasConAlumnosYCursos() {
        return notaRepository.findAllWithAlumnosAndCursos();
    }
	
	@Override
    public void actualizarNotas(Long idAlumno, Long idCurso, Double unidad1, Double unidad2, Double unidad3) {
        notaRepository.actualizarNotas(idAlumno, idCurso, unidad1, unidad2, unidad3);
    }
	
	@Override
    public List<Object[]> findAverageGradesPerCourse() {
        return notaRepository.findAverageGradesPerCourse();
    }

    @Override
    public List<Object[]> calculateFinalAverages() {
        List<Object[]> averagesPerCourse = findAverageGradesPerCourse();

        return averagesPerCourse.stream()
            .collect(Collectors.groupingBy(row -> (Long) row[0]))
            .entrySet().stream()
            .map(entry -> {
                Long alumnoId = entry.getKey();
                List<Object[]> courses = entry.getValue();
                double finalAverage = courses.stream()
                    .mapToDouble(row -> (Double) row[1])
                    .average().orElse(0.0);
                return new Object[]{alumnoId, finalAverage};
            })
            .sorted((a, b) -> Double.compare((Double) b[1], (Double) a[1]))
            .collect(Collectors.toList());
    }
}
