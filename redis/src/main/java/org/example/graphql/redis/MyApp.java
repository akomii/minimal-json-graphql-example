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

package org.example.graphql.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>The MyApp class is the entry point for the Spring Boot application. It uses the
 * {@link SpringBootApplication} annotation, which is a convenience annotation that adds all of the
 * following:</p>
 * <ul>
 * <li>{@link org.springframework.context.annotation.Configuration}: Tags the class as a source of bean definitions for the application
 * context.</li>
 * <li>{@link org.springframework.boot.autoconfigure.EnableAutoConfiguration}: Tells Spring Boot to start adding beans based on classpath
 * settings, other beans, and various property settings.</li>
 * <li>{@link org.springframework.context.annotation.ComponentScan}: Tells Spring to look for other components, configurations, and services in
 * the 'com/example/graphql' package, allowing it to find and register the controllers.</li>
 * </ul>
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 04-01-2024
 */
@SpringBootApplication
@ComponentScan(basePackages = {"org.example.graphql.redis", "org.example.graphql.server"})
public class MyApp {

  public static void main(String[] args) {
    SpringApplication.run(MyApp.class, args);
  }
}
