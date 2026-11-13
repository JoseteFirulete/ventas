package org.iesvdm.dao;

import java.time.LocalDate;
import java.util.List;

import org.iesvdm.modelo.ClienteComercialDetalle;

public interface ClienteComercialDAO {
	List<ClienteComercialDetalle> findByClienteId(int clienteId);
	boolean exists(int clienteId, int comercialId);
	void create(int clienteId, int comercialId, LocalDate fecha, int prioridad);
	void delete(int clienteId, int comercialId);
}