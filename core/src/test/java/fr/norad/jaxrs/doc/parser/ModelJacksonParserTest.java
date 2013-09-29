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

import static org.fest.assertions.api.Assertions.assertThat;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.norad.jaxrs.doc.JaxRsDocProcessorFactory;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.domain.ModelDefinition;
import fr.norad.jaxrs.doc.domain.ProjectDefinition;
import fr.norad.jaxrs.doc.parser.ModelJacksonParser;

@RunWith(MockitoJUnitRunner.class)
public class ModelJacksonParserTest {

    private ProjectDefinition p = new ProjectDefinition();

    @InjectMocks
    private JaxRsDocProcessorFactory config = new JaxRsDocProcessorFactory(Arrays.asList("fr.norad"));

    public class SimpleBean {
        @Description("publicDesc!")
        public String publicProp;

        @Description("desc!")
        @JsonProperty("simple-string")
        private String modifiedProp;

        @Description("desc")
        private SimpleBean complex;

        public SimpleBean getComplex() {
            return complex;
        }

        public void setComplex(SimpleBean complex) {
            this.complex = complex;
        }
    }

    @Test
    public void should_find_description() throws Exception {
        ModelJacksonParser modelJacksonParser = new ModelJacksonParser(config);
        modelJacksonParser.parse(p, SimpleBean.class);

        ModelDefinition modelDefinition = p.getModels().get(SimpleBean.class.getName());

        assertThat(modelDefinition.getProperties()).hasSize(3);
        assertThat(modelDefinition.getProperties().get("simple-string").getDescription()).isEqualTo("desc!");
        assertThat(modelDefinition.getProperties().get("publicProp").getDescription()).isEqualTo("publicDesc!");
        assertThat(modelDefinition.getProperties().get("complex").getDescription()).isEqualTo("desc");
    }

}
