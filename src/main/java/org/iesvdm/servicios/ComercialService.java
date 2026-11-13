package org.iesvdm.servicios;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.EstadisticasPedidosComercial;
import org.iesvdm.modelo.EstadisticasPedidosComercial.ClienteTotalPedidos;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.stereotype.Service;

@Service
public class ComercialService {

	private final ComercialDAO comercialDAO;
	private final PedidoDAO pedidoDAO;

	public ComercialService(ComercialDAO comercialDAO, PedidoDAO pedidoDAO) {
		this.comercialDAO = comercialDAO;
		this.pedidoDAO = pedidoDAO;
	}

	public void create(Comercial comercial) {
		comercialDAO.create(comercial);
	}

	public List<Comercial> listAll() {
		return comercialDAO.getAll();
	}

	public Optional<Comercial> find(int id) {
		return comercialDAO.find(id);
	}

	public List<PedidoDetalle> listPedidosByComercial(int comercialId) {
		return pedidoDAO.findByComercialId(comercialId);
	}

	public EstadisticasPedidosComercial estadisticasPedidos(List<PedidoDetalle> pedidosComercial) {
		BigDecimal importeTotal = BigDecimal.ZERO;
		BigDecimal importeMaximo = null;
		BigDecimal importeMinimo = null;
		for (PedidoDetalle detalle : pedidosComercial) {
			BigDecimal importe = importePedido(detalle);
			importeTotal = importeTotal.add(importe);
			if (importeMaximo == null || importe.compareTo(importeMaximo) > 0) importeMaximo = importe;
			if (importeMinimo == null || importe.compareTo(importeMinimo) < 0) importeMinimo = importe;
		}
		int cantidadPedidos = pedidosComercial.size();
		BigDecimal importeMedio = cantidadPedidos == 0 ? BigDecimal.ZERO
				: importeTotal.divide(BigDecimal.valueOf(cantidadPedidos), 2, RoundingMode.HALF_UP);
		if (importeMaximo == null) importeMaximo = BigDecimal.ZERO;
		if (importeMinimo == null) importeMinimo = BigDecimal.ZERO;

		Map<Integer, String> nombresClientes = new HashMap<>();
		pedidosComercial.forEach(detalle -> nombresClientes.putIfAbsent(
				detalle.pedido().getClienteId(), detalle.clienteNombre()));
		Map<Integer, BigDecimal> importesPorCliente = new HashMap<>();
		nombresClientes.keySet().forEach(clienteId -> importesPorCliente.put(clienteId, BigDecimal.ZERO));
		for (PedidoDetalle detalle : pedidoDAO.getAll()) {
			Integer clienteId = detalle.pedido().getClienteId();
			BigDecimal importeAcumulado = importesPorCliente.get(clienteId);
			if (importeAcumulado != null) {
				importesPorCliente.put(clienteId, importeAcumulado.add(importePedido(detalle)));
			}
		}

		List<ClienteTotalPedidos> clientesOrdenados = new ArrayList<>();
		importesPorCliente.forEach((clienteId, importe) -> clientesOrdenados.add(
				new ClienteTotalPedidos(clienteId, nombresClientes.get(clienteId), importe)));
		clientesOrdenados.sort((primero, segundo) -> {
			int comparacionImportes = segundo.importeTotal().compareTo(primero.importeTotal());
			return comparacionImportes != 0 ? comparacionImportes
					: primero.clienteNombre().compareToIgnoreCase(segundo.clienteNombre());
		});

		return new EstadisticasPedidosComercial(cantidadPedidos, importeTotal, importeMedio,
				importeMaximo, importeMinimo, List.copyOf(clientesOrdenados));
	}

	private BigDecimal importePedido(PedidoDetalle detalle) {
		BigDecimal importe = detalle.pedido().getTotal();
		return importe == null ? BigDecimal.ZERO : importe;
	}

	public void update(Comercial comercial) {
		comercialDAO.update(comercial);
	}

	public void delete(int id) {
		comercialDAO.delete(id);
	}
}