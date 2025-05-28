package edu.productos.diana.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.productos.diana.model.Perfil;
import edu.productos.diana.model.Usuario;
import edu.productos.diana.service.IUsuarioService;

@Controller
public class UsuarioController {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/usuario/lista")
	public String mostrarListaUsuarios(Model model) {
		List<Usuario> usuarios = usuarioService.buscarTodos();
		model.addAttribute("usuarios", usuarios);
		return "usuario/listaUsuarios"; // Asegúrate de tener esta vista en templates/usuario
	}

	@GetMapping("/usuario/eliminar/{id}")
	public String eliminarUsuario(@PathVariable("id") int idUsuario, RedirectAttributes redirectAttributes) {
		try {
			usuarioService.eliminarU(idUsuario);
			redirectAttributes.addFlashAttribute("mensajeExito", "Usuario eliminado correctamente.");
		} catch (IllegalStateException e) {
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("mensajeError", "Error inesperado al eliminar el usuario.");
		}
		return "redirect:/usuario/lista";
	}

	// ======================== USUARIOS ============================

	@GetMapping("/usuario/agregar")
	public String mostrarAgregarUsuario(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "usuario/formUsuario"; // Asegúrate que sea la ruta correcta al HTML
	}

	@PostMapping("/usuario/guardar")
	public String guardarU(@ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrió un error: " + error.getDefaultMessage());
			}
			return "usuario/formUsuario";
		}

		boolean esNuevo = (usuario.getId() == null);

		if (esNuevo) {
			// NUEVO USUARIO
			usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
			usuario.setFechaRegistro(LocalDate.now());
			usuario.setEstatus(1);

			Perfil perfilConsulta = new Perfil();
			perfilConsulta.setId(5); // cambia si tu rol por defecto es otro
			usuario.getPerfiles().add(perfilConsulta);

		} else {
			// EDICIÓN DE USUARIO
			Usuario original = usuarioService.buscarPorId(usuario.getId());

			if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
				// No se cambió contraseña → conservar
				usuario.setPassword(original.getPassword());
			} else {
				// Se cambió → encriptar nueva
				usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
			}

			// Conservar fecha y estatus
			usuario.setFechaRegistro(original.getFechaRegistro());
			usuario.setEstatus(original.getEstatus());

			// Conservar perfiles si no fueron modificados desde el formulario
			if (usuario.getPerfiles() == null || usuario.getPerfiles().isEmpty()) {
				usuario.setPerfiles(original.getPerfiles());
			}
		}

		usuarioService.guardarU(usuario);

		redirectAttributes.addFlashAttribute("mensajeExito",
				esNuevo ? "Usuario registrado correctamente." : "Usuario actualizado correctamente.");
		return "redirect:/usuario/lista";
	}

	@GetMapping("/usuario/editar/{id}")
	public String editarUsuario(@PathVariable("id") int idUsuario, Model model) {
		Usuario usuario = usuarioService.buscarPorId(idUsuario);

		if (usuario == null) {
			return "redirect:/usuario/lista";
		}

		model.addAttribute("usuario", usuario);
		return "usuario/formUsuario"; // El mismo formulario que usas para agregar
	}
}
