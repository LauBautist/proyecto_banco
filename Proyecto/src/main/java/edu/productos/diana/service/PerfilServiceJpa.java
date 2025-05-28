package edu.productos.diana.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import edu.productos.diana.model.Perfil;
import edu.productos.diana.repository.PerfilRepository;

@Service
@Primary
public class PerfilServiceJpa implements PerfilService {

	@Autowired
	private PerfilRepository perfilRepo;

	@Override
	public Perfil buscarPorId(Integer idPerfil) {
		Optional<Perfil> optional = perfilRepo.findById(idPerfil);
		return optional.orElse(null);
	}

	@Override
	public void guardarPerfil(Perfil perfil) {
		perfilRepo.save(perfil);
	}

	@Override
	public List<Perfil> buscarTodos() {
		return perfilRepo.findAll();
	}

	@Override
	public void eliminarPerfil(Integer idPerfil) {
		perfilRepo.deleteById(idPerfil);
	}
}