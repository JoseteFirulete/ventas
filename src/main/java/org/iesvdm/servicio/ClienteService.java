package org.iesvdm.servicio;

import java.util.List;
import org.iesvdm.modelo.Cliente;

public interface ClienteService {
    List<Cliente> getAll();
}