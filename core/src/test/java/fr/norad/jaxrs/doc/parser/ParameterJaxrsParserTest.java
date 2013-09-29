package fr.norad.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import java.lang.reflect.Method;
import javax.ws.rs.BeanParam;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Encoded;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import org.junit.Test;
import fr.norad.jaxrs.doc.domain.ParameterDefinition;
import fr.norad.jaxrs.doc.domain.ParameterType;

@SuppressWarnings("unused")
public class ParameterJaxrsParserTest {

    private ParameterDefinition parameter = new ParameterDefinition();
    private ParameterJaxrsParser parser = new ParameterJaxrsParser();

    @Test
    public void should_fill_encoded() throws Exception {
        class Test {
            @GET
            public void getSomething(@Encoded String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(parameter, method, 0);

        assertThat(parameter.getEncoded()).isTrue();
    }

    @Test
    public void should_fill_default_value() throws Exception {
        class Test {
            @GET
            public void getSomething(@DefaultValue("yop") String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(parameter, method, 0);

        assertThat(parameter.getDefaultValue()).isEqualTo("yop");
    }

    @Test
    public void should_fill_path_type() throws Exception {
        class Test {
            @GET
            public void getSomething(@PathParam("param") String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(parameter, method, 0);

        assertThat(parameter.getParamType()).isEqualTo(ParameterType.PATH);
        assertThat(parameter.getParamName()).isEqualTo("param");
    }

    @Test
    public void should_fill_context_type() throws Exception {
        class Test {
            @GET
            public void getSomething(@Context String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(parameter, method, 0);

        assertThat(parameter.getParamType()).isEqualTo(ParameterType.CONTEXT);
    }

    @Test
    public void should_fill_bean_type() throws Exception {
        class Test {
            @GET
            public void getSomething(@BeanParam String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(parameter, method, 0);

        assertThat(parameter.getParamType()).isEqualTo(ParameterType.BEAN);
    }

    @Test
    public void should_fill_query_type() throws Exception {
        class Test {
            @GET
            public void getSomething(@QueryParam("yop") String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(parameter, method, 0);

        assertThat(parameter.getParamType()).isEqualTo(ParameterType.QUERY);
        assertThat(parameter.getParamName()).isEqualTo("yop");
    }

    @Test
    public void should_fill_matrix_type() throws Exception {
        class Test {
            @GET
            public void getSomething(@MatrixParam("yop") String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(parameter, method, 0);

        assertThat(parameter.getParamType()).isEqualTo(ParameterType.MATRIX);
        assertThat(parameter.getParamName()).isEqualTo("yop");
    }

    @Test
    public void should_fill_form_type() throws Exception {
        class Test {
            @GET
            public void getSomething(@FormParam("yop") String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(parameter, method, 0);

        assertThat(parameter.getParamType()).isEqualTo(ParameterType.FORM);
        assertThat(parameter.getParamName()).isEqualTo("yop");
    }

    @Test
    public void should_fill_cookie_type() throws Exception {
        class Test {
            @GET
            public void getSomething(@CookieParam("yop") String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(parameter, method, 0);

        assertThat(parameter.getParamType()).isEqualTo(ParameterType.COOKIE);
        assertThat(parameter.getParamName()).isEqualTo("yop");
    }

    @Test
    public void should_fill_content_type() throws Exception {
        class Test {
            @GET
            public void getSomething(String param) {
            }
        }
        Method method = Test.class.getMethod("getSomething", String.class);

        parser.parse(parameter, method, 0);

        assertThat(parameter.getParamType()).isEqualTo(ParameterType.REQUEST_BODY);
    }

}
