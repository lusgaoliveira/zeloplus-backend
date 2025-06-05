package br.com.zelo.puls.zeloplus.service;

import br.com.zelo.puls.zeloplus.dto.CriarTarefaDTO;
import br.com.zelo.puls.zeloplus.dto.PegarListaTarefaDTO;
import br.com.zelo.puls.zeloplus.dto.PegarTarefaDTO;
import br.com.zelo.puls.zeloplus.mapper.TarefaMapper;
import br.com.zelo.puls.zeloplus.model.Idoso;
import br.com.zelo.puls.zeloplus.model.StatusTarefa;
import br.com.zelo.puls.zeloplus.model.Tarefa;
import br.com.zelo.puls.zeloplus.model.TipoTarefa;
import br.com.zelo.puls.zeloplus.repository.TarefaRepository;
import br.com.zelo.puls.zeloplus.repository.TipoTarefaRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class TarefaService {
    private final TarefaRepository tarefaRepository;
    private final IdosoService idosoService;
    private final TipoTarefaRepository tipoTarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository, IdosoService idosoService, TipoTarefaRepository tipoTarefaRepository) {
        this.tarefaRepository = tarefaRepository;
        this.idosoService = idosoService;
        this.tipoTarefaRepository = tipoTarefaRepository;
    }

    public void salvar(CriarTarefaDTO dto) {
        Idoso idoso = idosoService.buscarPorId(dto.idIdoso()).orElseThrow(() -> new IllegalArgumentException("Código do idoso não encontrado"));

        TipoTarefa tipoTarefa = tipoTarefaRepository.findById(dto.idTipoTarefa()).orElseThrow(()-> new IllegalArgumentException("Tipo de tarefa não existe!"));
        Tarefa tarefa = new Tarefa(
                null,
                dto.titulo(),
                dto.descricao(),
                dto.dataCriacao(),
                dto.dataAgendamento(),
                tipoTarefa,
                idoso,
                dto.nivel(),
                StatusTarefa.AGENDADA
        );
        tarefaRepository.save(tarefa);
    }

    public Page<PegarListaTarefaDTO> buscarTodasTarefas(Integer idIdoso, Pageable pageable) {
        return tarefaRepository.findByIdosoId(idIdoso, pageable)
                .map(TarefaMapper::toDTO);
    }

    public PegarTarefaDTO buscarPorId(Integer idTarefa) {
        return TarefaMapper.toReturnDTO(
                tarefaRepository.findById(idTarefa).orElseThrow(() -> new IllegalArgumentException("Tarafa não encontrada")));
    }


    public void concluirTarefa(Integer id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada"));
        tarefa.setStatus(StatusTarefa.CONCLUIDA);
        tarefaRepository.save(tarefa);
    }

    public void atualizarTarefa(Integer id, Tarefa tarefaAtualizada) {
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com id: " + id));

        tarefaExistente.setTitulo(tarefaAtualizada.getTitulo());
        tarefaExistente.setDescricao(tarefaAtualizada.getDescricao());
        tarefaExistente.setDataAgendamento(tarefaAtualizada.getDataAgendamento());
        tarefaExistente.setNivel(tarefaAtualizada.getNivel());

        if (tarefaAtualizada.getTipo() != null && tarefaAtualizada.getTipo().getId() != null) {
            TipoTarefa tipo = tipoTarefaRepository.findById(tarefaAtualizada.getTipo().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo de tarefa não encontrado"));
            tarefaExistente.setTipo(tipo);
        }
        tarefaRepository.save(tarefaExistente);
    }


}
