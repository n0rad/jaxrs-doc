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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.junit.Test;
import fr.norad.jaxrs.doc.PropertyAccessor;
import fr.norad.jaxrs.doc.domain.LocalizationDefinition;
import fr.norad.jaxrs.doc.domain.PropertyDefinition;

public class PropertyBeanValidationParserTest {
    private PropertyBeanValidationParser parser = new PropertyBeanValidationParser();
    private PropertyDefinition property = new PropertyDefinition();
    private Map<Locale, LocalizationDefinition> localeDef = new HashMap<>();

    @Test
    public void should_not_fail_for_non_constraints_field() throws Exception {
        class Test {
            public String name;
        }
        Field field = Test.class.getField("name");

        parser.parse(localeDef, property, new PropertyAccessor("name", field, null, null));

        assertThat(property.getConstraints()).isNull();
    }

    @Test
    public void should_not_fail_for_non_constraints_getter() throws Exception {
        class Test {
            public String getName() {
                return null;
            }
        }
        Method method = Test.class.getMethod("getName");

        parser.parse(localeDef, property, new PropertyAccessor("name", null, method, null));

        assertThat(property.getConstraints()).isNull();
    }

    @Test
    public void should_find_validator_on_field() throws Exception {
        class Test {
            @NotNull
            public String name;
        }
        Field field = Test.class.getField("name");

        parser.parse(localeDef, property, new PropertyAccessor("name", field, null, null));

        assertThat(property.getConstraints()).hasSize(1);
        assertThat(property.getConstraints().get(0).getConstraintClass()).isEqualTo(NotNull.class.getName());
    }

    @Test
    public void should_find_validator_on_getter() throws Exception {
        class Test {
            @NotNull
            public String getName() {
                return null;
            }
        }
        Method method = Test.class.getMethod("getName");

        parser.parse(localeDef, property, new PropertyAccessor("name", null, method, null));

        assertThat(property.getConstraints()).hasSize(1);
        assertThat(property.getConstraints().get(0).getConstraintClass()).isEqualTo(NotNull.class.getName());
    }

    @Test
    public void should_find_validator_on_both() throws Exception {
        class Test {
            @Pattern(regexp = "[0-9]*", message = "yop")
            public String name;

            @NotNull
            public String getName() {
                return name;
            }
        }
        Method method = Test.class.getMethod("getName");
        Field field = Test.class.getField("name");

        parser.parse(localeDef, property, new PropertyAccessor("name", field, method, null));

        assertThat(property.getConstraints()).hasSize(2);
        assertThat(property.getConstraints().get(0).getConstraintClass()).isEqualTo(Pattern.class.getName());
        assertThat(property.getConstraints().get(1).getConstraintClass()).isEqualTo(NotNull.class.getName());
    }

    @Test
    public void should_find_cascading_on_field() throws Exception {
        class Test {
            @Valid
            public Complex name;
        }
        Field field = Test.class.getField("name");

        parser.parse(localeDef, property, new PropertyAccessor("name", field, null, null));

        assertThat(property.getValidationCascaded()).isTrue();
    }

    @Test
    public void should_find_cascading_on_getter() throws Exception {
        class Test {
            @Valid
            public Complex getName() {
                return null;
            }
        }
        Method method = Test.class.getMethod("getName");

        parser.parse(localeDef, property, new PropertyAccessor("name", null, method, null));

        assertThat(property.getValidationCascaded()).isTrue();
    }


    class Complex {
    }
}
