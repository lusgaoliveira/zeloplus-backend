package br.com.zelo.puls.zeloplus.controller;

import br.com.zelo.puls.zeloplus.dto.CriarTarefaDTO;
import br.com.zelo.puls.zeloplus.dto.PegarListaTarefaDTO;
import br.com.zelo.puls.zeloplus.dto.PegarTarefaDTO;
import br.com.zelo.puls.zeloplus.model.Tarefa;
import br.com.zelo.puls.zeloplus.service.TarefaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/buscar/{id}")
    public PegarTarefaDTO buscarTarefa(@PathVariable Integer id) {
        return tarefaService.buscarPorId(id);
    }

    @GetMapping("/buscartarefas/{id}")
    public Page<PegarListaTarefaDTO> listarTarefasPorIdoso(
            @PathVariable Integer id,
            @PageableDefault(size = 10, sort = "dataAgendamento", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return tarefaService.buscarTodasTarefas(id, pageable);
    }


    @PatchMapping("/concluir/{id}")
    public ResponseEntity<?> concluirTarefa(@PathVariable Integer id) {
        tarefaService.concluirTarefa(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/excluir/{id}")
    public ResponseEntity<?> excluirTarefa(@PathVariable Integer id) {
        tarefaService.excluirTarefa(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarTarefa(@PathVariable Integer id, @RequestBody Tarefa tarefaAtualizada) {
        tarefaService.atualizarTarefa(id, tarefaAtualizada);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{criterio}")
    public ResponseEntity<?> obterRelatorioPorCriterio(
            @PathVariable String criterio,
            @RequestParam Integer id,
            @RequestParam(required = false) Integer mes
    ) {
        Map<String, Long> resultado;

        switch (criterio.toLowerCase()) {
            case "status":
                resultado = tarefaService.getTarefasPorStatus(id, mes);
                break;
            case "nivel":
                resultado = tarefaService.getTarefasPorNivel(id, mes);
                break;
            case "tipo":
                resultado = tarefaService.getTarefasPorTipo(id, mes);
                break;
            default:
                return ResponseEntity.badRequest().body("Critério inválido");
        }

        return ResponseEntity.ok(resultado);
    }


}