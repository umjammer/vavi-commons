/*
 * Copyright (c) 2010 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties.annotation;

import java.io.File;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * PropsEntityTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2010/10/08 nsano initial version <br>
 */
@PropsEntity(url = "classpath:vavi/util/properties/annotation/propsEntityTest.properties")
public class PropsEntityTest {

    @Property(name = "data1")
    private String data1;

    @Property(name = "data2")
    private int data2;

    @Property
    private String data3;

    @Property(value = "100")
    private int data6;

    public String toString() {
        return "data1: " + data1 +
               ", data2: " + data2 +
               ", data3: " + data3;
    }

    public static void main(String[] args) throws Exception {
        PropsEntityTest bean = new PropsEntityTest();
        PropsEntity.Util.bind(bean);
        System.err.println(bean);
    }

    @Test
    @DisplayName("normal usage")
    public void test01() throws Exception {
        PropsEntityTest bean = new PropsEntityTest();
        PropsEntity.Util.bind(bean);
        assertEquals("Sano Naohide", bean.data1);
        assertEquals(40, bean.data2);
        assertEquals("Umjammer", bean.data3);
    }

    @PropsEntity(url = "file://${HOME}/.vavi-commons")
    public static class Test2 {
        @Property(name = "test.data")
        private String data;
    }

    @Test
    @DisplayName("system env in url")
    public void test02() throws Exception {
        File file = new File(System.getenv("HOME"), ".vavi-commons");
        FileWriter fw = new FileWriter(file);
        fw.write("test.data=Hello\n");
        fw.close();
        Test2 bean = new Test2();
        PropsEntity.Util.bind(bean);
        assertEquals("Hello", bean.data);
        fw.close();
        file.delete();
    }

    @Test
    @DisplayName("just regex test")
    public void test03() {
        Pattern pattern = Pattern.compile("\\$\\{\\w+\\}");
        Matcher matcher = pattern.matcher("file://${HOME}/.vavi-commons");
        if (matcher.find()) {
            assertEquals("${HOME}", matcher.group());
        } else {
            fail("no match");
        }
    }

    @PropsEntity(url = "file://${user.dir}/local.properties.sample")
    public static class Test4 {
        @Property(name = "javac.source")
        private String data1;
    }

    @Test
    @DisplayName("system property in url")
    public void test04() throws Exception {
        Test4 bean = new Test4();
        PropsEntity.Util.bind(bean);
        assertEquals("src/main/java", bean.data1);
    }

    @PropsEntity(url = "file://${user.dir}/local.properties.{1}") // {1} this is not proper usage! just for test
    public static class Test5 {
        @Property(name = "{0}") // {0} this is not proper usage! just for test
        private String data1;
    }

    @Test
    @DisplayName("this is not proper usage! just for test")
    public void test05() throws Exception {
        Test5 bean = new Test5();
        PropsEntity.Util.bind(bean, "javac.source", "sample");
        assertEquals("src/main/java", bean.data1);
    }

    @Test
    @DisplayName("when no key, use default")
    public void test06() throws Exception {
        PropsEntityTest bean = new PropsEntityTest();
        PropsEntity.Util.bind(bean);
        assertEquals(100, bean.data6);
    }

    @PropsEntity(url = "classpath:/root.properties") // slash at start
    public static class Test7 {
        @Property(name = "root.key1")
        private String data1;
        @Property(name = "root.key2")
        private int data2;
        @Property(name = "root.key3")
        private String data3;
    }

    @Test
    @DisplayName("check root of classpath scheme")
    public void test07() throws Exception {
        assertThrows(Exception.class, () -> {
            Test7 bean = new Test7();
            PropsEntity.Util.bind(bean);
        });
    }

    @PropsEntity(url = "classpath:root.properties") // just dummy
    public static class Test8 {
        @Env(name = "HOME")
        private String data1;
        @Env(name = "NOT_EXISTS_ENV_XXX_YYY_ZZZ", value = "none")
        private String data2;
        @Env(name = "NOT_EXISTS_ENV_AAA_BBB_CCC")
        private String data3;
    }

    @Test
    @DisplayName("env")
    public void test08() throws Exception {
        Test8 bean = new Test8();
        PropsEntity.Util.bind(bean);
        assertEquals(System.getenv("HOME"), bean.data1);
        assertEquals("none", bean.data2);
        assertNull(bean.data3);
    }

    public static class Test9_0 {
        @Property
        String data1;
    }

    public static class Test9_1 extends Test9_0 {
        @Property
        int data2;
        @Env(name = "HOME")
        String home;
    }

    @PropsEntity(url = "classpath:vavi/util/properties/annotation/propsEntityTest.properties")
    public static class Test9 extends Test9_1 {
        @Property
        String data3;
    }

    @Test
    @DisplayName("super class fields")
    public void test09() throws Exception {
        Test9 bean = new Test9();
        PropsEntity.Util.bind(bean);
        assertEquals("Sano Naohide", bean.data1);
        assertEquals(40, bean.data2);
        assertEquals("Umjammer", bean.data3);
        assertEquals(System.getenv("HOME"), bean.home);
    }
}

/* */
