package net.awired.jaxrs.doc.utils;

import static org.fest.assertions.api.Assertions.assertThat;
import java.util.UUID;
import javax.ws.rs.DefaultValue;
import org.junit.Test;

public class AnnotationUtilTest {

    interface ParameterInterface {
        public UUID getUUID(@DefaultValue("defValue") UUID uid);
    }

    interface ParameterInterface2 extends ParameterInterface {
    }

    class ParamaterAbstract implements ParameterInterface2 {
        @Override
        public UUID getUUID(UUID uid) {
            return null;
        }
    }

    class ParameterClass extends ParamaterAbstract {
        @Override
        public UUID getUUID(UUID uid) {
            return super.getUUID(uid);
        }
    }

    @Test
    public void should_find_annotation() throws Exception {
        DefaultValue annotation = AnnotationUtil.findParameterAnnotation(
                ParameterClass.class.getMethod("getUUID", UUID.class), 0, DefaultValue.class);

        assertThat(annotation.value()).isEqualTo("defValue");
    }

}
