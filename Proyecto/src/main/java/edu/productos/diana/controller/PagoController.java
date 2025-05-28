package edu.productos.diana.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import edu.productos.diana.model.Cuota;
import edu.productos.diana.model.Empleado;
import edu.productos.diana.model.Pago;
import edu.productos.diana.model.Prestamo;
import edu.productos.diana.model.Usuario;
import edu.productos.diana.repository.CuotaRepository;
import edu.productos.diana.service.EmpleadoService;
import edu.productos.diana.service.IUsuarioService;
import edu.productos.diana.service.PagoService;
import edu.productos.diana.service.PrestamoService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/pago")
public class PagoController {

	@Autowired
	private PagoService pagoService;

	@Autowired
	private PrestamoService prestamoService;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private CuotaRepository cuotaRepository;

	@Autowired
	private IUsuarioService usuarioService;

	@GetMapping("/lista")
	public String mostrarLista(Model model) {
		List<Pago> pagos = pagoService.buscarTodos();
		model.addAttribute("pagos", pagos);
		return "pago/listaPagos";
	}

	@GetMapping("/crear")
	public String mostrarFormulario(Model model) {
		model.addAttribute("pago", new Pago());
		model.addAttribute("prestamos", prestamoService.buscarTodos());
		model.addAttribute("empleados", empleadoService.buscarTodos());
		return "pago/formPago";
	}

	@GetMapping("/registrar/{idPrestamo}")
	public String registrarPagoPorPrestamo(@PathVariable("idPrestamo") Integer idPrestamo, Model model) {
		Prestamo prestamo = prestamoService.buscarPorId(idPrestamo);
		if (prestamo == null)
			return "redirect:/pago/buscar";

		List<Cuota> cuotas = cuotaRepository.findByPrestamoIdOrderByNumeroAsc(idPrestamo);

		Pago pago = new Pago();
		pago.setPrestamo(prestamo);

		model.addAttribute("prestamo", prestamo);
		model.addAttribute("cuotas", cuotas);
		model.addAttribute("montoRestante", prestamo.getMonto());
		model.addAttribute("pago", pago);
		model.addAttribute("empleados", empleadoService.buscarTodos());

		return "pago/listaPagos";
	}

	@PostMapping("/guardar")
	public String guardarPago(@ModelAttribute("pago") Pago pago, RedirectAttributes redirectAttributes, Model model) {

		if (pago.getFecha() == null) {
			pago.setFecha(LocalDate.now());
		}

		// 🔐 Obtener usuario autenticado
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioService.buscarPorUsername(username);
		pago.setUsuario(usuario);

		// 🔄 Asignar empleado si está vinculado al usuario
		Empleado empleado = empleadoService.buscarPorUsuarioUsername(username);
		pago.setRegistradoPor(empleado);

		// ✅ Guardar el nuevo pago
		pagoService.guardar(pago);

		Prestamo prestamo = pago.getPrestamo();
		LocalDate fechaPago = pago.getFecha();

		// Buscar la cuota correspondiente al mes y año del pago
		List<Cuota> cuotas = cuotaRepository.findByPrestamoIdOrderByNumeroAsc(prestamo.getId());
		Cuota cuotaObjetivo = null;

		for (Cuota cuota : cuotas) {
			if (cuota.getFechaPago().getMonthValue() == fechaPago.getMonthValue()
					&& cuota.getFechaPago().getYear() == fechaPago.getYear()) {
				cuotaObjetivo = cuota;
				break;
			}
		}

		if (cuotaObjetivo == null) {
			redirectAttributes.addFlashAttribute("error", "No se encontró una cuota con esa fecha.");
			return "redirect:/pago/registrar/" + prestamo.getId();
		}

		// Calcular interés por mora si hay retraso
		if (fechaPago.isAfter(cuotaObjetivo.getFechaPago())) {
			long diasRetraso = ChronoUnit.DAYS.between(cuotaObjetivo.getFechaPago(), fechaPago);
			BigDecimal tasaDiaria = new BigDecimal("0.005"); // 0.5% por día
			BigDecimal interesMora = cuotaObjetivo.getTotal().multiply(tasaDiaria)
					.multiply(BigDecimal.valueOf(diasRetraso)).setScale(2, RoundingMode.HALF_UP);

			cuotaObjetivo.setInteresMora(interesMora);
			cuotaObjetivo.setAlerta("Pago con mora de $" + interesMora + " por " + diasRetraso + " días de retraso");
		} else {
			cuotaObjetivo.setInteresMora(BigDecimal.ZERO);
		}

		// Sumar todos los pagos registrados para la misma fecha y préstamo
		BigDecimal sumaPagos = pagoService.buscarPorPrestamoYFecha(prestamo.getId(), fechaPago).stream()
				.map(Pago::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);

		cuotaObjetivo.setMontoPagado(sumaPagos);

		// Total a cubrir considerando la mora
		BigDecimal totalConMora = cuotaObjetivo.getTotal().add(cuotaObjetivo.getInteresMora());

		// Determinar el tipo de alerta
		if (sumaPagos.compareTo(BigDecimal.ZERO) == 0) {
			cuotaObjetivo.setAlerta("-");
		} else if (sumaPagos.compareTo(totalConMora) < 0) {
			cuotaObjetivo.setAlerta("Pago incompleto");
		} else if (sumaPagos.compareTo(totalConMora) > 0) {
			cuotaObjetivo.setAlerta("Pago excedente");
		} else {
			cuotaObjetivo.setAlerta("Pago completo");
		}

		// Marcar como pagada si se cubre todo con mora incluida
		if (sumaPagos.compareTo(totalConMora) >= 0) {
			cuotaObjetivo.setPagada(true);
			cuotaObjetivo.setEstado(Cuota.EstadoCuota.PAGADO);
		}

		cuotaRepository.save(cuotaObjetivo);

		redirectAttributes.addFlashAttribute("mensajeExito",
				cuotaObjetivo.isPagada() ? "Pago registrado y cuota marcada como pagada."
						: "Pago registrado. Cuota aún no cubierta completamente.");

		return "redirect:/pago/registrar/" + prestamo.getId();
	}

	@GetMapping("/buscar")
	public String buscarPrestamos(Model model) {
		List<Prestamo> prestamos = prestamoService.buscarTodos();

		model.addAttribute("prestamos", prestamos);
		return "pago/formPago";
	}

	@GetMapping("/pdfPagados")
	public void generarPDFPagados(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=cuotas_pagadas.pdf");

		List<Cuota> cuotasPagadas = cuotaRepository.findByPagadaTrue();

		Document document = new Document();
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();

		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

		document.add(new Paragraph("REPORTE DE CUOTAS PAGADAS", titleFont));
		document.add(new Paragraph(" "));

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 1.2f, 2.2f, 2.2f, 2.2f, 2.5f });

		table.addCell(new PdfPCell(new Phrase("No.", headerFont)));
		table.addCell(new PdfPCell(new Phrase("Fecha de Pago", headerFont)));
		table.addCell(new PdfPCell(new Phrase("Monto Total", headerFont)));
		table.addCell(new PdfPCell(new Phrase("Monto Pagado", headerFont)));
		table.addCell(new PdfPCell(new Phrase("Forma de Pago", headerFont)));

		for (Cuota cuota : cuotasPagadas) {
			table.addCell(new PdfPCell(new Phrase(String.valueOf(cuota.getNumero()), cellFont)));
			table.addCell(new PdfPCell(new Phrase(String.valueOf(cuota.getFechaPago()), cellFont)));
			table.addCell(new PdfPCell(new Phrase("$" + cuota.getTotal(), cellFont)));
			table.addCell(new PdfPCell(new Phrase("$" + cuota.getMontoPagado(), cellFont)));
			table.addCell(new PdfPCell(new Phrase(cuota.getFormaPago(), cellFont)));
		}

		document.add(table);
		document.close();
	}

}
