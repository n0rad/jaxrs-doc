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
import javax.ws.rs.GET;
import org.junit.Test;
import fr.norad.jaxrs.doc.api.Description;
import fr.norad.jaxrs.doc.api.Outdated;
import fr.norad.jaxrs.doc.api.domain.ParameterDefinition;

public class ParameterJaxrsDocParserTest {

    private ParameterDefinition parameter = new ParameterDefinition();

    @Test
    public void should_find_deprecated_with_outdate() throws Exception {
        class Test {
            @GET
            public void getSomething(@Outdated(cause = "cause", since = "since", willBeRemovedOn = "2.2") String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        new ParameterJaxrsDocParser().parse(null, parameter, method, 0);

        assertThat(parameter.getDeprecated()).isTrue();
        assertThat(parameter.getDeprecatedSince()).isEqualTo("since");
        assertThat(parameter.getDeprecatedCause()).isEqualTo("cause");
        assertThat(parameter.getDeprecatedWillBeRemovedOn()).isEqualTo("2.2");
    }

    @Test
    public void should_find_description() throws Exception {
        class Test {
            @GET
            public void getSomething(@Description("yop") String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        new ParameterJaxrsDocParser().parse(null, parameter, method, 0);

        assertThat(parameter.getDescription()).isEqualTo("yop");
    }

}
