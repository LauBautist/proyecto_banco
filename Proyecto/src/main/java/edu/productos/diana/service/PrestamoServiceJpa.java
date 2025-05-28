package edu.productos.diana.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import edu.productos.diana.model.Cliente;
import edu.productos.diana.model.Cuota;
import edu.productos.diana.model.Empleado;
import edu.productos.diana.model.Prestamo;
import edu.productos.diana.repository.CuotaRepository;
import edu.productos.diana.repository.PrestamoRepository;

@Service
@Primary
public class PrestamoServiceJpa implements PrestamoService {

	@Autowired
	private PrestamoRepository prestamoRepository;

	@Autowired
	private CuotaRepository cuotaRepository;

	@Override
	public List<Prestamo> buscarTodos() {
		return prestamoRepository.findAll();
	}

	@Override
	public Prestamo buscarPorId(Integer id) {
		Optional<Prestamo> optional = prestamoRepository.findById(id);
		return optional.orElse(null);
	}

	@Override
	public void guardarPrestamo(Prestamo prestamo) {
		prestamoRepository.save(prestamo);
	}

	@Override
	public void guardarPrestamo(Prestamo prestamo, Empleado aprobadoPor) {
		prestamo.setAprobadoPor(aprobadoPor);
		prestamo.setFechaAprobacion(LocalDate.now());
		prestamoRepository.save(prestamo);

		List<Cuota> cuotas = calcularCuotas(prestamo.getMonto(), prestamo.getInteres(), prestamo.getPlazo(),
				prestamo.getFechaAprobacion());

		for (Cuota cuota : cuotas) {
			cuota.setPrestamo(prestamo);
			cuotaRepository.save(cuota);
		}
	}

	@Override
	public void eliminarPrestamo(Integer id) {
		Prestamo prestamo = prestamoRepository.findById(id).orElse(null);
		if (prestamo != null) {
			cuotaRepository.deleteAll(prestamo.getCuotas()); // eliminar cuotas
			prestamoRepository.delete(prestamo); // luego el préstamo
		}
	}

	@Override
	public List<Prestamo> buscarPorCliente(Cliente cliente) {
		return prestamoRepository.findByCliente(cliente);
	}

	@Override
	public List<Cuota> calcularCuotas(BigDecimal monto, BigDecimal interes, Integer plazo, LocalDate fechaInicio) {
		List<Cuota> cuotas = new ArrayList<>();

		if (monto == null || interes == null || plazo == null || fechaInicio == null || plazo <= 0) {
			return cuotas;
		}

		BigDecimal capital = monto.divide(BigDecimal.valueOf(plazo), 2, RoundingMode.HALF_UP);
		BigDecimal interesMensual = monto.multiply(interes).divide(BigDecimal.valueOf(100 * plazo), 2,
				RoundingMode.HALF_UP);
		BigDecimal total = capital.add(interesMensual);

		for (int i = 1; i <= plazo; i++) {
			LocalDate fechaCuota = fechaInicio.plusMonths(i - 1);
			Cuota cuota = new Cuota();
			cuota.setNumero(i);
			cuota.setCapital(capital);
			cuota.setInteres(interesMensual);
			cuota.setTotal(total);
			cuota.setFechaPago(fechaCuota);
			cuota.setEstado(Cuota.EstadoCuota.PENDIENTE);
			cuota.setFormaPago("MENSUAL");
			cuota.setAlerta("-");
			cuota.setPagada(false);
			cuotas.add(cuota);
		}

		return cuotas;
	}
}
