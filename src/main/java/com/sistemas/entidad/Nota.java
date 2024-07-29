package com.sistemas.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@IdClass(NotaID.class)
@Table(name = "notas")
@Data
public class Nota {
	// Se anota con @Id a cada campo de la clave compuesta
	@Id
	// ManyToOne: muchas Notas para un alumno
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			// la columna a crear en la Tabla es idAlumno
			name = "idAlumno",
			// referencia a la propiedad id en la clase alumno
			referencedColumnName = "id",
			// no acepta nulos
			nullable = false,
			// la relación se llama fk_alumnos_nota
			foreignKey = @ForeignKey(name = "fk_alumnos_nota"))
	private Alumno alumno;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "idCurso",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_cursos_notas"),
			nullable = false)
	private Curso curso;

	private Double unidad1;
	private Double unidad2;
	private Double unidad3;
}
