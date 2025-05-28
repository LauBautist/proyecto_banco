package edu.productos.diana.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.productos.diana.model.Cuota;
import edu.productos.diana.model.Empleado;
import edu.productos.diana.model.Prestamo;
import edu.productos.diana.model.Usuario;
import edu.productos.diana.repository.CuotaRepository;
import edu.productos.diana.service.ClienteService;
import edu.productos.diana.service.EmpleadoService;
import edu.productos.diana.service.IUsuarioService;
import edu.productos.diana.service.PrestamoService;

@Controller
@RequestMapping("/prestamo")
public class PrestamoController {

	@Autowired
	private PrestamoService prestamoService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private CuotaRepository cuotaRepository;

	@GetMapping("/crear")
	public String mostrarFormulario(Model model) {
		model.addAttribute("prestamo", new Prestamo());
		model.addAttribute("clientes", clienteService.buscarTodos());
		model.addAttribute("empleados", empleadoService.buscarTodos());
		model.addAttribute("prestamos", prestamoService.buscarTodos());
		return "prestamo/formPrestamo";
	}

	@PostMapping("/guardar")
	public String guardarPrestamo(@ModelAttribute("prestamo") Prestamo prestamo, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {

		if (prestamo.getMonto() == null || prestamo.getMonto().doubleValue() <= 0) {
			result.rejectValue("monto", "error.monto", "El monto debe ser mayor a cero.");
		}

		if (prestamo.getInteres() == null || prestamo.getInteres().doubleValue() < 0) {
			result.rejectValue("interes", "error.interes", "El interés no puede ser negativo.");
		}

		if (prestamo.getPlazo() == null || prestamo.getPlazo() <= 0) {
			result.rejectValue("plazo", "error.plazo", "El plazo debe ser mayor a cero.");
		}

		if (result.hasErrors()) {
			model.addAttribute("clientes", clienteService.buscarTodos());
			model.addAttribute("prestamos", prestamoService.buscarTodos());
			return "prestamo/formPrestamo";
		}

		if (prestamo.getFechaAprobacion() == null) {
			prestamo.setFechaAprobacion(LocalDate.now());
		}

		prestamo.setEstado(Prestamo.EstadoPrestamo.Activo);

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioService.buscarPorUsername(username);
		prestamo.setUsuario(usuario);

		Empleado empleado = empleadoService.buscarPorUsuarioUsername(username);
		if (empleado != null) {
			prestamo.setAprobadoPor(empleado);
		}

		// 1️⃣ Guardar préstamo
		prestamoService.guardarPrestamo(prestamo);

		// 2️⃣ Generar cuotas
		BigDecimal monto = prestamo.getMonto();
		BigDecimal interes = prestamo.getInteres();
		int plazo = prestamo.getPlazo();

		BigDecimal capitalMensual = monto.divide(BigDecimal.valueOf(plazo), 2, RoundingMode.HALF_UP);
		BigDecimal interesMensual = interes.divide(BigDecimal.valueOf(plazo), 2, RoundingMode.HALF_UP);
		BigDecimal totalMensual = capitalMensual.add(interesMensual);

		List<Cuota> cuotas = new ArrayList<>();

		for (int i = 0; i < plazo; i++) {
			Cuota cuota = new Cuota();
			cuota.setNumero(i + 1);
			cuota.setCapital(capitalMensual);
			cuota.setInteres(interesMensual);
			cuota.setTotal(totalMensual);
			cuota.setFechaPago(prestamo.getFechaAprobacion().plusMonths(i + 1));
			cuota.setEstado(Cuota.EstadoCuota.PENDIENTE);
			cuota.setFormaPago("MENSUAL");
			cuota.setPagada(false);
			cuota.setAlerta("-");
			cuota.setMontoPagado(BigDecimal.ZERO);
			cuota.setPrestamo(prestamo);
			cuotas.add(cuota);
		}

		// 3️⃣ Guardar cuotas
		cuotaRepository.saveAll(cuotas);

		redirectAttributes.addFlashAttribute("mensajeExito", "Préstamo y cuotas registrados correctamente.");
		return "redirect:/prestamo/crear";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarPrestamo(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			prestamoService.eliminarPrestamo(id);
			redirectAttributes.addFlashAttribute("mensajeExito", "Préstamo eliminado correctamente.");
		} catch (DataIntegrityViolationException e) {
			redirectAttributes.addFlashAttribute("errorEliminar",
					"No se puede eliminar el préstamo porque tiene pagos registrados.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorEliminar", "Error inesperado al eliminar el préstamo.");
		}
		return "redirect:/prestamo/crear";
	}

	// Nuevo método para mostrar cuotas en una vista separada
	@GetMapping("/cuotas/{id}")
	public String verCuotas(@PathVariable("id") Integer id, Model model) {
		Prestamo prestamo = prestamoService.buscarPorId(id);
		if (prestamo == null) {
			return "redirect:/prestamo/crear";
		}

		List<Cuota> cuotas = prestamoService.calcularCuotas(prestamo.getMonto(), prestamo.getInteres(),
				prestamo.getPlazo(), prestamo.getFechaAprobacion() // asegúrate que este getter exista
		);

		model.addAttribute("prestamo", prestamo);
		model.addAttribute("cuotas", cuotas);
		return "prestamo/cuotasPrestamo";
	}
}
