package org.iesvdm.dao;

import java.util.List;
import java.util.Optional;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PedidoDAOImpl implements PedidoDAO {
    private final JdbcTemplate jdbc;
    private static final String SELECT = """
            SELECT p.*, CONCAT_WS(' ', c.nombre, c.apellido1, c.apellido2) AS cliente_nombre,
                   CONCAT_WS(' ', v.nombre, v.apellido1, v.apellido2) AS comercial_nombre
            FROM pedido p
            JOIN cliente c ON c.id = p.id_cliente
            JOIN comercial v ON v.id = p.id_comercial
            """;
    private static final RowMapper<PedidoDetalle> MAPPER = (rs, row) -> {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("id"));
        pedido.setTotal(rs.getBigDecimal("total"));
        pedido.setFecha(rs.getObject("fecha", java.time.LocalDate.class));
        pedido.setClienteId(rs.getInt("id_cliente"));
        pedido.setComercialId(rs.getInt("id_comercial"));
        return new PedidoDetalle(pedido, rs.getString("cliente_nombre"), rs.getString("comercial_nombre"));
    };

    public PedidoDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<PedidoDetalle> getAll() {
        return jdbc.query(SELECT + " ORDER BY p.id", MAPPER);
    }

    @Override
    public Optional<PedidoDetalle> find(int id) {
        return jdbc.query(SELECT + " WHERE p.id = ?", MAPPER, id).stream().findFirst();
    }

    @Override
    public List<PedidoDetalle> findByComercialId(int comercialId) {
        return jdbc.query(SELECT + " WHERE p.id_comercial = ?", MAPPER, comercialId);
    }

    @Override
    public List<PedidoDetalle> findByClienteId(int clienteId) {
        return jdbc.query(SELECT + " WHERE p.id_cliente = ?", MAPPER, clienteId);
    }

    @Override
    public void create(Pedido pedido) {
        jdbc.update("INSERT INTO pedido (total, fecha, id_cliente, id_comercial) VALUES (?, ?, ?, ?)",
                pedido.getTotal(), pedido.getFecha(), pedido.getClienteId(), pedido.getComercialId());
    }

    @Override
    public void update(Pedido pedido) {
        jdbc.update("UPDATE pedido SET total = ?, fecha = ?, id_cliente = ?, id_comercial = ? WHERE id = ?",
                pedido.getTotal(), pedido.getFecha(), pedido.getClienteId(), pedido.getComercialId(), pedido.getId());
    }

    @Override
    public void delete(int id) {
        jdbc.update("DELETE FROM pedido WHERE id = ?", id);
    }
}
