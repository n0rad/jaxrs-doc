/**
 *
 *     Copyright (C) norad.fr
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package fr.norad.jaxrs.doc.processor;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import fr.norad.jaxrs.doc.JaxrsDocProcessorFactory;
import fr.norad.jaxrs.doc.ParserHolder;
import fr.norad.jaxrs.doc.api.domain.ApiDefinition;
import fr.norad.jaxrs.doc.api.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parserapi.ProjectParser;

public class ProjectProcessorTest {
    ProjectParser parser = mock(ProjectParser.class);
    ApiProcessor apiProcessor = mock(ApiProcessor.class);
    ApiDefinition api = mock(ApiDefinition.class);
    JaxrsDocProcessorFactory factory = mock(JaxrsDocProcessorFactory.class);
    ProjectProcessor processor = new ProjectProcessor(factory, new ParserHolder<ProjectParser>() {
        {
            add(parser);
        }
    });

    @Test
    public void should_call_parser_find_classes_and_process_api() throws Exception {
        List<Class<?>> classes = Arrays.<Class<?>>asList(String.class);
        when(parser.apiClasses()).thenReturn(classes);
        when(factory.getApiProcessor()).thenReturn(apiProcessor);
        when(apiProcessor.process(any(ProjectDefinition.class), eq(String.class))).thenReturn(api);

        ProjectDefinition project = processor.process();

        verify(parser).parse(any(ProjectDefinition.class));
        assertThat(project.getApis()).containsExactly(api);
    }
}
