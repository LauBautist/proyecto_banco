package edu.productos.diana.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import edu.productos.diana.model.Perfil;
import edu.productos.diana.model.Usuario;
import edu.productos.diana.repository.PerfilRepository;
import edu.productos.diana.repository.UsuarioRepository;

@Controller
public class HomeController {

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private PerfilRepository perfilRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private boolean yaEjecutado = false;

	@GetMapping("/")
	public String mostrarPublicidad() {
		if (!yaEjecutado) {
			insertarUsuariosIniciales();
			yaEjecutado = true;
		}
		return "home";
	}

	private void insertarUsuariosIniciales() {
		crearUsuarioSiNoExiste("Diana Laura Salvador", "dls@tecnm.mx", "Diana", "diana123", "ADMINISTRADOR");
		crearUsuarioSiNoExiste("Cristian Jair Ramirez", "cjrc@tecnm.mx", "Cristian", "cristian123", "EJECUTIVO");
		crearUsuarioSiNoExiste("Daniela Muñiz Gomez", "dmg@tecnm.mx", "Daniela", "daniela123", "CAJERO");
		crearUsuarioSiNoExiste("Hugo García Saguilan", "hgs@tecnm.mx", "Hugo", "hugo123", "SUPERVISOR");
	}

	private void crearUsuarioSiNoExiste(String nombre, String email, String username, String password,
			String nombrePerfil) {
		if (usuarioRepo.findByUsername(username) != null)
			return;

		Perfil perfil = perfilRepo.findByPerfil(nombrePerfil);
		if (perfil == null) {
			perfil = new Perfil();
			perfil.setPerfil(nombrePerfil);
			perfilRepo.save(perfil);
		}

		Usuario nuevo = new Usuario();
		nuevo.setNombre(nombre);
		nuevo.setEmail(email);
		nuevo.setUsername(username);
		nuevo.setPassword(passwordEncoder.encode(password)); // 🔐 Encriptado
		nuevo.setEstatus(1);
		nuevo.setFechaRegistro(LocalDate.now()); // ✅ CORRECTO
		nuevo.setPerfiles(List.of(perfil));
		usuarioRepo.save(nuevo);

		System.out.println("✅ Usuario " + username + " insertado con perfil " + nombrePerfil);
	}
}
