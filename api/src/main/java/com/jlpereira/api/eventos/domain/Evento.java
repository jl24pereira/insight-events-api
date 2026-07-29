package com.jlpereira.api.eventos.domain;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import com.jlpereira.api.categorias.domain.Categoria;
import com.jlpereira.api.eventos.domain.enums.EstadoEvento;
import com.jlpereira.api.eventos.domain.enums.Prioridad;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.id.uuid.UuidVersion7Strategy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    @Id
    @UuidGenerator(algorithm = UuidVersion7Strategy.class)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Generated(event = EventType.INSERT)
    @Column(name = "codigo", insertable = false, updatable = false, length = 30)
    private String codigo;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @Column(name = "fecha", nullable = false)
    private OffsetDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", nullable = false, length = 20)
    private Prioridad prioridad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoEvento estado;

    @Column(name = "fuente", length = 120)
    private String fuente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Evento otro))
            return false;
        return id != null && id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
