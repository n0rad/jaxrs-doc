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
import fr.norad.jaxrs.doc.domain.PropertyDefinition;

@SuppressWarnings("unused")
public class PropertyJavaParserTest {

    private PropertyDefinition property = new PropertyDefinition();
    private PropertyJavaParser parser = new PropertyJavaParser();

    @Test
    public void should_read_type_from_property() throws Exception {
        class Test {
            private UUID field;
        }

        parser.parse(property, Test.class.getDeclaredField("field"), null, null);

        assertThat((Object) property.getPropertyClass()).isEqualTo(UUID.class);
    }

    @Test
    public void should_read_type_from_getter() throws Exception {
        class Test {
            public UUID getElement() {
                return null;
            }
        }

        parser.parse(property, null, Test.class.getMethod("getElement"), null);

        assertThat((Object) property.getPropertyClass()).isEqualTo(UUID.class);
    }

    @Test
    public void should_read_type_from_setter() throws Exception {
        class Test {
            public void setElement(UUID field) {
            }
        }

        parser.parse(property, null, null, Test.class.getMethod("setElement", UUID.class));

        assertThat((Object) property.getPropertyClass()).isEqualTo(UUID.class);
    }

    @Test
    public void should_support_deprecated_on_field() throws Exception {
        class TheClass {
            @Deprecated
            private String genre;

        }

        parser.parse(property, TheClass.class.getDeclaredField("genre"), null, null);

        assertThat(property.getDeprecated()).isTrue();
    }

    @Test
    public void should_support_array_from_property() throws Exception {
        class TheClass {
            private String[] genre;

        }

        parser.parse(property, TheClass.class.getDeclaredField("genre"), null, null);

        assertThat((Object) property.getPropertyClass()).isEqualTo(String.class);
        assertThat(property.getAsList()).isTrue();
    }

    @Test
    public void should_support_array_from_getter() throws Exception {
        class TheClass {
            public UUID[] getElement() {
                return null;
            }
        }

        parser.parse(property, null, TheClass.class.getMethod("getElement"), null);

        assertThat((Object) property.getPropertyClass()).isEqualTo(UUID.class);
        assertThat(property.getAsList()).isTrue();
    }
}
