package edu.productos.diana.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.productos.diana.model.Cuenta;
import edu.productos.diana.model.CuentaDTO;
import edu.productos.diana.service.ClienteService;
import edu.productos.diana.service.CuentaService;

@Controller
@RequestMapping("/cuenta")
public class CuentaController {

	@Autowired
	private CuentaService cuentaService;

	@Autowired
	private ClienteService clienteService;

	// Mostrar lista de cuentas
	@GetMapping("/lista")
	public String mostrarLista(Model model) {
		List<Cuenta> cuentas = cuentaService.buscarTodas();
		model.addAttribute("cuentas", cuentas);
		return "cuenta/listaCuentas";
	}

	// Mostrar formulario para crear una cuenta
	@GetMapping("/crear")
	public String mostrarFormulario(Model model) {
		model.addAttribute("cuenta", new Cuenta());
		model.addAttribute("clientes", clienteService.buscarTodos());
		return "cuenta/formCuenta";
	}

	// Guardar cuenta
	@PostMapping("/guardar")
	public String guardarCuenta(@ModelAttribute("cuenta") Cuenta cuenta, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {

		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Error: " + error.getDefaultMessage());
			}
			model.addAttribute("clientes", clienteService.buscarTodos());
			return "cuenta/formCuenta";
		}

		// Si no se asignó fecha, usar la actual
		if (cuenta.getFechaApertura() == null) {
			cuenta.setFechaApertura(LocalDate.now());
		}

		// Forzar estado como activa
		cuenta.setEstado(Cuenta.EstadoCuenta.Activa);

		try {
			cuentaService.guardarCuenta(cuenta);
			redirectAttributes.addFlashAttribute("mensajeExito", "Cuenta guardada con éxito");
			return "redirect:/cuenta/lista";

		} catch (DataIntegrityViolationException e) {
			// El número de cuenta ya existe
			System.out.println("Error: Número de cuenta duplicado.");
			result.reject("numeroCuentaDuplicado", "Ya existe una cuenta con ese número.");
			model.addAttribute("clientes", clienteService.buscarTodos());
			return "cuenta/formCuenta";
		}
	}

	// Editar cuenta
	@GetMapping("/editar/{id}")
	public String editarCuenta(@PathVariable("id") Integer idCuenta, Model model) {
		Cuenta cuenta = cuentaService.buscarPorId(idCuenta);

		if (cuenta == null) {
			return "redirect:/cuenta/lista";
		}

		model.addAttribute("cuenta", cuenta);
		model.addAttribute("clientes", clienteService.buscarTodos());
		return "cuenta/formCuenta";
	}

	// Eliminar cuenta
	@GetMapping("/eliminar/{id}")
	public String eliminarCuenta(@PathVariable("id") Integer idCuenta, RedirectAttributes redirectAttributes) {
		try {
			cuentaService.eliminarCuenta(idCuenta);
			redirectAttributes.addFlashAttribute("mensajeExito", "Cuenta eliminada correctamente.");
		} catch (DataIntegrityViolationException e) {
			redirectAttributes.addFlashAttribute("mensajeError",
					"No se puede eliminar la cuenta porque tiene movimientos asociados.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("mensajeError", "Error inesperado al eliminar la cuenta.");
		}
		return "redirect:/cuenta/lista";
	}

	@GetMapping("/cuentasPorCliente/{id}")
	@ResponseBody
	public List<CuentaDTO> obtenerCuentasPorCliente(@PathVariable("id") Integer idCliente) {
		return cuentaService.buscarPorIdCliente(idCliente).stream().map(CuentaDTO::new).collect(Collectors.toList());
	}

}
