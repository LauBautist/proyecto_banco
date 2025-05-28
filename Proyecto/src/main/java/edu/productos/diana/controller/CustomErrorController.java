package edu.productos.diana.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

	@GetMapping("/error")
	public String handleError() {
		// Puedes personalizar más aquí según el código de estado
		return "login/error"; // Puedes hacer una lógica más detallada con HttpServletRequest si quieres
								// diferenciar errores
	}
}
