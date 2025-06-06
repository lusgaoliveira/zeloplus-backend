package br.com.zelo.puls.zeloplus.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas")
@Data
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

    public Tarefa(Integer id, String titulo, String descricao, LocalDateTime dataCriacao, LocalDateTime dataAgendamento, TipoTarefa tipo, Idoso idoso, int nivel, StatusTarefa status) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataAgendamento = dataAgendamento;
        this.tipo = tipo;
        this.idoso = idoso;
        this.nivel = nivel;
        this.status = status;
    }
}
