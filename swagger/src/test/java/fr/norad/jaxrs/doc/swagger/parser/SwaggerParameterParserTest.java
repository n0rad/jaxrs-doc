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
package fr.norad.jaxrs.doc.swagger.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import org.junit.Test;
import com.wordnik.swagger.annotations.ApiParam;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;

@SuppressWarnings("unused")
public class SwaggerParameterParserTest {

    SwaggerParameterParser parser = new SwaggerParameterParser();
    ParameterDefinition parameter = new ParameterDefinition();

    @Test
    public void should_fill_from_api_param() throws Exception {
        class Test {
            public void method(
                    @ApiParam(value = "yop", name = "name", defaultValue = "defval",
                            allowableValues = "allow", allowMultiple = true, required = true) String param) {
            }
        }

        parser.parse(parameter, Test.class.getDeclaredMethod("method", String.class), 0);

        assertThat(parameter.getDescription()).isEqualTo("yop");
        assertThat(parameter.getName()).isEqualTo("name");
        assertThat(parameter.getDefaultValue()).isEqualTo("defval");
        assertThat(parameter.getAllowedValues()).isEqualTo("allow");
        assertThat(parameter.getAsList()).isTrue();
        assertThat(parameter.getMandatory()).isTrue();
    }

    @Test
    public void should_not_from_api_param() throws Exception {
        class Test {
            public void method(@ApiParam String param) {
            }
        }

        parser.parse(parameter, Test.class.getDeclaredMethod("method", String.class), 0);

        assertThat(parameter.getDescription()).isNull();
        assertThat(parameter.getName()).isNull();
        assertThat(parameter.getDefaultValue()).isNull();
        assertThat(parameter.getAllowedValues()).isNull();
        assertThat(parameter.getAsList()).isNull();
        assertThat(parameter.getMandatory()).isNull();
    }

}
