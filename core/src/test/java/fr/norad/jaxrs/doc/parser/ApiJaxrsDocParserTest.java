package fr.norad.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.junit.Test;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.domain.ApiDefinition;

public class ApiJaxrsDocParserTest {

    @Test
    public void should_support_outdated() throws Exception {
        @Path("/")
        @Outdated(cause = "sux", since = "since")
        class Test {
            @GET
            public String getSomething() {
                return null;
            }
        }

        ApiDefinition api = new ApiDefinition();
        new ApiJaxrsDocParser().parse(api, Test.class);

        assertThat(api.getDeprecated()).isTrue();
        assertThat(api.getDeprecatedCause()).isEqualTo("sux");
        assertThat(api.getDeprecatedSince()).isEqualTo("since");
    }
}
