package net.awired.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.lang.reflect.Method;
import java.util.Arrays;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import net.awired.jaxrs.doc.DocConfig;
import net.awired.jaxrs.doc.annotations.Description;
import net.awired.jaxrs.doc.annotations.Summary;
import net.awired.jaxrs.doc.domain.ProjectDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApiParserTest {

    private ProjectDefinition p = new ProjectDefinition();

    @Mock
    private OperationParser operationParser;

    @InjectMocks
    private DocConfig config = new DocConfig(Arrays.asList("net.awired"));

    private ApiParser parser = new ApiParser(config);

    @Path("there")
    @Description("desc")
    @Summary("summary")
    class TestResource {

    }

    @Path("mypath")
    @Description("desc2")
    interface TestInterace {

    }

    class TestInterface implements TestInterace {

    }

    class TestExtends extends TestResource {

    }

    @Test
    public void should_find_informations() throws Exception {
        assertThat(parser.parse(p, TestExtends.class).getResourceClass()).isEqualTo((Class) TestExtends.class);
        assertThat(parser.parse(p, TestExtends.class).getDescription()).isEqualTo("desc");
        assertThat(parser.parse(p, TestExtends.class).getSummary()).isEqualTo("summary");
    }

    @Test
    public void should_find_path() throws Exception {
        assertThat(parser.parse(p, TestResource.class).getPath()).isEqualTo("there");
        assertThat(parser.parse(p, TestInterface.class).getPath()).isEqualTo("mypath");
        assertThat(parser.parse(p, TestExtends.class).getPath()).isEqualTo("there");
    }

    @Test
    public void should_find_description() throws Exception {
        assertThat(parser.parse(p, TestResource.class).getDescription()).isEqualTo("desc");
        assertThat(parser.parse(p, TestInterface.class).getDescription()).isEqualTo("desc2");
        assertThat(parser.parse(p, TestExtends.class).getDescription()).isEqualTo("desc");
    }

    class TestOperation extends TestResource {

        @GET
        public void operation() {

        }

        public void notOperation() {

        }

    }

    @Test
    public void should_call_operation_parser() throws Exception {
        Method method = TestOperation.class.getMethod("operation");
        when(operationParser.isOperation(method)).thenReturn(true);
        when(operationParser.isOperation(TestOperation.class.getMethod("notOperation"))).thenReturn(false);

        assertThat(parser.parse(p, TestOperation.class)).isNotNull();
        verify(operationParser).parse(p, method);
        verify(operationParser, never()).parse(p, TestOperation.class.getMethod("notOperation"));
    }
}
