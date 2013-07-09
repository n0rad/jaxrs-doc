package net.awired.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;
import net.awired.jaxrs.doc.DocConfig;
import org.junit.Test;

public class ModelParserTest {

    private DocConfig config = new DocConfig(Arrays.asList("net.awired"));

    @Test
    public void should_ignore_basic_class() throws Exception {
        ModelParser modelParser = new ModelParser(config);

        assertThat(modelParser.isIgnoreModel(String.class)).isTrue();
        assertThat(modelParser.isIgnoreModel(int.class)).isTrue();
        assertThat(modelParser.isIgnoreModel(void.class)).isTrue();
        assertThat(modelParser.isIgnoreModel(UUID.class)).isTrue();
        assertThat(modelParser.isIgnoreModel(InputStream.class)).isTrue();
    }

}
