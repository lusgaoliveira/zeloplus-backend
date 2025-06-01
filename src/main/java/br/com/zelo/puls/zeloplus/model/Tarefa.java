package br.com.zelo.puls.zeloplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_agendamento", nullable = false)
    private LocalDateTime dataAgendamento;

    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private TipoTarefa tipo;

    @ManyToOne
    @JoinColumn(name = "idoso_id", nullable = false)
    private Idoso idoso;

    @Column(name = "nivel", nullable = false)
    private int nivel;

    @Enumerated(EnumType.STRING)
    private StatusTarefa status;

}
