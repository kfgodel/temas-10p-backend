package convention.persistent;

/**
 * Created by sandro on 27/06/17.
 */
public enum ObligatoriedadDeReunion {

    NO_OBLIGATORIO(true),
    OBLIGATORIO(false);

    private Boolean permiteRecibirVotos;

    ObligatoriedadDeReunion(Boolean value){
        this.permiteRecibirVotos = value;
    }
    public boolean permiteRecibirVotos() {
        return permiteRecibirVotos;
    }
}
