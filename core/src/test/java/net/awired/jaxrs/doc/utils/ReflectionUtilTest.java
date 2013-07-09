package net.awired.jaxrs.doc.utils;

import static org.fest.assertions.api.Assertions.assertThat;
import java.util.List;
import java.util.UUID;
import org.junit.Test;

public class ReflectionUtilTest {

    @Test
    public void should_find_generic_return_type() throws Exception {
        class Test {
            public List<UUID> getSomething() {
                return null;
            }
        }
        Class<?> type = ReflectionUtil.getSingleGenericReturnType(Test.class.getMethod("getSomething"));

        assertThat((Object) type).isEqualTo(UUID.class);
    }

    @Test
    public void should_not_find_generic_return_type() throws Exception {
        class Test {
            public String getSomething() {
                return null;
            }
        }
        Class<?> type = ReflectionUtil.getSingleGenericReturnType(Test.class.getMethod("getSomething"));

        assertThat((Object) type).isNull();
    }
}
