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
import java.util.UUID;
import org.junit.Test;
import fr.norad.jaxrs.doc.PropertyAccessor;
import fr.norad.jaxrs.doc.api.Description;
import fr.norad.jaxrs.doc.api.Outdated;
import fr.norad.jaxrs.doc.api.domain.PropertyDefinition;

@SuppressWarnings("unused")
public class PropertyJaxrsDocParserTest {

    private PropertyDefinition property = new PropertyDefinition();
    private PropertyJaxrsDocParser parser = new PropertyJaxrsDocParser();

    @Test
    public void should_support_outdated_on_setter() throws Exception {
        class TheClass {
            @Outdated(cause = "cause", since = "since", willBeRemovedOn = "2.2")
            public void setField(UUID field) {
            }
        }

        parser.parse(null, property,
                new PropertyAccessor("field", null, null, TheClass.class.getMethod("setField", UUID.class)), null);

        assertThat(property.getDeprecated()).isTrue();
        assertThat(property.getDeprecatedCause()).isEqualTo("cause");
        assertThat(property.getDeprecatedSince()).isEqualTo("since");
        assertThat(property.getDeprecatedWillBeRemovedOn()).isEqualTo("2.2");
    }

    @Test
    public void should_support_outdated_on_getter() throws Exception {
        class TheClass {
            @Outdated(cause = "cause", since = "since")
            public UUID getField() {
                return null;
            }
        }

        parser.parse(null, property, new PropertyAccessor("field", null, TheClass.class.getMethod("getField"), null), null);

        assertThat(property.getDeprecated()).isTrue();
        assertThat(property.getDeprecatedCause()).isEqualTo("cause");
        assertThat(property.getDeprecatedSince()).isEqualTo("since");
    }

    @Test
    public void should_support_outdated_on_field() throws Exception {
        class TheClass {
            @Outdated(cause = "cause", since = "since", willBeRemovedOn = "2.2")
            private String genre;
        }

        parser.parse(null, property, new PropertyAccessor("field", TheClass.class.getDeclaredField("genre"), null, null), null);

        assertThat(property.getDeprecated()).isTrue();
        assertThat(property.getDeprecatedCause()).isEqualTo("cause");
        assertThat(property.getDeprecatedSince()).isEqualTo("since");
        assertThat(property.getDeprecatedWillBeRemovedOn()).isEqualTo("2.2");
    }

    @Test
    public void should_fill_description_from_getter() throws Exception {
        class TheClass {
            @Description("desc")
            public UUID getField() {
                return null;
            }
        }
        parser.parse(null, property, new PropertyAccessor("field", null, TheClass.class.getMethod("getField"), null), null);

        assertThat(property.getDescription()).isEqualTo("desc");
    }

    @Test
    public void should_fill_description_from_setter() throws Exception {
        class TheClass {
            @Description("desc")
            public void setField(UUID field) {
            }
        }
        parser.parse(null, property,
                new PropertyAccessor("field", null, null, TheClass.class.getMethod("setField", UUID.class)), null);

        assertThat(property.getDescription()).isEqualTo("desc");
    }

    @Test
    public void should_fill_description_from_property() throws Exception {
        class TheClass {
            @Description("desc")
            private UUID field;
        }
        parser.parse(null, property, new PropertyAccessor("field", TheClass.class.getDeclaredField("field"), null, null), null);

        assertThat(property.getDescription()).isEqualTo("desc");
    }
}
