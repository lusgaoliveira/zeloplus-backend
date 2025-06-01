package br.com.zelo.puls.zeloplus.repository;

import br.com.zelo.puls.zeloplus.model.TipoTarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTarefaRepository extends JpaRepository<TipoTarefa, Integer> {
}
