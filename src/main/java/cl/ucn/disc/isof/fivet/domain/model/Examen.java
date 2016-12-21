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
public class Examen extends BaseModel{


    /**
     * Fecha del examen
     */
    @Getter
    @NotEmpty
    private Date fecha;
}
