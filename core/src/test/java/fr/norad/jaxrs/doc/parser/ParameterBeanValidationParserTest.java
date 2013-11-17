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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import org.hibernate.validator.constraints.NotBlank;
import org.junit.Test;
import fr.norad.jaxrs.doc.domain.ConstraintDefinition;
import fr.norad.jaxrs.doc.domain.LocalizationDefinition;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;

public class ParameterBeanValidationParserTest {
    private ParameterDefinition parameter = new ParameterDefinition();
    private ParameterBeanValidationParser parser = new ParameterBeanValidationParser();
    private Map<Locale, LocalizationDefinition> localeDef = new HashMap<>();

    @Test
    public void should_not_fail_on_non_constraint_parameter() throws Exception {
        class Test {
            @GET
            public void getSomething(Map<Integer, String> param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", Map.class);

        parser.parse(localeDef, parameter, method, 0);

        assertThat(parameter.getConstraints()).isNull();
    }

    @Test
    public void should_find_not_null() throws Exception {
        class Test {
            @GET
            public void getSomething(@NotNull Map<Integer, String> param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", Map.class);

        parser.parse(localeDef, parameter, method, 0);

        assertThat(parameter.getConstraints()).hasSize(1);
        assertThat(parameter.getConstraints().get(0).getConstraintClass()).isEqualTo(NotNull.class.getName());
    }

    @Test
    public void should_report_as_single() throws Exception {
        class Test {
            @GET
            public void getSomething(@NotBlank String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(localeDef, parameter, method, 0);

        assertThat(parameter.getConstraints()).hasSize(1);
        assertThat(parameter.getConstraints().get(0).getConstraintClass()).isEqualTo(NotBlank.class.getName());
        assertThat(parameter.getConstraints().get(0).getReportAsSingle()).isTrue();
        List<ConstraintDefinition> composingConstraints = new ArrayList<>(parameter.getConstraints().get(0)
                                                                                   .getComposingConstraints());
        assertThat(composingConstraints).hasSize(1);
        assertThat(composingConstraints.get(0).getConstraintClass()).isEqualTo(NotNull.class.getName());
    }

    @Test
    public void should_find_cascading() throws Exception {
        class Complex {
            @NotNull
            public String name;
        }
        class Test {
            @GET
            public void getSomething(@Valid Complex param, Complex param2) {
            }
        }
        Method method = Test.class.getMethod("getSomething", Complex.class, Complex.class);

        parser.parse(null, parameter, method, 0);
        ParameterDefinition parameter2 = new ParameterDefinition();
        parser.parse(null, parameter2, method, 1);

        assertThat(parameter.getConstraints()).isNull();
        assertThat(parameter.getValidationCascaded()).isTrue();
        assertThat(parameter2.getConstraints()).isNull();
        assertThat(parameter2.getValidationCascaded()).isNull();
    }



}

