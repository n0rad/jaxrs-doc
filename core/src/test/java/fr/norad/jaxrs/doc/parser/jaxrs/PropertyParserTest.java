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

import static org.fest.assertions.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.UUID;
import org.junit.Test;
import fr.norad.jaxrs.doc.DocConfig;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.domain.PropertyDefinition;
import fr.norad.jaxrs.doc.parser.jaxrs.PropertyParser;

public class PropertyParserTest {

    private DocConfig config = new DocConfig(Arrays.asList("fr.norad"));

    private PropertyParser parser = new PropertyParser(config);

    private ProjectDefinition project = new ProjectDefinition();

    @Test
    public void should_read_public_field() throws Exception {
        class TheClass {
            @Description("desc")
            public UUID publicField;
        }
        PropertyDefinition property = parser.parse(project, TheClass.class.getField("publicField"), null, null);

        assertThat((Object) property.getPropertyClass()).isEqualTo(UUID.class);
        assertThat(property.getDescription()).isEqualTo("desc");
    }

    @Test
    public void should_read_property() throws Exception {
        class TheClass {
            @Description("desc")
            private UUID field;

            public UUID getField() {
                return field;
            }

            public void setField(UUID field) {
                this.field = field;
            }

        }
        PropertyDefinition property = parser.parse(project, TheClass.class.getDeclaredField("field"),
                TheClass.class.getMethod("getField"), TheClass.class.getMethod("setField", UUID.class));

        assertThat((Object) property.getPropertyClass()).isEqualTo(UUID.class);
        assertThat(property.getDescription()).isEqualTo("desc");
    }

    @Test
    public void should_read_method() throws Exception {
        class TheClass {

            @Description("desc")
            public UUID getField() {
                return null;
            }

            public void setField(UUID field) {
            }

        }
        PropertyDefinition property = parser.parse(project, null, TheClass.class.getMethod("getField"),
                TheClass.class.getMethod("setField", UUID.class));

        assertThat((Object) property.getPropertyClass()).isEqualTo(UUID.class);
        assertThat(property.getDescription()).isEqualTo("desc");
    }

    @Test
    public void should_support_deprecated_on_field() throws Exception {
        class TheClass {
            @Deprecated
            private String genre;

        }

        PropertyDefinition property = parser.parse(project, TheClass.class.getDeclaredField("genre"), null, null);

        assertThat(property.getDeprecated()).isTrue();
    }

    @Test
    public void should_support_outdated_on_setter() throws Exception {
        class TheClass {
            @Outdated(cause = "cause", since = "since")
            public void setField(UUID field) {
            }
        }

        PropertyDefinition property = parser.parse(project, null, null,
                TheClass.class.getMethod("setField", UUID.class));

        assertThat(property.getDeprecated()).isTrue();
        assertThat(property.getDeprecatedCause()).isEqualTo("cause");
        assertThat(property.getDeprecatedSince()).isEqualTo("since");
    }

    @Test
    public void should_support_outdated_on_getter() throws Exception {
        class TheClass {
            @Outdated(cause = "cause", since = "since")
            public UUID getField() {
                return null;
            }
        }

        PropertyDefinition property = parser.parse(project, null, TheClass.class.getMethod("getField"), null);

        assertThat(property.getDeprecated()).isTrue();
        assertThat(property.getDeprecatedCause()).isEqualTo("cause");
        assertThat(property.getDeprecatedSince()).isEqualTo("since");
    }

    @Test
    public void should_support_outdated_on_field() throws Exception {
        class TheClass {
            @Outdated(cause = "cause", since = "since")
            private String genre;
        }

        PropertyDefinition property = parser.parse(project, TheClass.class.getDeclaredField("genre"), null, null);

        assertThat(property.getDeprecated()).isTrue();
        assertThat(property.getDeprecatedCause()).isEqualTo("cause");
        assertThat(property.getDeprecatedSince()).isEqualTo("since");
    }
}
