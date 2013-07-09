package net.awired.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.UUID;
import net.awired.jaxrs.doc.DocConfig;
import net.awired.jaxrs.doc.annotations.Description;
import net.awired.jaxrs.doc.domain.ProjectDefinition;
import net.awired.jaxrs.doc.domain.PropertyDefinition;
import org.junit.Test;

public class PropertyParserTest {

    private DocConfig config = new DocConfig(Arrays.asList("net.awired"));

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

}
