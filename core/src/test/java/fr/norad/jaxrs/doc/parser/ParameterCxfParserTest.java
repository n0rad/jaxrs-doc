package fr.norad.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import java.lang.reflect.Method;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.junit.Test;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.domain.ParameterType;

public class ParameterCxfParserTest {
    private ParameterDefinition parameter = new ParameterDefinition();
    private ParameterCxfParser parser = new ParameterCxfParser();

    @Test
    public void should_support_form_data_param_as_multipart() throws Exception {
        class Test {
            public void getSomething(@Multipart("caller") String caller) {
            }
        }

        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(null, parameter, method, 0);

        assertThat(parameter.getType()).isEqualTo(ParameterType.MULTIPART);
        assertThat(parameter.getName()).isEqualTo("caller");
    }
}
