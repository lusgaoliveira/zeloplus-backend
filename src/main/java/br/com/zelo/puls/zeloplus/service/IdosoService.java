package br.com.zelo.puls.zeloplus.service;

import br.com.zelo.puls.zeloplus.dto.CriarIdosoDTO;
import br.com.zelo.puls.zeloplus.model.Idoso;
import br.com.zelo.puls.zeloplus.model.Usuario;
import br.com.zelo.puls.zeloplus.repository.IdosoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class IdosoService {

    private final IdosoRepository repository;

    public IdosoService(IdosoRepository repository) {
        this.repository = repository;
    }

    public List<Idoso> listarTodos() {
        return repository.findAll();
    }

    public Optional<Idoso> buscarPorId(int id) {
        return repository.findById(id);
    }

    public Optional<Idoso> buscarPorUsuario(Usuario usuario) {
        return repository.findByUsuario(usuario);
    }

    public Idoso salvar(CriarIdosoDTO dto, Usuario usuario) {
        Idoso idoso = new Idoso(
                null,
                dto.nome(),
                dto.dataNascimento(),
                null,
                List.of(),
                usuario
        );
        return repository.save(idoso);
    }

    public void deletar(int id) {
        repository.deleteById(id);
    }

    public String gerarCodigoVinculo(Integer id) {
        Random random = new Random();
        int number = 100_000 + random.nextInt(900_000);
        Idoso idoso = this.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Falha em encontrar o idoso!"));
        idoso.setCodigoVinculo(String.valueOf(number));

        idoso = repository.save(idoso);
        return idoso.getCodigoVinculo();
    }

    public Optional<Idoso> buscarPorCodigo(String codigo) {
        return repository.findByCodigoVinculo(codigo);
    }

}
