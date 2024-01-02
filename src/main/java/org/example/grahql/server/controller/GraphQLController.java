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

package org.example.grahql.server.controller;

import graphql.ExecutionResult;
import graphql.GraphQL;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GraphQLController {

  private static final Logger log = LoggerFactory.getLogger(GraphQLController.class);

  private final GraphQL graphQL;

  public GraphQLController(GraphQL graphQL) {
    this.graphQL = graphQL;
  }

  @PostMapping("/graphql")
  public ResponseEntity<Object> handle(@RequestBody Map<String, Object> request) {
    if (!request.containsKey("query")) {
      return ResponseEntity.badRequest().body("No query provided");
    }
    String query = (String) request.get("query");
    log.info("Received GraphQL query: {}", query);
    ExecutionResult result = graphQL.execute(query);
    log.info("GraphQL execution result: {}", result);
    if (result.getErrors().isEmpty()) {
      return ResponseEntity.ok(result.getData());
    } else {
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("error", "An error occurred while processing your request.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }
}
