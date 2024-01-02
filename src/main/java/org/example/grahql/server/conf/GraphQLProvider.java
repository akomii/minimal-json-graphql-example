/*
 * MIT License
 *
 * Copyright (c) 2024 Alexander Kombeiz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.example.grahql.server.conf;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.IOException;
import java.nio.file.Files;
import org.example.grahql.server.resolvers.AuthorResolver;
import org.example.grahql.server.resolvers.BookResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class GraphQLProvider {

  private final AuthorResolver authorResolver;
  private final BookResolver bookResolver;

  public GraphQLProvider(AuthorResolver authorResolver, BookResolver bookResolver) {
    this.authorResolver = authorResolver;
    this.bookResolver = bookResolver;
  }

  @Bean
  public GraphQL graphQL() throws IOException {
    Resource schemaResource = new ClassPathResource("schema.graphqls");
    TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(
        Files.readString(schemaResource.getFile().toPath()));
    RuntimeWiring runtimeWiring = buildWiring();
    GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring);
    return GraphQL.newGraphQL(schema).build();
  }

  private RuntimeWiring buildWiring() {
    return RuntimeWiring.newRuntimeWiring()
        .type(newTypeWiring("Query")
            .dataFetcher("author", authorResolver.getAuthor())
            .dataFetcher("authors", authorResolver.getAuthors())
            .dataFetcher("book", bookResolver.getBook())
            .dataFetcher("books", bookResolver.getBooks()))
        .type(newTypeWiring("Mutation")
            .dataFetcher("createAuthor", authorResolver.createAuthor())
            .dataFetcher("createBook", bookResolver.createBook()))
        .build();
  }
}
