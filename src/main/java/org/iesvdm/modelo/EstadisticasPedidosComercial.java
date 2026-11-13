package org.iesvdm.modelo;

import java.math.BigDecimal;
import java.util.List;

public record EstadisticasPedidosComercial(
		int cantidadPedidos,
		BigDecimal importeTotal,
		BigDecimal importeMedio,
		BigDecimal importeMaximo,
		BigDecimal importeMinimo,
		List<ClienteTotalPedidos> clientesOrdenados) {

	public record ClienteTotalPedidos(int clienteId, String clienteNombre, BigDecimal importeTotal) {}
}