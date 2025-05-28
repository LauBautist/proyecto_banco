package edu.productos.diana.service;

import java.util.List;
import edu.productos.diana.model.Perfil;

public interface PerfilService {

	List<Perfil> buscarTodos(); // Obtener todos los perfiles

	Perfil buscarPorId(Integer idPerfil); // Buscar perfil por ID

	void guardarPerfil(Perfil perfil); // Guardar o actualizar un perfil

	void eliminarPerfil(Integer idPerfil); // Eliminar perfil por ID
}