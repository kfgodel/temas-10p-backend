package convention.rest.api;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.operations.basic.DeleteById;
import ar.com.kfgodel.orm.api.operations.basic.FindAll;
import ar.com.kfgodel.orm.api.operations.basic.FindById;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.webbyconvention.impl.auth.adapters.JettyIdentityAdapter;
import convention.persistent.Reunion;
import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;
import convention.rest.api.tos.ReunionTo;
import convention.rest.api.tos.TemaEnCreacionTo;
import convention.rest.api.tos.TemaTo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collector;

/**
 * Esta clase representa el recurso rest para modificar temas
 * Created by kfgodel on 03/03/15.
 */
@Produces("application/json")
@Consumes("application/json")
public class TemaResource extends Resource {

  @Inject
  private DependencyInjector appInjector;

  private static final Type LISTA_DE_TEMAS_TO = new ReferenceOf<List<TemaTo>>() {
  }.getReferencedType();

  // Esto no debería funcionar, así que no se debe estar usando en ningún lado
  @GET
  public List<ReunionTo> getAll() {
    return createOperation()
      .insideASession()
      .applying(FindAll.of(TemaDeReunion.class))
      .convertTo(LISTA_DE_TEMAS_TO);
  }

  @POST
  public TemaTo create(TemaEnCreacionTo newState) {
    return createOperation()
      .insideATransaction()
      .taking(newState)
      .convertingTo(TemaDeReunion.class)
      .applyingResultOf(Save::create)
      .convertTo(TemaTo.class);
  }

  @GET
  @Path("/{resourceId}")
  public TemaTo getSingle(@PathParam("resourceId") Long id) {
    return createOperation()
      .insideASession()
      .applying(FindById.create(TemaDeReunion.class, id))
      .mapping((encontrado) -> {
        // Answer 404 if missing
        return encontrado.orElseThrowRuntime(() -> new WebApplicationException("tema not found", 404));
      })
      .convertTo(TemaTo.class);
  }

  //este put ya no se usa, queda reservado por si en algun futuro se quiere modificar un tema
 /* @PUT
  @Path("/{resourceId}")
  public TemaTo update(TemaTo newState, @PathParam("resourceId") Long id) {

    return createOperation()
      .insideATransaction()
      .taking(newState)
      .convertingTo(TemaDeReunion.class)
      .mapping((encontrado) -> {
        // Answer 404 if missing
        if (encontrado == null) {
          throw new WebApplicationException("tema not found", 404);
        }
        return encontrado;
      }).applyingResultOf(Save::create)
      .convertTo(TemaTo.class);
  }*/
  @GET
  @Path("votar/{resourceId}")
  public TemaTo votar(@PathParam("resourceId") Long id, @Context SecurityContext securityContext) {

    TemaTo temaAActualizar=getSingle(id);
    Usuario usuarioActual=usuarioActual(securityContext);

    return createOperation()
            .insideATransaction()
            .taking(temaAActualizar)
            .convertingTo(TemaDeReunion.class)
            .mapping((encontrado) -> {
              // Answer 404 if missing
              if (encontrado == null) {
                throw new WebApplicationException("tema not found", 404);
              }
              long cantidadDeVotos=encontrado.getInteresados().stream()
                      .filter(usuario ->
                              usuario.getId().equals(usuarioActual.getId())).count();
              if (cantidadDeVotos>=3) {
                throw new WebApplicationException("excede la cantidad de votos permitidos", 409);
              }
              encontrado.agregarInteresado(usuarioActual);
              return encontrado;
            }).applyingResultOf(Save::create)
            .convertTo(TemaTo.class);
  }
  @GET
  @Path("desvotar/{resourceId}")
  public TemaTo desvotar(@PathParam("resourceId") Long id, @Context SecurityContext securityContext) {
      Usuario usuarioActual=usuarioActual(securityContext);
    TemaTo temaAActualizar=getSingle(id);

    return createOperation()
            .insideATransaction()
            .taking(temaAActualizar)
            .convertingTo(TemaDeReunion.class)
            .mapping((encontrado) -> {
              // Answer 404 if missing
              if (encontrado == null) {
                throw new WebApplicationException("tema not found", 404);
              }
              long cantidadDeVotos=encontrado.getInteresados().stream()
                      .filter(usuario ->
                              usuario.getId().equals(usuarioActual.getId())).count();
              if (cantidadDeVotos<=0) {
                throw new WebApplicationException("el usuario no tiene votos en el tema", 409);
              }
              encontrado.quitarInteresado(usuarioActual);
              return encontrado;
            }).applyingResultOf(Save::create)
            .convertTo(TemaTo.class);
  }
  @DELETE
  @Path("/{resourceId}")
  public void delete(@PathParam("resourceId") Long id) {
    createOperation()
      .insideATransaction()
      .apply(DeleteById.create(TemaDeReunion.class, id));
  }

  public static TemaResource create(DependencyInjector appInjector) {
    TemaResource resource = new TemaResource();
    resource.appInjector = appInjector;
    return resource;
  }

}
