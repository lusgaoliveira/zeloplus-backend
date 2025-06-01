package br.com.zelo.puls.zeloplus.service;

import br.com.zelo.puls.zeloplus.model.TipoTarefa;
import br.com.zelo.puls.zeloplus.repository.TipoTarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoTarefaService {
    private final TipoTarefaRepository tipoTarefaRepository;

    public TipoTarefaService(TipoTarefaRepository tipoTarefaRepository) {
        this.tipoTarefaRepository = tipoTarefaRepository;
    }

    public TipoTarefa salvar(TipoTarefa tipoTarefa) {
        return tipoTarefaRepository.save(tipoTarefa);
    }

    public List<TipoTarefa> listarTodos() {
        return tipoTarefaRepository.findAll();
    }

    public TipoTarefa buscarPorId(Integer id) {
        return tipoTarefaRepository.findById(id).orElse(null);
    }

}
