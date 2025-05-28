package edu.productos.diana.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	// Muestra el formulario de login personalizado
	@GetMapping("/login")
	public String mostrarLogin() {
		return "login/login"; // → templates/login/login.html
	}

	// Muestra la página de confirmación de logout
	@GetMapping("/logout")
	public String mostrarLogout() {
		return "login/logout"; // → templates/login/logout.html
	}
}
