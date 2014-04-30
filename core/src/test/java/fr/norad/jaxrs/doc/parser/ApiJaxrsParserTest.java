/**
 *
 *     Copyright (C) norad.fr
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package fr.norad.jaxrs.doc.parser;

import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.api.Assertions.assertThat;
import java.lang.reflect.Method;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.junit.Test;
import fr.norad.jaxrs.doc.api.domain.ApiDefinition;

public class ApiJaxrsParserTest {

    @Test
    public void should_fill_path() throws Exception {
        @Path("/yop")
        class Test {
        }
        ApiDefinition api = new ApiDefinition();
        new ApiJaxrsParser().parse(api, Test.class);

        assertThat(api.getPath()).isEqualTo("/yop");
    }

    @Test
    public void should_fill_consume_media_type() throws Exception {
        @Consumes({ APPLICATION_ATOM_XML, APPLICATION_JSON })
        class Test {
        }
        ApiDefinition api = new ApiDefinition();
        new ApiJaxrsParser().parse(api, Test.class);

        assertThat(api.getConsumes()).hasSize(2);
        assertThat(api.getConsumes()).containsExactly(APPLICATION_ATOM_XML, APPLICATION_JSON);
    }

    @Test
    public void should_fill_produce_media_type() throws Exception {
        @Produces({ APPLICATION_ATOM_XML, APPLICATION_JSON })
        class Test {
        }
        ApiDefinition api = new ApiDefinition();
        new ApiJaxrsParser().parse(api, Test.class);

        assertThat(api.getProduces()).hasSize(2);
        assertThat(api.getProduces()).containsExactly(APPLICATION_ATOM_XML, APPLICATION_JSON);
    }

    @Test
    public void should_find_operation() throws Exception {
        class Test {
            @GET
            public void call() {

            }

            public void method() {
            }
        }
        Set<Method> findOperations = new ApiJaxrsParser().findOperations(Test.class);

        assertThat(findOperations).hasSize(1);
        assertThat(findOperations.iterator().next()).isEqualTo(Test.class.getDeclaredMethod("call"));
    }
}
