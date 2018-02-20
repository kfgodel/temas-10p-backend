package convention.persistent;

import ar.com.kfgodel.temas.model.OrdenarPorVotos;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Esta clase representa una reunion de roots con el temario a realizar
 * Created by kfgodel on 21/08/16.
 */
@Entity
public class Reunion extends PersistableSupport {

    @NotNull
    private LocalDate fecha;
    public static final String fecha_FIELD = "fecha";

    @Enumerated(EnumType.STRING)
    private StatusDeReunion status = StatusDeReunion.PENDIENTE;
    public static final String status_FIELD = "status";

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany( cascade = CascadeType.ALL, mappedBy = TemaDeReunion.reunion_FIELD, orphanRemoval = true)
    @OrderBy(TemaDeReunion.prioridad_FIELD)
    private List<TemaDeReunion> temasPropuestos;
    public static final String temasPropuestos_FIELD = "temasPropuestos";

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<TemaDeReunion> getTemasPropuestos() {
        if (temasPropuestos == null) {
            temasPropuestos = new ArrayList<>();
        }
        return temasPropuestos;
    }

    public StatusDeReunion getStatus() {
        return status;
    }

    public void setStatus(StatusDeReunion status) {
        this.status = status;
    }

    public void setTemasPropuestos(List<TemaDeReunion> temasPropuestos) {
        getTemasPropuestos().clear();
        if (temasPropuestos != null) {
            getTemasPropuestos().addAll(temasPropuestos);
        }
    }

    public static Reunion create(LocalDate fecha) {
        Reunion reunion = new Reunion();
        reunion.fecha = fecha;
        reunion.status = StatusDeReunion.PENDIENTE;
        if(reunion.getTemasPropuestos() == null)
            reunion.setTemasPropuestos(new ArrayList<>());
        return reunion;
    }

    public void cerrarVotacion() {
    this.getTemasPropuestos().sort(Collections.reverseOrder(OrdenarPorVotos.create()));
        for (int i = 0; i < getTemasPropuestos().size(); i++) {
            TemaDeReunion tema = getTemasPropuestos().get(i);
            tema.setPrioridad(i + 1); // Queremos que empiece de 1 la prioridad
        }
        this.status = StatusDeReunion.CERRADA;
    }

    public void reabrirVotacion() {
        this.status = StatusDeReunion.PENDIENTE;
    }

    public Reunion copy() {
        Reunion copia = new Reunion();
        copia.setPersistenceVersion(this.getPersistenceVersion());
        copia.setMomentoDeUltimaModificacion(this.getMomentoDeUltimaModificacion());
        copia.setMomentoDeCreacion(this.getMomentoDeCreacion());
        copia.setId(this.getId());
        copia.setStatus(this.getStatus());
        copia.setFecha(this.getFecha());
        copia.setTemasPropuestos(this.getTemasPropuestos());
        return copia;
    }

    public void agregarTemasGenerales(List<TemaGeneral> temasGenerales) {
        temasGenerales.forEach(temaGeneral -> this.agregarTemaGeneral(temaGeneral));
    }

    public void agregarTemaGeneral(TemaGeneral temaGeneral) {
        TemaDeReunion temaNuevo = temaGeneral.generarTemaPara(this);
        this.agregarTema(temaNuevo);
    }

    private void agregarTema(TemaDeReunion temaNuevo) {

        temasPropuestos.add(temaNuevo);
    }

    public void marcarComoMinuteada(){
        this.setStatus(StatusDeReunion.CON_MINUTA);
    }

    public List<Usuario> usuariosQueVotaron(){
    return getTemasPropuestos().stream().
            map(temaDeReunion -> temaDeReunion.getInteresados())
            .reduce((usuarios, usuarios2) -> {usuarios.addAll(usuarios2);
                                              return usuarios;})
            .orElse(new ArrayList<>());
    }

}
