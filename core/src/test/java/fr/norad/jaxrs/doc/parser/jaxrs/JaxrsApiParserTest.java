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
package fr.norad.jaxrs.doc.parser.jaxrs;

import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.lang.reflect.Method;
import java.util.Arrays;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import fr.norad.jaxrs.doc.DocConfig;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parser.jaxrs.JaxrsApiParser;

@RunWith(MockitoJUnitRunner.class)
public class JaxrsApiParserTest {

    private ProjectDefinition p = new ProjectDefinition();

    @Mock
    private OperationParser operationParser;

    @InjectMocks
    private DocConfig config = new DocConfig(Arrays.asList("fr.norad"));

    private JaxrsApiParser parser = new JaxrsApiParser(config);

    @Path("there")
    class TestResource {

    }

    @Path("mypath")
    interface TestInterace {

    }

    class TestInterface implements TestInterace {

    }

    class TestExtends extends TestResource {

    }

    class TestOperation extends TestResource {
        @GET
        public void operation() {

        }

        public void notOperation() {

        }
    }

    ArgumentCaptor<ApiDefinition> captor = ArgumentCaptor.forClass(ApiDefinition.class);

    @Test
    public void should_support_consume_media_type() throws Exception {
        @Path("/")
        @Consumes({ APPLICATION_ATOM_XML, APPLICATION_JSON })
        class Test {
            @GET
            public String getSomething() {
                return null;
            }
        }

        ApiDefinition api = parser.parse(p, Test.class);

        assertThat(api.getConsumes()).hasSize(2);
        assertThat(api.getConsumes()).containsExactly(APPLICATION_ATOM_XML, APPLICATION_JSON);
    }

    @Test
    public void should_support_produce_media_type() throws Exception {
        @Path("/")
        @Produces({ APPLICATION_ATOM_XML, APPLICATION_JSON })
        class Test {
            @GET
            public String getSomething() {
                return null;
            }
        }

        ApiDefinition api = parser.parse(p, Test.class);

        assertThat(api.getProduces()).hasSize(2);
        assertThat(api.getProduces()).containsExactly(APPLICATION_ATOM_XML, APPLICATION_JSON);
    }

    @Test
    public void should_support_deprecated() throws Exception {
        @Path("/")
        @Deprecated
        class Test {
            @GET
            public String getSomething() {
                return null;
            }
        }

        ApiDefinition api = parser.parse(p, Test.class);

        assertThat(api.getDeprecated()).isTrue();
        assertThat(api.getDeprecatedCause()).isNull();
        assertThat(api.getDeprecatedSince()).isNull();
    }

    @Test
    public void should_support_outdated() throws Exception {
        @Path("/")
        @Deprecated
        @Outdated(cause = "sux", since = "since")
        class Test {
            @GET
            public String getSomething() {
                return null;
            }
        }

        ApiDefinition api = parser.parse(p, Test.class);

        assertThat(api.getDeprecated()).isTrue();
        assertThat(api.getDeprecatedCause()).isEqualTo("sux");
        assertThat(api.getDeprecatedSince()).isEqualTo("since");
    }

    @Test
    public void should_call_operation_parser() throws Exception {
        Method method = TestOperation.class.getMethod("operation");
        when(operationParser.isOperation(method)).thenReturn(true);
        when(operationParser.isOperation(TestOperation.class.getMethod("notOperation"))).thenReturn(false);

        parser.parse(p, TestOperation.class);
        verify(operationParser).parse(eq(p), captor.capture(), eq(method));
        assertThat(captor.getValue().getPath()).isEqualTo("there");
        assertThat((Object) captor.getValue().getResourceClass()).isEqualTo(TestOperation.class);
        verify(operationParser, never()).parse(eq(p), captor.capture(),
                eq(TestOperation.class.getMethod("notOperation")));
    }
}
