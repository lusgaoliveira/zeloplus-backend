package br.com.zelo.puls.zeloplus.service;

import br.com.zelo.puls.zeloplus.dto.CriarTarefaDTO;
import br.com.zelo.puls.zeloplus.dto.PegarListaTarefaDTO;
import br.com.zelo.puls.zeloplus.dto.PegarTarefaDTO;
import br.com.zelo.puls.zeloplus.mapper.TarefaMapper;
import br.com.zelo.puls.zeloplus.model.*;
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
    private final UsuarioService usuarioService;

    public TarefaService(TarefaRepository tarefaRepository, IdosoService idosoService, TipoTarefaRepository tipoTarefaRepository,
                         UsuarioService usuarioService) {
        this.tarefaRepository = tarefaRepository;
        this.idosoService = idosoService;
        this.tipoTarefaRepository = tipoTarefaRepository;
        this.usuarioService = usuarioService;
    }

    public void salvar(CriarTarefaDTO dto) {

        Usuario usuario = usuarioService.buscarPorId(dto.id());

        System.out.println(usuario.getId());
        Idoso idoso = idosoService.buscarPorUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("Idoso não encontrado"));

        System.out.println(idoso.getId());
        TipoTarefa tipoTarefa = tipoTarefaRepository.findById(dto.idTipoTarefa())
                .orElseThrow(()-> new IllegalArgumentException("Tipo de tarefa não existe!"));

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

    public Page<PegarListaTarefaDTO> buscarTodasTarefas(Integer id, Pageable pageable) {
        var usuario = usuarioService.buscarPorId(id);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não econtrado!");
        } else{
            var idoso = idosoService.buscarPorUsuario(usuario)
                    .orElseThrow(() -> new IllegalArgumentException("Idoso não encontrado"));

            return tarefaRepository.findByIdosoIdAndStatusNot(idoso.getId(), StatusTarefa.EXCLUIDA, pageable)
                    .map(TarefaMapper::toDTO);
        }

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

    public void excluirTarefa(Integer id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada"));
        tarefa.setStatus(StatusTarefa.EXCLUIDA);
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
