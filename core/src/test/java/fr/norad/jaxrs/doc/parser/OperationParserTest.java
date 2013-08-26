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

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import fr.norad.jaxrs.doc.DocConfig;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.Summary;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;

@RunWith(MockitoJUnitRunner.class)
public class OperationParserTest {

    private ProjectDefinition p = new ProjectDefinition();
    private ApiDefinition api = new ApiDefinition() {
        {
            setPath("/");
        }
    };

    @Mock
    private ModelParser modelParser;

    @Mock
    private ParameterParser paramParser;

    @InjectMocks
    private DocConfig config = new DocConfig(Arrays.asList("fr.norad"));

    private OperationParser parser = new OperationParser(config);

    class api {
        @GET
        @Path("subpath")
        @Deprecated
        @Summary("summary")
        public String getSomething() {
            return null;
        }

        @DELETE
        public UUID withParam(@QueryParam("myId") UUID id) {
            return id;
        }

    }

    class apiExtended extends api {
        @Override
        @Description("something")
        public String getSomething() {
            return null;
        }

        public String notOperation() {
            return null;
        }

        @Override
        public UUID withParam(@Description("descId") UUID id) {
            return id;
        }
    }

    @Test
    public void should_find_valid_operation() throws Exception {
        assertThat(parser.isOperation(apiExtended.class.getMethod("getSomething"))).isTrue();
        assertThat(parser.isOperation(apiExtended.class.getMethod("notOperation"))).isFalse();
    }

    @Test
    public void should_fill_info() throws Exception {
        Method method = apiExtended.class.getMethod("getSomething");
        OperationDefinition operation = parser.parse(p, api, method);

        assertThat(operation.getHttpMethod()).isEqualTo("GET");
        assertThat(operation.getDescription()).isEqualTo("something");
        assertThat(operation.getMethodName()).isEqualTo("getSomething");
        assertThat(operation.getDeprecated()).isTrue();
        assertThat(operation.getSummary()).isEqualTo("summary");
        assertThat((Object) operation.getResponseClass()).isEqualTo(String.class);
        assertThat(operation.getPath()).isEqualTo("/subpath");
    }

    @Test
    public void should_parse_return_type() throws Exception {
        parser.parse(p, api, apiExtended.class.getMethod("getSomething"));

        verify(modelParser).parse(p, String.class);
    }

    @Test
    public void should_parse_parameters() throws Exception {
        Method method = apiExtended.class.getMethod("withParam", UUID.class);
        parser.parse(p, api, method);

        verify(paramParser).parse(p, method, 0);
    }

    @Test
    public void should_support_array_return_type() throws Exception {
        class Test {
            @GET
            public String[] getSomething() {
                return null;
            }
        }

        Method method = Test.class.getMethod("getSomething");
        OperationDefinition operation = parser.parse(p, api, method);

        assertThat((Object) operation.getResponseClass()).isEqualTo(String.class);
        assertThat(operation.getResponseAsList()).isTrue();
    }

    @Test
    public void should_support_list_return_type() throws Exception {
        class Test {
            @GET
            public List<String> getSomething() {
                return null;
            }
        }

        Method method = Test.class.getMethod("getSomething");
        OperationDefinition operation = parser.parse(p, api, method);

        assertThat((Object) operation.getResponseClass()).isEqualTo(String.class);
        assertThat(operation.getResponseAsList()).isTrue();
    }

    @Test
    public void should_support_standard_return_type() throws Exception {
        class Test {
            @GET
            public String getSomething() {
                return null;
            }
        }

        Method method = Test.class.getMethod("getSomething");
        OperationDefinition operation = parser.parse(p, api, method);

        assertThat((Object) operation.getResponseClass()).isEqualTo(String.class);
        assertThat(operation.getResponseAsList()).isNull();
    }

    @Test
    public void should_build_full_path() throws Exception {
        assertThat(parser.buildFullPath("/", "/")).isEqualTo("/");
        assertThat(parser.buildFullPath("/", null)).isEqualTo("/");
        assertThat(parser.buildFullPath("/", "/settings")).isEqualTo("/settings");
        assertThat(parser.buildFullPath("/users/", "/{id}")).isEqualTo("/users/{id}");
        assertThat(parser.buildFullPath("/users/", "{id}")).isEqualTo("/users/{id}");
        assertThat(parser.buildFullPath("users/", "{id}")).isEqualTo("/users/{id}");
        assertThat(parser.buildFullPath("users", "{id}")).isEqualTo("/users/{id}");
        assertThat(parser.buildFullPath("/users", "{id}")).isEqualTo("/users/{id}");
        assertThat(parser.buildFullPath("/users/", "{id}/")).isEqualTo("/users/{id}");
        assertThat(parser.buildFullPath("/users", null)).isEqualTo("/users");
        assertThat(parser.buildFullPath("/users/", null)).isEqualTo("/users");
    }
}
