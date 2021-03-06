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
package fr.norad.jaxrs.doc.parser;

import static java.util.Arrays.asList;
import static org.fest.assertions.api.Assertions.assertThat;
import java.util.List;
import org.junit.Test;
import fr.norad.jaxrs.doc.api.domain.ProjectDefinition;

public class ProjectDiscoveryParserTest {

    @Test
    public void should_fill_name_and_version() throws Exception {
        ProjectDefinition project = new ProjectDefinition();
        ProjectDiscoveryParser parser = new ProjectDiscoveryParser(asList("fr.norad.jaxrs.doc"), "name", "version");

        parser.parse(project);

        assertThat(project.getName()).isEqualTo("name");
        assertThat(project.getVersion()).isEqualTo("version");
    }

    @Test
    public void should_discover_api() throws Exception {
        ProjectDiscoveryParser parser = new ProjectDiscoveryParser(asList("fr.norad.jaxrs.doc"), "name", "version");

        List<Class<?>> apiClasses = parser.apiClasses();

        assertThat(apiClasses).isNotEmpty();
    }

}
