package vavi.beans;

import org.junit.jupiter.api.Test;
import vavi.util.Debug;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class BeanUtilTest {

    @Test
    void test1() throws Exception {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            assertDoesNotThrow(() -> {
                BeanUtil.getMethodByNameOf(BeanUtil.class, "getMethodByNameOf", Class.class, String.class, Class[].class);
            });
        }
Debug.printf("%d ms", (System.currentTimeMillis() - t));
    }
}