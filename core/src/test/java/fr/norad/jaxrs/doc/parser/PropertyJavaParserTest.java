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
import java.util.Map;
import java.util.UUID;
import org.junit.Test;
import fr.norad.jaxrs.doc.PropertyAccessor;
import fr.norad.jaxrs.doc.api.domain.PropertyDefinition;

@SuppressWarnings("unused")
public class PropertyJavaParserTest {

    private PropertyDefinition property = new PropertyDefinition();
    private PropertyJavaParser parser = new PropertyJavaParser();

    @Test
    public void should_read_complex_type_from_property() throws Exception {
        class Test {
            private Map<Integer, String> field;
        }

        parser.parse(null, property, new PropertyAccessor("field", Test.class.getDeclaredField("field"), null, null), null);

        assertThat((Object) property.getPropertyClass()).isEqualTo(String.class);
        assertThat((Object) property.getMapKeyClass()).isEqualTo(Integer.class);
    }

    @Test
    public void should_read_type_from_property() throws Exception {
        class Test {
            private UUID field;
        }

        parser.parse(null, property, new PropertyAccessor("field", Test.class.getDeclaredField("field"), null, null), null);

        assertThat((Object) property.getPropertyClass()).isEqualTo(UUID.class);
    }

    @Test
    public void should_read_type_from_getter() throws Exception {
        class Test {
            public UUID getElement() {
                return null;
            }
        }

        parser.parse(null, property, new PropertyAccessor("field", null, Test.class.getMethod("getElement"), null), null);

        assertThat((Object) property.getPropertyClass()).isEqualTo(UUID.class);
    }

    @Test
    public void should_read_type_from_setter() throws Exception {
        class Test {
            public void setElement(UUID field) {
            }
        }

        parser.parse(null, property,
                new PropertyAccessor("field", null, null, Test.class.getMethod("setElement", UUID.class)), null);

        assertThat((Object) property.getPropertyClass()).isEqualTo(UUID.class);
    }

    @Test
    public void should_support_deprecated_on_field() throws Exception {
        class TheClass {
            @Deprecated
            private String genre;

        }

        parser.parse(null, property, new PropertyAccessor("field", TheClass.class.getDeclaredField("genre"), null, null), null);

        assertThat(property.getDeprecated()).isTrue();
    }

    @Test
    public void should_support_array_from_property() throws Exception {
        class TheClass {
            private String[] genre;

        }

        parser.parse(null, property, new PropertyAccessor("field", TheClass.class.getDeclaredField("genre"), null, null), null);

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

        parser.parse(null, property, new PropertyAccessor("field", null, TheClass.class.getMethod("getElement"), null), null);

        assertThat((Object) property.getPropertyClass()).isEqualTo(UUID.class);
        assertThat(property.getAsList()).isTrue();
    }

    @Test
    public void should_not_fail_if_cannot_construct_default_instance() throws Exception {

        parser.parse(null, property, new PropertyAccessor("field", DefaultValueClass.class.getDeclaredField("genre"), null, null), null);

        assertThat(property.getDefaultValue()).isNull();
    }

    @Test
    public void should_fill_default_value() throws Exception {

        parser.parse(null, property, new PropertyAccessor("genre", DefaultValueClass.class.getDeclaredField("genre"), null, null), DefaultValueClass.class);

        assertThat(property.getDefaultValue()).isEqualTo("salut");
    }

    @Test
    public void should_fill_default_value_from_superclass() throws Exception {

        parser.parse(null, property, new PropertyAccessor("field", DefaultValueSuperClass.class.getDeclaredField("genre"),
                null, null), DefaultValueHoverClass.class);

        assertThat(property.getDefaultValue()).isEqualTo("salut");
    }

    @Test
    public void should_find_default_value_setted() throws Exception {

        parser.parse(null, property, new PropertyAccessor("field", DefaultValueSuperClass.class.getDeclaredField("ouda"),
                null, null), DefaultValueHoverClass.class);

        assertThat(property.getDefaultValue()).isEqualTo("plop");
    }

    @Test
    public void should_find_default_value_from_getter() throws Exception {

        parser.parse(null, property, new PropertyAccessor("field", DefaultValueSuperClass.class.getDeclaredField("genre"),
                DefaultValueHoverClass2.class.getMethod("getGenre"), null), DefaultValueHoverClass2.class);

        assertThat(property.getDefaultValue()).isEqualTo("genreOverride");
    }

    /////////////////////////////////////////////////////////////////////////////////

    static class DefaultValueClass {
        private String genre = "salut";

    }

    public static class DefaultValueHoverClass extends DefaultValueSuperClass {
        DefaultValueHoverClass() {
            super("plop");
        }
    }

    public static class DefaultValueHoverClass2 extends DefaultValueSuperClass {
        DefaultValueHoverClass2() {
            super("plop");
        }

        public String getGenre() {
            return "genreOverride";
        }
    }

    public static class DefaultValueSuperClass {

        protected String genre = "salut";
        private String ouda;

        DefaultValueSuperClass(String ouda) {
            this.ouda = ouda;
        }
    }
}
