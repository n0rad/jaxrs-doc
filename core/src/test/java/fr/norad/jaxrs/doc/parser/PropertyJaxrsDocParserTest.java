package fr.norad.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import java.util.UUID;
import org.junit.Test;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.domain.PropertyDefinition;

@SuppressWarnings("unused")
public class PropertyJaxrsDocParserTest {

    private PropertyDefinition property = new PropertyDefinition();
    private PropertyJaxrsDocParser parser = new PropertyJaxrsDocParser();

    @Test
    public void should_support_outdated_on_setter() throws Exception {
        class TheClass {
            @Outdated(cause = "cause", since = "since")
            public void setField(UUID field) {
            }
        }

        parser.parse(property, null, null, TheClass.class.getMethod("setField", UUID.class));

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

        parser.parse(property, null, TheClass.class.getMethod("getField"), null);

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

        parser.parse(property, TheClass.class.getDeclaredField("genre"), null, null);

        assertThat(property.getDeprecated()).isTrue();
        assertThat(property.getDeprecatedCause()).isEqualTo("cause");
        assertThat(property.getDeprecatedSince()).isEqualTo("since");
    }

    @Test
    public void should_fill_description_from_getter() throws Exception {
        class TheClass {
            @Description("desc")
            public UUID getField() {
                return null;
            }
        }
        parser.parse(property, null, TheClass.class.getMethod("getField"), null);

        assertThat(property.getDescription()).isEqualTo("desc");
    }

    @Test
    public void should_fill_description_from_setter() throws Exception {
        class TheClass {
            @Description("desc")
            public void setField(UUID field) {
            }
        }
        parser.parse(property, null, null, TheClass.class.getMethod("setField", UUID.class));

        assertThat(property.getDescription()).isEqualTo("desc");
    }

    @Test
    public void should_fill_description_from_property() throws Exception {
        class TheClass {
            @Description("desc")
            private UUID field;
        }
        parser.parse(property, TheClass.class.getDeclaredField("field"), null, null);

        assertThat(property.getDescription()).isEqualTo("desc");
    }
}
