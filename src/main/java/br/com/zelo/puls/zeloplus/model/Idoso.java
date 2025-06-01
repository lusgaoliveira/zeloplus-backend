package br.com.zelo.puls.zeloplus.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "idosos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Idoso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "codigo_vinculo", unique = true)
    private String codigoVinculo;

    @OneToMany(mappedBy = "idoso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarefa> tarefas;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}