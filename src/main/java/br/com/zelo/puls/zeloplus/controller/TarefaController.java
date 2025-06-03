package br.com.zelo.puls.zeloplus.controller;

import br.com.zelo.puls.zeloplus.dto.CriarTarefaDTO;
import br.com.zelo.puls.zeloplus.dto.PegarListaTarefaDTO;
import br.com.zelo.puls.zeloplus.dto.PegarTarefaDTO;
import br.com.zelo.puls.zeloplus.service.TarefaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@RequestMapping("/api/tarefas")
public class TarefaController {
    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criar(@RequestBody @Validated CriarTarefaDTO dto) {
        tarefaService.salvar(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/buscar/{id}")
    public PegarTarefaDTO buscarTarefa(@PathVariable Integer id) {
        return tarefaService.buscarPorId(id);
    }

    @GetMapping("/buscartarefas/{id}")
    public Page<PegarListaTarefaDTO> listarTarefasPorIdoso(
            @PathVariable Integer id,
            @PageableDefault(size = 10, sort = "dataAgendamento") Pageable pageable
    ) {
        return tarefaService.buscarTodasTarefas(id, pageable);
    }

}