import javax.annotation.PostConstruct;


/**
 * Test steps of instance creation.
 *
 * @author eric
 * @date Jan 7, 2018 3:31:12 AM
 */
public class InstanceCreateStepTest {
    public static void main(String[] args) {
        new Sub().hello();
        System.out.printf("%s\n", "------------");
        new Sub().hello();
    }
}

class Base {
    static {
        System.out.printf("%s - %s - %s\n", "base", "static", "block");
    }
    {
        System.out.printf("%s - %s - %s\n", "base", "instance", "block");
    }

    public Base() {
        System.out.printf("%s - %s\n", "base", "constructor");
    }

    @PostConstruct
    public void init() {
        System.out.printf("%s - %s\n", "base", "PostConstruct");
    }

    public void hello() {
        System.out.printf("%s - %s\n", "base", "method");
    }
}

class Sub extends Base {
    static {
        System.out.printf("%s - %s - %s\n", "sub", "static", "block");
    }
    {
        System.out.printf("%s - %s - %s\n", "sub", "instance", "block");
    }

    public Sub() {
        System.out.printf("%s - %s\n", "sub", "constructor");
    }

    @PostConstruct
    public void init() {
        System.out.printf("%s - %s\n", "sub", "PostConstruct");
    }

    @Override
    public void hello() {
        // super.hello();
        System.out.printf("%s - %s\n", "sub", "method");
    }
}
