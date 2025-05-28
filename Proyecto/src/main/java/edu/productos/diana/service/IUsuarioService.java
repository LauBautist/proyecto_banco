package edu.productos.diana.service;

import java.util.List;

import edu.productos.diana.model.Usuario;

public interface IUsuarioService {

	void guardarU(Usuario usuario); // Método para guardar usuario

	List<Usuario> buscarTodos();

	void eliminarU(Integer idUsuario); // Método para eliminar usuario

	Usuario buscarPorUsername(String username);
	
	Usuario buscarPorId(Integer id);


}
