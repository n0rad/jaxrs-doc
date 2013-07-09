package net.awired.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import java.util.Arrays;
import net.awired.jaxrs.doc.DocConfig;
import net.awired.jaxrs.doc.annotations.Description;
import net.awired.jaxrs.doc.domain.ModelDefinition;
import net.awired.jaxrs.doc.domain.ProjectDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import com.fasterxml.jackson.annotation.JsonProperty;

@RunWith(MockitoJUnitRunner.class)
public class ModelJacksonParserTest {

    private ProjectDefinition p = new ProjectDefinition();

    @InjectMocks
    private DocConfig config = new DocConfig(Arrays.asList("net.awired"));

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

        ModelDefinition modelDefinition = p.getModels().get(SimpleBean.class);

        assertThat(modelDefinition.getProperties()).hasSize(3);
        assertThat(modelDefinition.getProperties().get("simple-string").getDescription()).isEqualTo("desc!");
        assertThat(modelDefinition.getProperties().get("publicProp").getDescription()).isEqualTo("publicDesc!");
        assertThat(modelDefinition.getProperties().get("complex").getDescription()).isEqualTo("desc");
    }

}
