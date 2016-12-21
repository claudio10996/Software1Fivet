package cl.ucn.disc.isof.fivet.domain.model;

import com.avaje.ebean.annotation.Encrypted;
import com.avaje.ebean.annotation.EnumValue;
import com.durrutia.ebean.BaseModel;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Claudio Gonzalez on 09-11-2016.
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Control extends BaseModel{

    /**
     * Identificador del control
     */
    @Getter
    @NotEmpty
    @Column(nullable = false)
    private String idC;

    /**
     * Fecha del control
     */
    @Getter
    @NotEmpty
    @Column(nullable = false)
    private Date fecha;

    /**
     * Fecha del proximo control
     */
    @Getter
    @NotEmpty
    @Column(nullable = false)
    private Date proximoControl;

    /**
     * Temperatura del paciente
     */
    @Getter
    @NotEmpty
    @Column(nullable = false)
    private String temperatura;

    /**
     * Peso del paciente
     */
    @Getter
    @NotEmpty
    @Column(nullable = false)
    private String peso;

    /**
     * Altura del paciente
     */
    @Getter
    @NotEmpty
    @Column(nullable = false)
    private String altura;

    /**
     * Diagnóstico del paciente
     */
    @Getter
    @NotEmpty
    @Column(nullable = false)
    private String diagnostico;

    /**
     * Nota
     */
    @Getter
    @NotEmpty
    @Column(nullable = false)
    private int nota;

    /**
     * Listado de exámenes que se solicitan
     */
    @Getter
    @ManyToMany
    private List<Examen> examenes;

    /**
     * El paciente al que se le hace control
     */
    @Getter
    @NotEmpty
    @Column(nullable = false)
    private Paciente paciente;

    /**
     * El veterinario que realiza el control
     */
    @Getter
    @NotEmpty
    @Column(nullable = false)
    private Persona veterinario;
}
