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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import org.junit.Test;
import fr.norad.jaxrs.doc.api.domain.ParameterDefinition;

@SuppressWarnings("unused")
public class ParameterJavaParserTest {

    private ParameterDefinition parameter = new ParameterDefinition();
    private ParameterJavaParser parser = new ParameterJavaParser();

    @Test
    public void should_support_map() throws Exception {
        class Test {
            @GET
            public void getSomething(Map<Integer, String> param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", Map.class);

        parser.parse(null, parameter, method, 0);

        assertThat((Object) parameter.getMapKeyClass()).isEqualTo(Integer.class);
        assertThat((Object) parameter.getParamClass()).isEqualTo(String.class);
        assertThat(parameter.getAsList()).isTrue();
    }

    @Test
    public void should_support_map_extended() throws Exception {
        class Test {
            @GET
            public void getSomething(HashMap<Integer, String> param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", HashMap.class);

        parser.parse(null, parameter, method, 0);

        assertThat((Object) parameter.getMapKeyClass()).isEqualTo(Integer.class);
        assertThat((Object) parameter.getParamClass()).isEqualTo(String.class);
        assertThat(parameter.getAsList()).isTrue();
    }

    @Test
    public void should_support_array_param_type() throws Exception {
        class Test {
            @GET
            public void getSomething(String[] param) {
            }
        }

        Method method = Test.class.getMethod("getSomething", String[].class);
        parser.parse(null, parameter, method, 0);

        assertThat((Object) parameter.getParamClass()).isEqualTo(String.class);
        assertThat(parameter.getAsList()).isTrue();
    }

    @Test
    public void should_support_list_param_type() throws Exception {
        class Test {
            @GET
            public void getSomething(List<String> param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", List.class);

        parser.parse(null, parameter, method, 0);

        assertThat((Object) parameter.getParamClass()).isEqualTo(String.class);
        assertThat(parameter.getAsList()).isTrue();
    }

    @Test
    public void should_support_standard_param_type() throws Exception {
        class Test {
            @GET
            public void getSomething(String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(null, parameter, method, 0);

        assertThat((Object) parameter.getParamClass()).isEqualTo(String.class);
        assertThat(parameter.getAsList()).isNull();
    }

    @Test
    public void should_find_deprecated() throws Exception {
        class Test {
            @GET
            public void getSomething(@Deprecated String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(null, parameter, method, 0);

        assertThat(parameter.getDeprecated()).isTrue();
    }

}
