package br.com.zelo.puls.zeloplus.mapper;

import br.com.zelo.puls.zeloplus.dto.PegarListaTarefaDTO;
import br.com.zelo.puls.zeloplus.dto.PegarTarefaDTO;
import br.com.zelo.puls.zeloplus.model.Tarefa;

import java.util.List;
import java.util.stream.Collectors;

public class TarefaMapper {
    public static PegarListaTarefaDTO toDTO(Tarefa tarefa) {
        return new PegarListaTarefaDTO(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getStatus().name(),
                tarefa.getDataAgendamento()
        );
    }

    public static List<PegarListaTarefaDTO> toDTOList(List<Tarefa> tarefas) {
        return tarefas.stream()
                .map(TarefaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static PegarTarefaDTO toReturnDTO(Tarefa tarefa) {
        if (tarefa == null) {
            return null;
        }

        return new PegarTarefaDTO(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getDataCriacao(),
                tarefa.getDataAgendamento(),
                tarefa.getTipo(),
                tarefa.getNivel(),
                tarefa.getStatus()
        );
    }
}
