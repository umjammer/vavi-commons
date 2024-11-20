/*
 * https://stackoverflow.com/questions/23093470/java-order-of-initialization-and-instantiation/23094875
 */

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


/**
 * Test steps of instance creation.
 *
 * @author eric
 * @version Jan 7, 2018 3:31:12 AM
 */
@Disabled
class InstanceCreateStepTest {

    static List<String> log = new ArrayList<>();

    static class Base {
        static {
            log.add("%s - %s - %s".formatted("base", "static", "block"));
        }
        static {
            log.add("%s - %s - %s".formatted("base", "instance", "block"));
        }

        public Base() {
            log.add("%s - %s".formatted("base", "constructor"));
        }

        public void init() {
            log.add("%s - %s".formatted("base", "PostConstruct"));
        }

        public void hello() {
            log.add("%s - %s".formatted("base", "method"));
        }
    }

    static class Sub extends Base {
        static {
            log.add("%s - %s - %s".formatted("sub", "static", "block"));
        }
        static {
            log.add("%s - %s - %s".formatted("sub", "instance", "block"));
        }

        public Sub() {
            log.add("%s - %s".formatted("sub", "constructor"));
        }

        @Override
        public void init() {
            log.add("%s - %s".formatted("sub", "PostConstruct"));
        }

        @Override
        public void hello() {
            // super.hello());
            log.add("%s - %s".formatted("sub", "method"));
        }
    }

    @Test
    void test() {
        new Sub().hello();
        log.add("%s".formatted("------------"));
        new Sub().hello();

        String[] exepected = {
            "base - static - block",
            "sub - static - block",
            "base - instance - block",
            "base - constructor",
            "sub - instance - block",
            "sub - constructor",
            "sub - method",
            "------------",
            "base - instance - block",
            "base - constructor",
            "sub - instance - block",
            "sub - constructor",
            "sub - method"
        };
        assertArrayEquals(exepected, log.toArray(new String[0]));
    }
}
