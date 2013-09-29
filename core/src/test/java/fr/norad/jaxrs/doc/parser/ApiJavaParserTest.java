package fr.norad.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import org.junit.Test;
import fr.norad.jaxrs.doc.domain.ApiDefinition;

public class ApiJavaParserTest {

    @Test
    public void should_fill_deprecated() throws Exception {
        @Deprecated
        class Test {
        }

        ApiDefinition api = new ApiDefinition();
        new ApiJavaParser().parse(api, Test.class);

        assertThat(api.getDeprecated()).isTrue();
    }

    @Test
    public void should_fill_class_name() throws Exception {
        class Test {
        }

        ApiDefinition api = new ApiDefinition();
        new ApiJavaParser().parse(api, Test.class);

        assertThat((Object) api.getApiClass()).isEqualTo(Test.class);
    }
}
