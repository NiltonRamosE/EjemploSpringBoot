package com.sistemas.controlador;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sistemas.entidad.Alumno;
import com.sistemas.entidad.Curso;
import com.sistemas.entidad.Nota;
import com.sistemas.entidad.NotaID;
import com.sistemas.servicio.AlumnoService;
import com.sistemas.servicio.CursoService;
import com.sistemas.servicio.NotaService;

@Controller
@RequestMapping("/nota")
public class NotaController {

	@Autowired private NotaService notaService;
	@Autowired private AlumnoService alumnoService;
	@Autowired private CursoService cursoService;

	@GetMapping("/index")
    public String getRegistroNotasForm(Model model) {
		
		List<Alumno> alumnos = alumnoService.listarTodos();
        List<Curso> cursos = cursoService.listarTodos();
        Nota nota = new Nota();
        model.addAttribute("nota", nota);
        model.addAttribute("alumnos", alumnos);
        model.addAttribute("cursos", cursos);
        return "nota/registroNotas";

    }
	
	@PostMapping("/registrar")
    public String registrarNota(@ModelAttribute("nota") Nota nota) {
		notaService.actualizarNotas(nota.getAlumno().getId(), nota.getCurso().getId(), nota.getUnidad1(), nota.getUnidad2(), nota.getUnidad3());
		return "redirect:/alumno/index";
    }
	
	@GetMapping("/matricular/{id}")
	public String getMatriculaForm(Model model, @PathVariable("id") Long idAlumno) {

		Alumno alumno = alumnoService.buscar(idAlumno);
		List<Long> idCursosActuales = notaService.listarPorAlumno(idAlumno).stream()
				.map(nota -> nota.getCurso().getId())
				.collect(Collectors.toList());

		model.addAttribute("alumno", alumno == null ? new Alumno() : alumno);
		model.addAttribute("listaCursos", cursoService.listarTodos());
		model.addAttribute("idCursosActuales", idCursosActuales);

		return "nota/notaForm";
	}

	@PostMapping("/matricula/guardar")
	public String postMatriculaForm(@RequestParam("id") Long id,
            @RequestParam(name = "idsSeleccionados", required = false) List<Long> idsSeleccionados) {

		Stream<Nota> notasActuales1 = notaService.listarPorAlumno(id).stream();
		Stream<Nota> notasActuales2 = notaService.listarPorAlumno(id).stream();

		// Eliminar cursos desmarcados
		notasActuales1
				.filter(nota -> idsSeleccionados == null
						? true : !idsSeleccionados.contains(nota.getCurso().getId()))
				.forEach(nota -> notaService.eliminar(new NotaID(id, nota.getCurso().getId())));

		// lista de id de cursos actualmente matriculados
		List<Long> idsCursosActuales = notasActuales2
				.map(nota -> nota.getCurso().getId())
				.collect(Collectors.toList());

		// Añadir solo los nuevos seleccionados
		if (idsSeleccionados != null)
			idsSeleccionados.stream()
			.filter(idCurso->!idsCursosActuales.contains(idCurso))
			.forEach(idCurso->{
				Nota notaAgregar = new Nota();
				Alumno alumno = new Alumno();
				Curso curso = new Curso();

				alumno.setId(id);
				curso.setId(idCurso);
				notaAgregar.setAlumno(alumno);
				notaAgregar.setCurso(curso);
				notaService.agregar(notaAgregar);
			});
		return "redirect:/alumno/index";
	}
	
	@GetMapping("/matriculados")
    public String getReporteMatriculados(Model model) {
		
		List<Nota> todasLasNotas = notaService.listarTodasConAlumnosYCursos();
        model.addAttribute("notas", todasLasNotas);

        return "nota/reporteMatriculados";

    }
	
	@GetMapping("/cuadromeritos")
	public String getCuadroMeritosView(Model model) {
		
		List<Object[]> ranking = notaService.calculateFinalAverages();
        List<Object[]> rankingConNombre = ranking.stream()
            .map(row -> {
                Long alumnoId = (Long) row[0];
                String nombreCompleto = alumnoService.buscar(alumnoId).getNombres() + " " + alumnoService.buscar(alumnoId).getApellidos();
                Double promedioFinal = (Double) row[1];
                return new Object[]{nombreCompleto, promedioFinal};
            })
            .collect(Collectors.toList());
        
        model.addAttribute("ranking", rankingConNombre);
		
		return "nota/cuadroMerito";
	}
	
	@GetMapping("/boleta")
    public String mostrarFormularioSeleccion(Model model) {
        List<Alumno> alumnos = alumnoService.listarTodos();
        model.addAttribute("alumnos", alumnos);
        return "nota/boletaForm";
    }
	
	@PostMapping("/verNotas")
    public String mostrarBoletaNotas(@RequestParam("alumnoId") Long alumnoId, Model model) {
        Alumno alumno = alumnoService.buscar(alumnoId);
        List<Nota> notas = notaService.listarPorAlumno(alumnoId);
        model.addAttribute("alumno", alumno);
        model.addAttribute("notas", notas);
        return "nota/verNotas";
    }
}
