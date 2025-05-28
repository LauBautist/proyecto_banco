package edu.productos.diana.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import edu.productos.diana.model.Pago;
import edu.productos.diana.repository.PagoRepository;

@Service
@Primary
public class PagoServiceJpa implements PagoService {

	@Autowired
	private PagoRepository pagoRepository;

	@Override
	public List<Pago> buscarTodos() {
		return pagoRepository.findAll();
	}

	@Override
	public Pago buscarPorId(Integer id) {
		Optional<Pago> optional = pagoRepository.findById(id);
		return optional.orElse(null);
	}

	@Override
	public void guardar(Pago pago) {
		pagoRepository.save(pago);
	}

	@Override
	public void eliminar(Integer id) {
		pagoRepository.deleteById(id);
	}

	@Override
	public List<Pago> buscarPorPrestamoYFecha(Integer idPrestamo, LocalDate fecha) {
		return pagoRepository.findByPrestamoIdAndFecha(idPrestamo, fecha);
	}

}
