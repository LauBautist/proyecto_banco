package edu.productos.diana.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.productos.diana.model.Empleado;
import edu.productos.diana.model.Perfil;
import edu.productos.diana.model.Usuario;
import edu.productos.diana.service.EmpleadoService;
import edu.productos.diana.service.IUsuarioService;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// Mostrar lista
	@GetMapping("/lista")
	public String mostrarLista(Model model) {
		List<Empleado> empleados = empleadoService.buscarTodos();
		model.addAttribute("empleados", empleados);
		return "empleado/listaEmpleados";
	}

	// Mostrar formulario de creación
	@GetMapping("/crear")
	public String crear(Model model) {
		model.addAttribute("empleado", new Empleado());
		return "empleado/formEmpleado";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute("empleado") Empleado empleado, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Error: " + error.getDefaultMessage());
			}
			return "empleado/formEmpleado";
		}

		try {
			Usuario usuarioExistente = usuarioService.buscarPorUsername(empleado.getNombre().toLowerCase());

			if (usuarioExistente == null) {
				Usuario nuevoUsuario = new Usuario();
				nuevoUsuario.setNombre(empleado.getNombre() + " " + empleado.getApellido());
				nuevoUsuario.setEmail(empleado.getCorreo());

				String usernameGenerado = (empleado.getNombre() + empleado.getApellido()).toLowerCase()
						.replaceAll("\\s+", "");
				nuevoUsuario.setUsername(usernameGenerado);
				nuevoUsuario.setPassword(passwordEncoder.encode("12345"));
				nuevoUsuario.setEstatus(1);
				nuevoUsuario.setFechaRegistro(LocalDate.now());

				Perfil perfilCajero = new Perfil();
				perfilCajero.setId(3); // ID del perfil CAJERO
				nuevoUsuario.getPerfiles().add(perfilCajero);

				usuarioService.guardarU(nuevoUsuario);
				empleado.setUsuario(nuevoUsuario);
			} else {
				empleado.setUsuario(usuarioExistente);
			}

			empleadoService.guardar(empleado);
			redirectAttributes.addFlashAttribute("mensajeExito", "Empleado guardado con éxito");
			return "redirect:/empleado/lista";

		} catch (DataIntegrityViolationException e) {
			System.out.println("Error al guardar el empleado: " + e.getMessage());
			result.reject("errorGeneral", "No se pudo guardar el empleado.");
			model.addAttribute("empleado", empleado);
			return "empleado/formEmpleado";
		}
	}

	// Eliminar empleado
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			empleadoService.eliminar(id);
			redirectAttributes.addFlashAttribute("mensajeExito", "Empleado eliminado correctamente.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("mensajeError", "No se pudo eliminar el empleado.");
		}
		return "redirect:/empleado/lista";
	}

	// Editar empleado
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") Integer id, Model model) {
		Empleado empleado = empleadoService.buscarPorId(id);
		if (empleado == null) {
			return "redirect:/empleado/lista";
		}
		model.addAttribute("empleado", empleado);
		return "empleado/formEmpleado";
	}
}
