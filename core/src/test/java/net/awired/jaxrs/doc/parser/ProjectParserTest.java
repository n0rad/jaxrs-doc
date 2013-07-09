package net.awired.jaxrs.doc.parser;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import java.util.Arrays;
import net.awired.jaxrs.doc.DocConfig;
import net.awired.jaxrs.doc.domain.ProjectDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProjectParserTest {

    @Mock
    private ApiParser apiParser;

    @InjectMocks
    private DocConfig config = new DocConfig(Arrays.asList("net.awired"));

    @Test
    public void should_find_some_api() throws Exception {
        ProjectDefinition project = new ProjectParser(config).parse();
        verify(apiParser, atLeastOnce()).parse(eq(project), any(Class.class));
    }

}
