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

package org.example.graphql.elasticsearch;

import java.time.Duration;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * Represents a custom container for running an Elasticsearch instance. This class simplifies the setup of an Elasticsearch container for testing or
 * integration purposes.
 *
 * @see FixedHostPortGenericContainer
 */
public class ElasticContainer extends FixedHostPortGenericContainer<ElasticContainer> {

  private static final int ELASTICSEARCH_PORT = 9200;
  private static final String ELASTICSEARCH_IMAGE = "elasticsearch:7.17.18";

  /**
   * Constructs a new ElasticContainer, initializing it with the specified Elasticsearch image. It waits for the Elasticsearch server to start
   * listening on the specified port, with a timeout of 1 minute.
   */
  public ElasticContainer() {
    super(ELASTICSEARCH_IMAGE);
    withFixedExposedPort(ELASTICSEARCH_PORT, ELASTICSEARCH_PORT);
    waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofMinutes(1)));
  }
}
