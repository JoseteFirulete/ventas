package org.iesvdm.dao;

import java.util.List;
import java.util.Optional;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;

public interface PedidoDAO {
    List<PedidoDetalle> getAll();
    Optional<PedidoDetalle> find(int id);
    void create(Pedido pedido);
    void update(Pedido pedido);
    void delete(int id);
}
