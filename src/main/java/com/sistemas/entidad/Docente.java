package com.sistemas.entidad;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "docentes")
@Data
public class Docente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 35, nullable = false)
	@NotBlank(message = "El apellido no puede estar vacio")
	@Size(min = 2, max = 35, message = "El apellido debe tener entre 2 y 35 letras")
	private String apellidos;

	@Column(length = 35, nullable = false)
	@NotBlank(message = "El nombre no puede estar vacio")
	@Size(min = 2, max = 35, message = "El nombre debe tener entre 2 y 35 letras")
	private String nombres;

	@Column(length = 8, nullable = false, unique = true)
	@NotBlank(message = "El DNI no puede estar en blanco")
	@Size(min = 8, max = 8, message = "El DNI debe tener 8 caracteres")
	private String dni;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	// Establece el formato de la fecha con el cual comunicarse
	@Past(message = "El nacimiento debe ser anterior a la fecha actual")
	// Verifica que el valor ingresado sea anterior a la fecha actual
	private Date fechaNacimiento;

	@Column(length = 9, unique = true)
	@NotBlank(message = "El celular no puede estar en blanco")
	@Size(min = 9, max = 9, message = "El celular debe tener 9 caracteres")
	private String celular;

	@Column(length = 80)
	@NotBlank(message = "El eMail no puede estar vacio")
	@Email(message = "Debe ingresar un correo vélido")
	private String eMail;

	@Column(length = 80)
	private String direccion;

	@DateTimeFormat(iso = ISO.DATE)
	@Past(message = "El ingreso debe ser anterior a la fecha actual")
	private Date fechaIngreso;

	@Column(length = 50)
	private String especialidad;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modificationTime;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "docente", cascade = CascadeType.ALL)
	private List<Curso> curso;
}
