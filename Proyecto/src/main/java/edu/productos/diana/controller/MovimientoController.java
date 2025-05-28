package edu.productos.diana.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.productos.diana.model.Cuenta;
import edu.productos.diana.model.Empleado;
import edu.productos.diana.model.Movimiento;
import edu.productos.diana.model.Usuario;
import edu.productos.diana.service.ClienteService;
import edu.productos.diana.service.CuentaService;
import edu.productos.diana.service.EmpleadoService;
import edu.productos.diana.service.IUsuarioService;
import edu.productos.diana.service.MovimientoService;

@Controller
@RequestMapping("/movimiento")
public class MovimientoController {

	@Autowired
	private MovimientoService movimientoService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private CuentaService cuentaService;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private IUsuarioService usuarioService;

	// Mostrar formulario con resumen y tabla
	@GetMapping("/crear")
	public String mostrarFormulario(@RequestParam(name = "cuentaId", required = false) Integer cuentaId, Model model) {
		model.addAttribute("movimiento", new Movimiento());
		model.addAttribute("clientes", clienteService.buscarTodos());
		model.addAttribute("empleados", empleadoService.buscarTodos());

		if (cuentaId != null) {
			Cuenta cuenta = cuentaService.buscarPorId(cuentaId);
			model.addAttribute("cuentaSeleccionada", cuenta);
			model.addAttribute("movimientosCuenta", movimientoService.buscarPorCuenta(cuenta));
			model.addAttribute("saldoTotal", cuenta.getSaldo());
		} else {
			model.addAttribute("movimientosCuenta", List.of());
			model.addAttribute("saldoTotal", "0.00");
		}

		return "movimiento/formMovimiento";
	}

	@PostMapping("/guardar")
	public String guardarMovimiento(@ModelAttribute("movimiento") Movimiento movimiento, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {

		// Validación de cuenta
		if (movimiento.getCuenta() == null || movimiento.getCuenta().getId() == null) {
			result.rejectValue("cuenta", "cuentaVacia", "Debe seleccionar una cuenta válida.");
		}

		// Mostrar errores
		if (result.hasErrors()) {
			model.addAttribute("clientes", clienteService.buscarTodos());
			model.addAttribute("cuentaSeleccionada", cuentaService.buscarPorId(movimiento.getCuenta().getId()));
			model.addAttribute("movimientosCuenta", movimientoService.buscarPorCuenta(movimiento.getCuenta()));
			model.addAttribute("saldoTotal", cuentaService.buscarPorId(movimiento.getCuenta().getId()).getSaldo());
			return "movimiento/formMovimiento";
		}

		// Fecha por defecto
		if (movimiento.getFecha() == null) {
			movimiento.setFecha(LocalDate.now());
		}

		// Cargar cuenta y modificar saldo
		Cuenta cuenta = cuentaService.buscarPorId(movimiento.getCuenta().getId());

		if (movimiento.getEstado() == Movimiento.EstadoMovimiento.Completado) {
			if (movimiento.getTipo() == Movimiento.TipoMovimiento.Deposito) {
				cuenta.setSaldo(cuenta.getSaldo().add(movimiento.getMonto()));
			} else if (movimiento.getTipo() == Movimiento.TipoMovimiento.Retiro) {
				if (cuenta.getSaldo().compareTo(movimiento.getMonto()) < 0) {
					result.rejectValue("monto", "saldoInsuficiente", "El saldo en la cuenta es insuficiente.");
					model.addAttribute("cuentaSeleccionada", cuenta);
					model.addAttribute("clientes", clienteService.buscarTodos());
					model.addAttribute("movimientosCuenta", movimientoService.buscarPorCuenta(cuenta));
					model.addAttribute("saldoTotal", cuenta.getSaldo());
					return "movimiento/formMovimiento";
				}
				cuenta.setSaldo(cuenta.getSaldo().subtract(movimiento.getMonto()));
			}
		}

		cuentaService.guardarCuenta(cuenta);
		movimiento.setCuenta(cuenta);

		// ✅ Asignar el usuario logueado
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioService.buscarPorUsername(username);
		movimiento.setUsuario(usuario); // siempre lo asignamos

		// 🔄 Opcionalmente asignar empleado si existe
		Empleado empleado = empleadoService.buscarPorUsuarioUsername(username);
		if (empleado != null) {
			movimiento.setRegistradoPor(empleado);
		} else {
			movimiento.setRegistradoPor(null); // No todos los usuarios son empleados
		}

		// Guardar
		movimientoService.guardarMovimiento(movimiento, empleado); // o null si es admin/supervisor

		redirectAttributes.addFlashAttribute("mensajeExito", "Movimiento registrado correctamente.");
		return "redirect:/movimiento/crear?cuentaId=" + cuenta.getId();
	}

	// Eliminar
	@GetMapping("/eliminar/{id}")
	public String eliminarMovimiento(@PathVariable("id") Integer idMovimiento, RedirectAttributes redirectAttributes) {
		movimientoService.eliminarMovimiento(idMovimiento);
		redirectAttributes.addFlashAttribute("mensajeExito", "Movimiento eliminado correctamente");
		return "redirect:/movimiento/crear";
	}
}
