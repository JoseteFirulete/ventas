package org.iesvdm.dao;

import java.util.List;
import java.util.Optional;
import org.iesvdm.modelo.Comercial;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository // <-- ¡Esta anotación es obligatoria para que Spring lo detecte!
public class ComercialDAOImpl implements ComercialDAO {

    private final JdbcTemplate jdbcTemplate;

    public ComercialDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Comercial comercial) {
        // Tu código SQL para insertar
    }

    @Override
    public List<Comercial> getAll() {
        return jdbcTemplate.query("SELECT * FROM comercial", (rs, rowNum) -> new Comercial(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("apellido1"),
            rs.getString("apellido2"),
            rs.getDouble("comisión") // <-- Con tilde como en la base de datos
        ));
    }

    @Override
    public Optional<Comercial> find(int id) {
        // Tu código SQL para buscar por ID
        return Optional.empty();
    }

    @Override
    public void update(Comercial comercial) {
        // Tu código SQL para actualizar
    }

    @Override
    public void delete(int id) {
        // Tu código SQL para borrar
    }
}