package convention.persistent;

/**
 * Created by sandro on 27/06/17.
 */
public enum ObligatoriedadDeReunion {

    NO_OBLIGATORIO(true, 2),
    OBLIGATORIO(false, 1);

    private Boolean permiteRecibirVotos;
    private Integer nivelDePrioridad;

    ObligatoriedadDeReunion(Boolean recibeVotos, Integer unNivelDePrioridad){
        this.permiteRecibirVotos = recibeVotos;
        this.nivelDePrioridad = unNivelDePrioridad;
    }
    public boolean permiteRecibirVotos() {
        return permiteRecibirVotos;
    }

    public Integer prioridad() {
        return nivelDePrioridad;
    }
}
