package br.com.zelo.puls.zeloplus.controller;

import br.com.zelo.puls.zeloplus.model.Idoso;
import br.com.zelo.puls.zeloplus.service.IdosoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/idosos")
public class IdosoController {
    private final IdosoService service;

    public IdosoController(IdosoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Idoso> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/buscaridoso/{id}")
    public ResponseEntity<Idoso> buscarPorId(@PathVariable int id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/gerarcodigo/{id}")
    public String gerarCodigo(@PathVariable int id) {
        return service.gerarCodigoVinculo(id);
    }
}
