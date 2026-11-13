package org.iesvdm.servicios;

import java.time.LocalDate;
import java.util.List;

import org.iesvdm.dao.ClienteComercialDAO;
import org.iesvdm.modelo.ClienteComercialDetalle;
import org.springframework.stereotype.Service;

@Service
public class ClienteComercialService {
	private final ClienteComercialDAO clienteComercialDAO;

	public ClienteComercialService(ClienteComercialDAO clienteComercialDAO) {
		this.clienteComercialDAO = clienteComercialDAO;
	}

	public List<ClienteComercialDetalle> listByClienteId(int clienteId) {
		return clienteComercialDAO.findByClienteId(clienteId);
	}

	public boolean add(int clienteId, int comercialId, LocalDate fecha, int prioridad) {
		if (clienteComercialDAO.exists(clienteId, comercialId)) return false;
		clienteComercialDAO.create(clienteId, comercialId, fecha, prioridad);
		return true;
	}

	public void delete(int clienteId, int comercialId) {
		clienteComercialDAO.delete(clienteId, comercialId);
	}
}