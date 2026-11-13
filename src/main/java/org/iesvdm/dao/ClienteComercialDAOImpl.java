package org.iesvdm.dao;

import java.time.LocalDate;
import java.util.List;

import org.iesvdm.modelo.ClienteComercialDetalle;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteComercialDAOImpl implements ClienteComercialDAO {
	private final JdbcTemplate jdbcTemplate;

	public ClienteComercialDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<ClienteComercialDetalle> findByClienteId(int clienteId) {
		return jdbcTemplate.query("""
				SELECT v.id AS comercial_id,
				       CONCAT_WS(' ', v.nombre, v.apellido1, v.apellido2) AS comercial_nombre,
				       chc.fecha_asociacion AS fecha, chc.prioridad
				FROM cliente_has_comercial chc
				JOIN comercial v ON v.id = chc.comercial_id
				WHERE chc.cliente_id = ?
				ORDER BY chc.prioridad, v.apellido1, v.nombre
				""",
				(rs, rowNum) -> new ClienteComercialDetalle(
						rs.getInt("comercial_id"),
						rs.getString("comercial_nombre"),
						rs.getObject("fecha", LocalDate.class),
						rs.getInt("prioridad")),
				clienteId);
	}

	@Override
	public boolean exists(int clienteId, int comercialId) {
		return !jdbcTemplate.queryForList("""
				SELECT 1 FROM cliente_has_comercial
				WHERE cliente_id = ? AND comercial_id = ?
				""", Integer.class, clienteId, comercialId).isEmpty();
	}

	@Override
	public void create(int clienteId, int comercialId, LocalDate fecha, int prioridad) {
		jdbcTemplate.update("""
				INSERT INTO cliente_has_comercial (cliente_id, comercial_id, fecha_asociacion, prioridad)
				VALUES (?, ?, ?, ?)
				""", clienteId, comercialId, fecha, prioridad);
	}

	@Override
	public void delete(int clienteId, int comercialId) {
		jdbcTemplate.update("""
				DELETE FROM cliente_has_comercial
				WHERE cliente_id = ? AND comercial_id = ?
				""", clienteId, comercialId);
	}
}