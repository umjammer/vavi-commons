/*
 * Copyright (c) 2010 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties.annotation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import vavi.beans.Binder;

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

    @PropsEntity(url = "file://${user.dir}/src/test/resources/logging.properties")
    public static class Test4 {
        @Property(name = "handlers")
        private String data1;
    }

    @Test
    @DisplayName("system property in url")
    public void test04() throws Exception {
        Test4 bean = new Test4();
        PropsEntity.Util.bind(bean);
        assertEquals("java.util.logging.ConsoleHandler", bean.data1);
    }

    @PropsEntity(url = "file://${user.dir}/src/test/resources/logging.{1}") // {1} this is not proper usage! just for test
    public static class Test5 {
        @Property(name = "{0}") // {0} this is not proper usage! just for test
        private String data1;
    }

    @Test
    @DisplayName("this is not proper usage! just for test")
    public void test05() throws Exception {
        Test5 bean = new Test5();
        PropsEntity.Util.bind(bean, "handlers", "properties");
        assertEquals("java.util.logging.ConsoleHandler", bean.data1);
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

    @PropsEntity(url = "classpath:vavi/util/properties/annotation/propsEntityTest.properties")
    public static class Test10 {
        @Property(binder = vavi.beans.InstanciationBinder.class)
        Binder data4;
    }

    @Test
    @DisplayName("binder")
    public void test10() throws Exception {
        Test10 bean = new Test10();
        PropsEntity.Util.bind(bean);
        assertEquals(vavi.beans.AdvancedBinder.class, bean.data4.getClass());
    }

    @PropsEntity
    public static class Test11 {
        @Env(name = "FOO_BAR_{0}")
        private String data1;
        @Env(name = "HOME")
        private String data2;
    }

    @Test
    @DisplayName("@PropsEntity w/o url")
    public void test11() throws Exception {
        Test11 bean = new Test11();
        PropsEntity.Util.bind(bean, "BUZ");
        assertEquals("hello!!", bean.data1);
        assertEquals(System.getenv("HOME"), bean.data2);
    }

    @BeforeEach
    void before() {
        System.setProperty("test.system.1", "(´・ω・`)");
    }

    @AfterEach
    void after() {
        System.getProperties().remove("test.system.1");
    }

    @PropsEntity
    public static class Test12 {
        @Property(name = "test.system.1")
        private String data1;
    }

    @Test
    @DisplayName("@PropsEntity w/o url -> use system")
    public void test12() throws Exception {
        Test12 bean = new Test12();
        PropsEntity.Util.bind(bean);
        assertEquals("(´・ω・`)", bean.data1);
    }

    @PropsEntity(url = "file:/XYZ", useSystem = true) // error url, use system
    public static class Test13 {
        @Property(name = "test.system.1")
        private String data1;
    }

    @Test
    @DisplayName("@PropsEntity error url -> use system")
    public void test13() throws Exception {
        Test13 bean = new Test13();
        PropsEntity.Util.bind(bean);
        assertEquals("(´・ω・`)", bean.data1);
    }

    @PropsEntity(url = "file:/XYZ") // error url
    public static class Test17 {
        @Property(value = "even though error, here i am", name = "test.system.1")
        private String data1;
        @Property(name = "test.system.2")
        private int data2 = 3;
        @Property(value = "8", name = "test.system.3")
        private int data3 = 3;
    }

    @Test
    @DisplayName("@PropsEntity error url")
    public void test17() throws Exception {
        Test17 bean = new Test17();
        assertThrows(IOException.class, () -> PropsEntity.Util.bind(bean));
        assertEquals("even though error, here i am", bean.data1);
        assertEquals(3, bean.data2);
        assertEquals(8, bean.data3);
    }

    @PropsEntity(url = "classpath:root.properties")
    public static class Test14 {
        @Property(name = "test.system.1", useSystem = true)
        private String data1;
        @Property(name = "test.system.1")
        private String data2;
    }

    @Test
    @DisplayName("@Property use system")
    public void test14() throws Exception {
        Test14 bean = new Test14();
        PropsEntity.Util.bind(bean);
        assertEquals("(´・ω・`)", bean.data1); // overridden by system
        assertEquals("( ･`ω･´)", bean.data2);
    }

    @PropsEntity(url = "classpath:root.properties")
    public static class Test15 {
        @Property(name = "test.system.2", useSystem = true) // test.system.2 is 2
        private int data1 = 100;
        @Property(name = "test.system.3", useSystem = true) // test.system.3 is none
        private int data2 = 100;
    }

    @Test
    @DisplayName("@Property use system but none")
    public void test15() throws Exception {
        Test15 bean = new Test15();
        PropsEntity.Util.bind(bean);
        assertEquals(2, bean.data1);
        assertEquals(100, bean.data2); // use defined
    }

    @PropsEntity
    public static class Test16 {
        @Env(name = "FOO_BAR_BUZ2") // not set
        private String data1 = "bye!!";
    }

    @Test
    @DisplayName("@Env none")
    public void test16() throws Exception {
        Test16 bean = new Test16();
        PropsEntity.Util.bind(bean);
        assertEquals("bye!!", bean.data1); // use defined
    }
}

/* */
