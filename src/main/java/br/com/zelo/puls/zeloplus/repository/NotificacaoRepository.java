package br.com.zelo.puls.zeloplus.repository;

import br.com.zelo.puls.zeloplus.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {
}
