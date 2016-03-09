/*
 * Copyright (c) 2010 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties.annotation;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vavi.beans.DefaultBinder;
import vavi.net.www.protocol.URLStreamHandlerUtil;


/**
 * PropsEntity. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2010/10/08 nsano initial version <br>
 */
@java.lang.annotation.Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropsEntity {

    /** 
     * <code>${Foo}</code> is replaced with <code>System.getProperty("Foo")</code> or <code>System.getenv("Foo")</code>.
     * <p>
     * ex.
     * <pre>
     *  "/foo.properties" 
     *  "classpath:your/package/foo.properties" 
     * </pre>
     * </p>
     * @see {@link vavi.net.www.protocol.classpath.Handler}
     */
    String url();

    /** */
    class Util {

        /** */
        public static String getUrl(Object bean) throws IOException {
            PropsEntity propsEntity = bean.getClass().getAnnotation(PropsEntity.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @PropsEntity");
            }

            return propsEntity.url();
        }

        /**
         * @return {@link PropsEntity} annotated fields
         */
        public static Set<Field> getPropertyFields(Object bean) {
            //
            PropsEntity propsEntity = bean.getClass().getAnnotation(PropsEntity.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @PropsEntity");
            }

            //
            Set<Field> propertyFields = new HashSet<>(); 

            for (Field field : bean.getClass().getDeclaredFields()) {
                Property property = field.getAnnotation(Property.class);
                if (property != null) {
                    propertyFields.add(field);
                }
            }

            return propertyFields;
        }

        /** replacing key pattern */
        private static final Pattern pattern = Pattern.compile("\\$\\{[\\w\\.]+\\}"); 

        /**
         * Replaces <code>${Foo}</code> with <code>System.getProperty("Foo")</code> or <code>System.getenv("Foo")</code>.
         */
        private static String replaceWithEnvOrProps(String url) {
//System.err.println("url: origin: " + url);
            Matcher matcher = pattern.matcher(url);
            while (matcher.find()) {
               String key = matcher.group();
               String name = key.substring(2, key.length() - 1);
               String value = System.getenv(name);
               if (value == null) {
                   value = System.getProperty(name);
                   if (value == null) {
                       System.err.println(key + " is not replaceable");
                       continue;
                   }
//else {
// System.err.println("url: replaced with props: " + value);
//}
               }
//else {
// System.err.println("url: replaced with env: " + value);
//}
               url = url.replace(key, value);
//System.err.println("url: replace: " + key + ": " + url);
            }
            return url;
        }

        /* for "classpath" schema */
        static {
            URLStreamHandlerUtil.loadService();
        }

        /**
         * Entry point.
         * 
         * @param args replace <code>"{#}"</code> (# is 0, 1, 2 ...)
         * <pre>
         * $ cat some.properties
         * foo.bar.buz=xxx
         * foo.bar.aaa=yyy
         * 
         * @Property(name = "foo.bar.{0})
         * Foo bar;
         *  
         *    :
         * 
         * PropsEntity.Util.bind(bean, "buz");
         * assertEquals(bean.bar, "xxx");
         *  
         * </pre>
         */
        public static void bind(Object bean, String... args) throws IOException {
            //
            PropsEntity propsEntity = bean.getClass().getAnnotation(PropsEntity.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @PropsEntity");
            }

            Properties props = new Properties();
            String url = replaceWithEnvOrProps(getUrl(bean));
//System.err.println("url: finally: " + url);
            props.load(new URL(url).openStream());

            DefaultBinder binder = new DefaultBinder();

            //
            for (Field field : getPropertyFields(bean)) {
                String name = Property.Util.getName(field, args);
                String value = props.getProperty(name);
System.err.println("value: " + name + ", " + value);
                binder.bind(bean, field, field.getType(), value, value); // TODO elseValue is used for type String
            }
        }
    }
}

/* */
