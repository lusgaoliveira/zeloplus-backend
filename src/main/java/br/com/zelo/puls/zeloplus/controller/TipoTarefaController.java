package br.com.zelo.puls.zeloplus.controller;

import br.com.zelo.puls.zeloplus.model.TipoTarefa;
import br.com.zelo.puls.zeloplus.service.TipoTarefaService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("api/tipos-tarefa")
public class TipoTarefaController {
    private final TipoTarefaService tipoTarefaService;

    public TipoTarefaController(TipoTarefaService tipoTarefaService) {
        this.tipoTarefaService = tipoTarefaService;
    }

    @GetMapping()
    public List<TipoTarefa> buscarTarefas() {
        return tipoTarefaService.listarTodos();
    }
}
