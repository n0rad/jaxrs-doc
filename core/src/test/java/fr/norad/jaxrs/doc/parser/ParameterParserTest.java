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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import fr.norad.jaxrs.doc.DocConfig;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.domain.sub.ParameterType;

@RunWith(MockitoJUnitRunner.class)
public class ParameterParserTest {

    private ProjectDefinition p = new ProjectDefinition();

    @Mock
    private ModelParser modelParser;

    @InjectMocks
    private DocConfig config = new DocConfig(Arrays.asList("fr.norad"));

    private ParameterParser parser = new ParameterParser(config);

    interface ParameterInterface {
        public UUID getUUID(@Description("theID") UUID uid);
    }

    class ParamaterAbstract implements ParameterInterface {
        @Override
        public UUID getUUID(@PathParam("pathId") UUID uid) {
            return null;
        }
    }

    class ParameterClass extends ParamaterAbstract {
        @Override
        public UUID getUUID(@DefaultValue("defValue") UUID uid) {
            return super.getUUID(uid);
        }
    }

    @Test
    public void should_find_info() throws Exception {
        ParameterDefinition param = parser.parse(p, ParameterClass.class.getMethod("getUUID", UUID.class), 0);

        assertThat((Object) param.getParamClass()).isEqualTo(UUID.class);
        assertThat(param.getParamType()).isEqualTo(ParameterType.PATH);
        assertThat(param.getDefaultValue()).isEqualTo("defValue");
        assertThat(param.getParamName()).isEqualTo("pathId");
        assertThat(param.getDescription()).isEqualTo("theID");
    }

    @Test
    public void should_call_model_parser() throws Exception {
        ParameterDefinition param = parser.parse(p, ParameterClass.class.getMethod("getUUID", UUID.class), 0);

        verify(modelParser).parse(p, UUID.class);
    }

    @Test
    public void should_support_array_param_type() throws Exception {
        class Test {
            @GET
            public void getSomething(String[] param) {
            }
        }

        Method method = Test.class.getMethod("getSomething", String[].class);
        ParameterDefinition param = parser.parse(p, method, 0);

        assertThat((Object) param.getParamClass()).isEqualTo(String.class);
        assertThat(param.getParamAsList()).isTrue();
    }

    @Test
    public void should_support_list_param_type() throws Exception {
        class Test {
            @GET
            public void getSomething(List<String> param) {
            }
        }

        Method method = Test.class.getMethod("getSomething", List.class);
        ParameterDefinition param = parser.parse(p, method, 0);

        assertThat((Object) param.getParamClass()).isEqualTo(String.class);
        assertThat(param.getParamAsList()).isTrue();
    }

    @Test
    public void should_support_standard_param_type() throws Exception {
        class Test {
            @GET
            public void getSomething(String param) {
            }
        }

        Method method = Test.class.getMethod("getSomething", String.class);
        ParameterDefinition param = parser.parse(p, method, 0);

        assertThat((Object) param.getParamClass()).isEqualTo(String.class);
        assertThat(param.getParamAsList()).isNull();
    }
}
