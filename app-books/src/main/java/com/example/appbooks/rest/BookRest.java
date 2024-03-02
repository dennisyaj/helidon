package com.example.appbooks.rest;

import com.example.appbooks.clients.AuthorsRestClient;
import com.example.appbooks.db.Book;
import com.example.appbooks.dtos.AuthorDto;
import com.example.appbooks.dtos.BookDto;
import com.example.appbooks.repo.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@ApplicationScoped
public class BookRest {

    @Inject
    BookRepository rep;

    @Inject
    @RestClient
    AuthorsRestClient clientAuthors;

    static BookDto fromBook(Book obj) {
        BookDto dto = new BookDto();
        dto.setId(obj.getId());
        dto.setIsbn(obj.getIsbn());
        dto.setTitle(obj.getTitle());
        dto.setPrice(obj.getPrice());
        dto.setAuthorId(obj.getAuthorId());
        return dto;
    }

    @GET
    @Operation(
            summary = "Lista todos los libros",
            description = "Lista todos los books")
    @Counted(name = "findAll", description = "How many times findAll method has been invoked", absolute = true)
    public List<BookDto> findAll() {
        return rep.findAll().stream()
                .map(BookRest::fromBook)
                .map(dto -> {
                    AuthorDto authorDto = clientAuthors.getById(dto.getAuthorId());
                    String aname = String.format("%s, %s", authorDto.getLastName(),
                            authorDto.getFirstName());
                    dto.setAuthorName(aname);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "obtiene el libro ")
    @APIResponse(responseCode = "200",description = "encontro el libro",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "400", description = "libro no encontrado")
    @Counted(name = "findAllById", description = "How many times findAllyId method has been invoked", absolute = true)
    public Response getById( @Parameter(description = " id requerido", required = true) @PathParam("id") Integer id) {

        var book = rep.findById(id);
        if (book== null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Book obj = book;
        BookDto dto = fromBook(obj);
        AuthorDto authorDto = clientAuthors.getById(obj.getAuthorId());
        String aname = String.format("%s, %s", authorDto.getLastName(),
                authorDto.getFirstName());
        dto.setAuthorName(aname);
        return Response.ok(dto).build();
    }

    @POST
    @Operation(summary = "crear book",
            description = "Se crea un book")
    @Timed(name = "create", description = "A measure of how long it takes to create an books", absolute = true)

    public Response create(@RequestBody(description = " book  creado", required = true,
            content = @Content(schema = @Schema(implementation = Book.class)))Book p) {
        rep.create(p);
        return Response.status(Response.Status.CREATED.getStatusCode(), "book created").build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualiza el libro ")
    @APIResponse(responseCode = "200",description = "actualiza el libro",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "400", description = " no actualizado")
    @Timed(name = "updatebooks", description = "A measure of how long it takes to update an books", absolute = true)

    public Response update( @Parameter(description = " id requerido", required = true)@PathParam("id") Integer id,@RequestBody(description = "actualizar libro ", required = true,
            content = @Content(schema = @Schema(implementation = Book.class))) Book bookObj) {

        Book book = rep.findById(id);
        book.setIsbn(bookObj.getIsbn());
        book.setPrice(bookObj.getPrice());
        book.setTitle(bookObj.getTitle());
        //rep.persistAndFlush(book);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Borra el libro ")
    @APIResponse(description = "borra el libro",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "400", description = "borrado")
    @Timed(name = "deleteBooks", description = "A measure of how long it takes to delete an books", absolute = true)
    public Response delete(@Parameter(description = " id requerido", required = true)@PathParam("id") Integer id) {

        rep.delete(id);
        return Response.ok()
                .build();
    }

}
