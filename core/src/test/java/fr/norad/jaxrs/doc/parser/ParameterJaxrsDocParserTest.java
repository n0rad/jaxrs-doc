package fr.norad.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import java.lang.reflect.Method;
import javax.ws.rs.GET;
import org.junit.Test;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;

public class ParameterJaxrsDocParserTest {

    private ParameterDefinition parameter = new ParameterDefinition();

    @Test
    public void should_find_deprecated_with_outdate() throws Exception {
        class Test {
            @GET
            public void getSomething(@Outdated(cause = "cause", since = "since") String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        new ParameterJaxrsDocParser().parse(parameter, method, 0);

        assertThat(parameter.getDeprecated()).isTrue();
        assertThat(parameter.getDeprecatedSince()).isEqualTo("since");
        assertThat(parameter.getDeprecatedCause()).isEqualTo("cause");
    }

    @Test
    public void should_find_description() throws Exception {
        class Test {
            @GET
            public void getSomething(@Description("yop") String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        new ParameterJaxrsDocParser().parse(parameter, method, 0);

        assertThat(parameter.getDescription()).isEqualTo("yop");
    }

}
