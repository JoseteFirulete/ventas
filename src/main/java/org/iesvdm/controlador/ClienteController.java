package org.iesvdm.controlador;

import java.util.List;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.servicio.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/clientes")
    public String listar(Model model) {
        List<Cliente> listaClientes = clienteService.getAll();
        
        // Añade el atributo al modelo para la vista
        model.addAttribute("listaClientes", listaClientes);
        
        // Retorna la plantilla clientes.html[cite: 14]
        return "clientes"; 
    }
}