package net.awired.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import net.awired.jaxrs.doc.DocConfig;
import net.awired.jaxrs.doc.annotations.Description;
import net.awired.jaxrs.doc.domain.ParameterDefinition;
import net.awired.jaxrs.doc.domain.ProjectDefinition;
import net.awired.jaxrs.doc.domain.sub.ParameterType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParameterParserTest {

    private ProjectDefinition p = new ProjectDefinition();

    @Mock
    private ModelParser modelParser;

    @InjectMocks
    private DocConfig config = new DocConfig(Arrays.asList("net.awired"));

    private ParameterParser parser = new ParameterParser(config);

    interface ParameterInterface {
        public UUID getUUID(@Description("theID") UUID uid);
    }

    class ParamaterAbstract implements ParameterInterface {
        @Override
        public UUID getUUID(@PathParam("pathId") UUID uid) {
            return null;
        }
    }

    class ParameterClass extends ParamaterAbstract {
        @Override
        public UUID getUUID(@DefaultValue("defValue") UUID uid) {
            return super.getUUID(uid);
        }
    }

    @Test
    public void should_find_info() throws Exception {
        ParameterDefinition param = parser.parse(p, ParameterClass.class.getMethod("getUUID", UUID.class), 0);

        assertThat((Object) param.getParamClass()).isEqualTo(UUID.class);
        assertThat(param.getParamType()).isEqualTo(ParameterType.PATH);
        assertThat(param.getDefaultValue()).isEqualTo("defValue");
        assertThat(param.getParamName()).isEqualTo("pathId");
        assertThat(param.getDescription()).isEqualTo("theID");
    }

    @Test
    public void should_call_model_parser() throws Exception {
        ParameterDefinition param = parser.parse(p, ParameterClass.class.getMethod("getUUID", UUID.class), 0);

        verify(modelParser).parse(p, UUID.class);
    }

    @Test
    public void should_support_array_param_type() throws Exception {
        class Test {
            @GET
            public void getSomething(String[] param) {
            }
        }

        Method method = Test.class.getMethod("getSomething", String[].class);
        ParameterDefinition param = parser.parse(p, method, 0);

        assertThat((Object) param.getParamClass()).isEqualTo(String.class);
        assertThat(param.getParamAsList()).isTrue();
    }

    @Test
    public void should_support_list_param_type() throws Exception {
        class Test {
            @GET
            public void getSomething(List<String> param) {
            }
        }

        Method method = Test.class.getMethod("getSomething", List.class);
        ParameterDefinition param = parser.parse(p, method, 0);

        assertThat((Object) param.getParamClass()).isEqualTo(String.class);
        assertThat(param.getParamAsList()).isTrue();
    }

    @Test
    public void should_support_standard_param_type() throws Exception {
        class Test {
            @GET
            public void getSomething(String param) {
            }
        }

        Method method = Test.class.getMethod("getSomething", String.class);
        ParameterDefinition param = parser.parse(p, method, 0);

        assertThat((Object) param.getParamClass()).isEqualTo(String.class);
        assertThat(param.getParamAsList()).isNull();
    }
}
