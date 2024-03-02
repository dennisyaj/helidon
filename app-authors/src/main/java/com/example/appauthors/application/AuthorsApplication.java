package com.example.appauthors.application;


import com.example.appauthors.rest.AuthorRest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.HashSet;
import java.util.Set;

@OpenAPIDefinition(
        tags = {
                @Tag(name="Authors", description="Informacion de autores"),

        },
        info = @Info(
                title="Authors API",
                version = "1.0.0",
                contact = @Contact(
                        name = "Dennis Pilco",

                        email = "dapilcoy1@uce.edu.ec"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html")
                )
)
@ApplicationPath("/API")
@ApplicationScoped
public class AuthorsApplication extends Application {

        @Override
        public Set<Class<?>> getClasses() {

                Set<Class<?>> classes = new HashSet<>();

                classes.add(AuthorRest.class);

                return classes;
        }


}
