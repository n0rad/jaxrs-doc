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
import org.junit.Test;
import com.sun.jersey.multipart.FormDataParam;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.domain.ParameterType;

@SuppressWarnings("unused")
public class ParameterJerseyParserTest {

    private ParameterDefinition parameter = new ParameterDefinition();
    private ParameterJerseyParser parser = new ParameterJerseyParser();

    @Test
    public void should_support_form_data_param_as_multipart() throws Exception {
        class Test {
            public void getSomething(@FormDataParam("caller") String caller) {
            }
        }

        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(null, parameter, method, 0);

        assertThat(parameter.getType()).isEqualTo(ParameterType.MULTIPART);
        assertThat(parameter.getName()).isEqualTo("caller");
    }

}
