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

package org.example.graphql.server;

import java.time.Duration;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * RedisContainer is a custom Testcontainers configuration for a Redis container. It extends
 * FixedHostPortGenericContainer to bind a fixed port for the Redis server. This class is used to
 * create a Redis container for integration tests.
 *
 * @see org.testcontainers.containers.FixedHostPortGenericContainer
 */
public class RedisContainer extends FixedHostPortGenericContainer<RedisContainer> {

  private static final int REDIS_PORT = 6379;
  private static final String REDIS_IMAGE = "redis:7.2.3";

  /**
   * Constructs a new RedisContainer with the default Redis image and port. It waits for the Redis
   * server to start listening on the specified port, with a timeout of 1 minute.
   */
  public RedisContainer() {
    super(REDIS_IMAGE);
    withFixedExposedPort(REDIS_PORT, REDIS_PORT);
    waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofMinutes(1)));
  }
}