/*
 * https://stackoverflow.com/questions/23093470/java-order-of-initialization-and-instantiation/23094875
 */

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

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
            log.add(String.format("%s - %s - %s", "base", "static", "block"));
        }
        static {
            log.add(String.format("%s - %s - %s", "base", "instance", "block"));
        }

        public Base() {
            log.add(String.format("%s - %s", "base", "constructor"));
        }

        @PostConstruct
        public void init() {
            log.add(String.format("%s - %s", "base", "PostConstruct"));
        }

        public void hello() {
            log.add(String.format("%s - %s", "base", "method"));
        }
    }

    static class Sub extends Base {
        static {
            log.add(String.format("%s - %s - %s", "sub", "static", "block"));
        }
        static {
            log.add(String.format("%s - %s - %s", "sub", "instance", "block"));
        }

        public Sub() {
            log.add(String.format("%s - %s", "sub", "constructor"));
        }

        @PostConstruct
        public void init() {
            log.add(String.format("%s - %s", "sub", "PostConstruct"));
        }

        @Override
        public void hello() {
            // super.hello());
            log.add(String.format("%s - %s", "sub", "method"));
        }
    }

    @Test
    void test() {
        new Sub().hello();
        log.add(String.format("%s", "------------"));
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
