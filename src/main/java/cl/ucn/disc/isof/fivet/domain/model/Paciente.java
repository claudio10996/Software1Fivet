package cl.ucn.disc.isof.fivet.domain.model;

import com.avaje.ebean.annotation.EnumValue;
import com.durrutia.ebean.BaseModel;
import lombok.*;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa a un Paciente de la veterinaria.
 *
 * @author Diego P. Urrutia Astorga
 * @version 20161102
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Paciente extends BaseModel {

    /**
     * Numero de la ficha
     */
    @Getter
    @Column
    private Integer numero;

    /**
     * Nombre del paciente
     */
    @Getter
    @Setter
    @Column
    private String nombre;

    /**
     * Fecha de nacimiento
     */
    @Getter
    @Setter
    @Column
    private Date fechaNacimiento;

    /**
     * Raza
     */
    @Getter
    @Setter
    @Column
    private String raza;

    /**
     * Sexo
     */
    private Sexo sexo;

    /**
     * Color
     */
    @Getter
    @Setter
    private String color;

    /**
     * Sexo?
     */
    public enum Sexo {
        @EnumValue("Macho")
        MACHO,

        @EnumValue("Hembra")
        HEMBRA,

        @EnumValue("Indeterminado")
        INDETERMINADO,
    }
    /**
     * Especie
     */
    @Getter
    @Setter
    private String especie;

    /**
     * Listado de controles del paciente
     */
    @Getter
    @ManyToMany
    @OrderBy("id")
    private List<Control> controles;

    /**
     * Listado de Personas que son dueños del paciente
     */
    @Getter
    @ManyToMany
    @OrderBy("rut")
    private List<Persona> dueños;

    public void addControl(Control control){
        this.controles.add(control);
    }


}
