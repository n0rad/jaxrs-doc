package fr.norad.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import org.junit.Test;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.domain.ModelDefinition;

public class ModelJaxrsDocParserTest {

    private ModelDefinition model = new ModelDefinition();
    private ModelJaxrsDocParser parser = new ModelJaxrsDocParser();

    @Test
    public void should_fill_oudated_part() throws Exception {
        @Outdated(since = "since", cause = "cause")
        class Test {

        }
        parser.parse(model, Test.class);

        assertThat(model.getDeprecated()).isTrue();
        assertThat(model.getDeprecatedCause()).isEqualTo("cause");
        assertThat(model.getDeprecatedSince()).isEqualTo("since");
    }

    @Test
    public void should_fill_description_part() throws Exception {
        @Description("yopla")
        class Test {

        }
        parser.parse(model, Test.class);

        assertThat(model.getDescription()).isEqualTo("yopla");
    }

}
