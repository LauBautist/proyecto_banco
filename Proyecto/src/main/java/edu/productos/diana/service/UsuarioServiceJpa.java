package edu.productos.diana.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.productos.diana.model.Usuario;
import edu.productos.diana.repository.UsuarioRepository;

@Service
public class UsuarioServiceJpa implements IUsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public void guardarU(Usuario usuario) {
		usuarioRepo.save(usuario);
	}

	@Override
	public List<Usuario> buscarTodos() {
		return usuarioRepo.findAll();
	}

	// Método para eliminar usuario
	@Override
	public void eliminarU(Integer idUsuario) {
		Usuario usuario = usuarioRepo.findById(idUsuario)
				.orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + idUsuario));

		if (usuario.getPerfiles() != null && !usuario.getPerfiles().isEmpty()) {
			throw new IllegalStateException("No se puede eliminar el usuario porque tiene perfiles asignados.");
		}

		usuarioRepo.deleteById(idUsuario);
	}

	@Override
	public Usuario buscarPorUsername(String username) {
		return usuarioRepo.findByUsername(username);
	}

	@Override
	public Usuario buscarPorId(Integer id) {
		return usuarioRepository.findById(id).orElse(null);
	}
}