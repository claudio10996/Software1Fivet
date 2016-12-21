package cl.ucn.disc.isof.fivet.domain.service.ebean;
import java.util.ArrayList;
import java.util.List;
import cl.ucn.disc.isof.fivet.domain.model.Persona;
import cl.ucn.disc.isof.fivet.domain.model.Paciente;

import cl.ucn.disc.isof.fivet.domain.model.Control;
import cl.ucn.disc.isof.fivet.domain.service.BackendService;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.util.Date;

/**
 * Clase de testing del {@link BackendService}.
 */
@Slf4j
@FixMethodOrder(MethodSorters.DEFAULT)
public class TestEbeanBackendService {

    /**
     * Todos los test deben terminar antes de 60 segundos.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(60);

    /**
     * Configuracion de la base de datos:  h2, hsql, sqlite
     * WARN: hsql no soporta ENCRYPT
     */
    private static final String DB = "h2";

    /**
     * Backend
     */
    private BackendService backendService;

    /**
     * Cronometro
     */
    private Stopwatch stopWatch;

    /**
     * Antes de cada test
     */
    @Before
    public void beforeTest() {

        stopWatch = Stopwatch.createStarted();
        log.debug("Initializing Test Suite with database: {}", DB);

        backendService = new EbeanBackendService(DB);
        backendService.initialize();
    }

    /**
     * Despues del test
     */
    @After
    public void afterTest() {

        log.debug("Test Suite done. Shutting down the database ..");
        backendService.shutdown();

        log.debug("Test finished in {}", stopWatch.toString());
    }

    /**
     * Test de la persona
     */
    @Test
    public void testPersona() {

        final String rut = "1-1";
        final String nombre = "Este es mi nombre";

        // Insert into backend
        {
            final Persona persona = Persona.builder()
                    .nombre(nombre)
                    .rut(rut)
                    .password("durrutia123")
                    .email("ja@gmail.com")
                    .direccion("calle")
                    .movil("12")
                    .fijo("122")
                    .tipo(Persona.Tipo.VETERINARIO)
                    .build();

            persona.insert();

            log.debug("Persona to insert: {}", persona);
            Assert.assertNotNull("Objeto sin id", persona.getId());
        }

        // Get from backend v1
        {
            final Persona persona = backendService.getPersona(rut);
            log.debug("Persona founded: {}", persona);
            Assert.assertNotNull("Can't find Persona", persona);
            Assert.assertNotNull("Objeto sin id", persona.getId());
            Assert.assertEquals("Rut distintos!", rut, persona.getRut());//Assert.assertEquals("Nombre distintos!", rut, persona.getNombre());
            Assert.assertNotNull("Pacientes null", persona.getPacientes());
            Assert.assertTrue("Pacientes != 0", persona.getPacientes().size() == 0);

            // Update nombre
            persona.setNombre(nombre); //persona.setNombre(nombre + nombre);
            persona.update();
        }

        // Get from backend v2
        {
            final Persona persona = backendService.getPersona(rut);
            log.debug("Persona founded: {}", persona);
            Assert.assertNotNull("Can't find Persona", persona);
            Assert.assertEquals("Nombres distintos!", nombre, persona.getNombre());
        }

    }

    @Test
    public void TestPaciente(){
        int numero =001;
        String nombre ="fifi";
        // Insert into backend
        {
            final Paciente paciente = Paciente.builder()
                    .nombre(nombre)
                    .numero(numero)
                    .fechaNacimiento(new Date(2005,12,22))
                    .raza("chiguagua")
                    .sexo(Paciente.Sexo.HEMBRA)
                    .especie("perro")
                    .color("café")
                    .build();

            paciente.insert();

            log.debug("Paciente to insert: {}", paciente);
            Assert.assertNotNull("Objeto sin id", paciente.getId());
        }

        // Get from backend v1
        {
            final Paciente paciente = backendService.getPaciente(numero);
            log.debug("Persona founded: {}", paciente);
            Assert.assertNotNull("Can't find Paciente", paciente);
            Assert.assertNotNull("Objeto sin id", paciente.getId());
            Assert.assertEquals("Nombre distintos!", nombre, paciente.getNombre());//Assert.assertEquals("Nombre distintos!", rut, persona.getNombre());
            Assert.assertNotNull("Dueños null", paciente.getDuenios());
            Assert.assertTrue("Dueños != 0", paciente.getDuenios().size() == 0);

            // Update nombre
            paciente.setNombre(nombre); //persona.setNombre(nombre + nombre);
            paciente.update();

        }

        // Get from backend v2
        {
            final List<Paciente> pacientes = backendService.getPacientesPorNombre(nombre);
            log.debug("Pacientes founded: {}", pacientes);
            Assert.assertNotNull("Can't find Pacientes", pacientes);
            Assert.assertTrue("Hay más de un paciente con ese nombre, y no debería",1==pacientes.size());
            Assert.assertEquals("Nombres distintos!", nombre, pacientes.get(0).getNombre());
        }

        // Get from backend v3
        {
            final List<Paciente> pacientes = backendService.getPacientes();
            log.debug("Pacientes founded: {}", pacientes);
            Assert.assertNotNull("Can't find Pacientes", pacientes);
            Assert.assertTrue("Hay más de un paciente, y no debería",1==pacientes.size());
            Assert.assertEquals("Nombres distintos!", nombre, pacientes.get(0).getNombre());
        }

    }

    @Test
    public void TestControles(){

        String idCon ="C001";
        int numeroPac=001;
        String nombrePac ="fifi";
        String rutVet="1-1";
        String nombreVet="Este es mi nombre";
        //insertarPersona
        final Persona persona = Persona.builder()
                .nombre(nombreVet)
                .rut(rutVet)
                .password("durrutia123")
                .email("ja@gmail.com")
                .direccion("calle")
                .movil("12")
                .fijo("122")
                .tipo(Persona.Tipo.VETERINARIO)
                .build();

        persona.insert();

        List <Persona> dueños= new ArrayList<Persona>();
        dueños.add(persona);
        //insertarPaciente
        final Paciente paciente = Paciente.builder()
                .nombre(nombrePac)
                .numero(numeroPac)
                .fechaNacimiento(new Date(2005,12,22))
                .raza("chiguagua")
                .sexo(Paciente.Sexo.HEMBRA)
                .especie("perro")
                .color("café")
                .duenios(dueños)
                .build();

        paciente.insert();
        // Insert into backend
        {
            final Control control = Control.builder()
                    .idC(idCon)
                    .fecha(new Date(2005,12,22))
                    .proximoControl(new Date(2006,01,22))
                    .temperatura(" 35°C")
                    .peso("2 kg")
                    .altura("20 cm")
                    .diagnostico("Infección de herida")
                    .nota("Se portó bien.")
                    .rutVeterinario(persona.getRut())
                    .build();

            backendService.agregarControl(control,paciente.getNumero());
        }
        //Get from backend v1
        {
            final List <Control> controles= backendService.getControlesVeterinario(persona.getRut());
            log.debug("Controles founded: {}", controles);
            Assert.assertNotNull("Can't find Controles", controles);
            Assert.assertTrue("Hay más de un paciente con ese nombre, y no debería",1==controles.size());
            Assert.assertEquals("Numeros distintos!", numeroPac, controles.get(0).getNumeroPaciente());
        }

    }


}
