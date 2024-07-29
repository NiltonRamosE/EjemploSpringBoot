package com.sistemas.entidad;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "cursos")
@Data
public class Curso {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50)
	@NotBlank(message = "El nombre no puede estar vacio")
	private String nombre;

	@Min(value = 1, message = "El curso debe tener al menos un credito")
	@NotNull(message = "Los creditos deben tener un valor")
	private Integer creditos;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			// la columna a crear en la Tabla Curso es idDocente
			name = "idDocente",
			// referencia a la propiedad id de la clase Docente
			referencedColumnName = "id",
			// la relacion se llama fk_docente_cursos
			foreignKey = @ForeignKey(name = "fk_docente_cursos"),
			// no acepta nulos
			nullable = false)
	@NotNull(message = "Debe seleccionar un docente")
	private Docente docente;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "curso",
			cascade = CascadeType.ALL)
	private List<Nota> notas;

	public Curso() {
		this.docente = new Docente();
	}
}
