package cl.ucn.disc.isof.fivet.domain.service.ebean;

import cl.ucn.disc.isof.fivet.domain.model.*;
import cl.ucn.disc.isof.fivet.domain.service.BackendService;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.Expr;
import com.avaje.ebean.config.EncryptKey;
import com.avaje.ebean.config.EncryptKeyManager;
import com.avaje.ebean.config.ServerConfig;
import com.durrutia.ebean.BaseModel;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Null;
import java.util.List;

@Slf4j
public class EbeanBackendService implements BackendService {

    /**
     * EBean server
     */
    private final EbeanServer ebeanServer;

    /**
     *
     */
    public EbeanBackendService(final String database) {

        log.debug("Loading EbeanBackend in database: {}", database);

        /**
         * Configuration
         */
        ServerConfig config = new ServerConfig();
        config.setName(database);
        config.setDefaultServer(true);
        config.loadFromProperties();

        // Don't try this at home
        //config.setAutoCommitMode(false);

        // config.addPackage("package.de.la.clase.a.agregar.en.el.modelo");
        config.addClass(BaseModel.class);

        config.addClass(Persona.class);
        config.addClass(Persona.Tipo.class);

        config.addClass(Paciente.class);
        config.addClass(Paciente.Sexo.class);

        config.addClass(Control.class);
        config.addClass(Examen.class);



        // http://ebean-orm.github.io/docs/query/autotune
        config.getAutoTuneConfig().setProfiling(false);
        config.getAutoTuneConfig().setQueryTuning(false);

        config.setEncryptKeyManager(new EncryptKeyManager() {

            @Override
            public void initialise() {
                log.debug("Initializing EncryptKey ..");
            }

            @Override
            public EncryptKey getEncryptKey(final String tableName, final String columnName) {

                log.debug("gettingEncryptKey for {} in {}.", columnName, tableName);

                // Return the encrypt key
                return () -> tableName + columnName;
            }
        });

        this.ebeanServer = EbeanServerFactory.create(config);

        log.debug("EBeanServer ready to go.");

    }


    /**
     * @param rutEmail
     * @return the Persona
     */
    @Override
    public Persona getPersona(String rutEmail) {
        Persona person =this.ebeanServer.find(Persona.class)
                .where()
                .or(Expr.eq("email",rutEmail),Expr.eq("rut", rutEmail))
                .findUnique();
        return person;

    }

    /**
     * Obtiene el listado de los pacientes.
     *
     * @return the {@link List} of {@link Paciente}
     */
    @Override
    public List<Paciente> getPacientes() {
        return this.ebeanServer.find(Paciente.class).findList();
    }

    /**
     * Obtiene un {@link Paciente} a partir de su numero de ficha.
     *
     * @param numeroPaciente de ficha.
     * @return the {@link Paciente}.
     */
    @Override
    public Paciente getPaciente(Integer numeroPaciente) {
        Paciente paciente =this.ebeanServer.find(Paciente.class)
                .where()
                .eq("numero", numeroPaciente)
                .findUnique();
        return paciente;
    }

    /**
     * Obtiene todos los controles realizados por un veterinario ordenado por fecha de control.
     *
     * @param rutVeterinario del que realizo el control.
     * @return the {@link List} of {@link Control}.
     */
    @Override
    public List<Control> getControlesVeterinario(String rutVeterinario) {
       List<Control> controles=this.ebeanServer.find(Control.class)
               .findList();
        List<Control> controlesVeterinario= Lists.newArrayList();
        for (Control control:controles) {
            if(control.getRutVeterinario().equals(rutVeterinario)){
                controlesVeterinario.add(control);
            }
        }
        return controlesVeterinario;
    }

    /**
     * Obtiene todos los {@link Paciente} que poseen un match en su nombre.
     *
     * @param nombre a buscar, ejemplo: "pep" que puede retornar pepe, pepa, pepilla, etc..
     * @return the {@link List} of {@link Paciente}.
     */
    @Override
    public List<Paciente> getPacientesPorNombre(String nombre) {
        return this.ebeanServer.find(Paciente.class)
                .where()
                .contains("nombre",nombre)
                .findList();

    }

    /**
     * Agrega un {@link Control} a un {@link Paciente} identificado por el numeroPaciente.
     *
     * @param control        a agregar al paciente.
     * @param numeroPaciente a asociar.
     * @throws RuntimeException en caso de no encontrar al paciente.
     */
    @Override
    public void agregarControl(Control control, Integer numeroPaciente) {
        control.setNumeroPaciente(numeroPaciente);
        this.ebeanServer.insert(control);
    }

    /**
     * Inicializa la base de datos
     */
    @Override
    public void initialize() {
        log.info("Initializing Ebean ..");
    }

    /**
     * Cierra la conexion a la BD
     */
    @Override
    public void shutdown() {
        log.debug("Shutting down Ebean ..");

        // TODO: Verificar si es necesario des-registrar el driver
        this.ebeanServer.shutdown(true, false);
    }
}
