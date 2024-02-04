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
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vavi.beans.Binder;
import vavi.net.www.protocol.URLStreamHandlerUtil;


/**
 * PropsEntity.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2010/10/08 nsano initial version <br>
 */
@java.lang.annotation.Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropsEntity {

    /**
     * <code>${Foo}</code> is replaced with <code>System.getProperty("Foo")</code> or <code>System.getenv("Foo")</code>.<br/>
     * <code>{#}</code> (# is 0, 1, 2...) will be replaced by parameters (String...) of {@link PropsEntity.Util#bind(Object, String...)}<br/>
     * <p>
     * ex.
     * <pre>
     *  "file://${user.home}/.foo/{0}.properties"
     *  "classpath:your/package/foo.properties"
     * </pre>
     * </p>
     * when this is not set, system properties will be used.
     * @see vavi.net.www.protocol.classpath.Handler
     */
    String url() default "";

    /**
     * if true, when the url has error, system properties are used as backup. else an exception will be thrown.
     */
    boolean useSystem() default false;

    /** */
    final class Util {

        private static Logger logger = Logger.getLogger(Util.class.getName());

        private Util() {
        }

        /** */
        private static String getUrl(Object bean) {
            PropsEntity propsEntity = bean.getClass().getAnnotation(PropsEntity.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @PropsEntity");
            }

            return propsEntity.url();
        }

        /** */
        private static boolean useSystem(Object bean) {
            PropsEntity propsEntity = bean.getClass().getAnnotation(PropsEntity.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @PropsEntity");
            }

            return propsEntity.useSystem();
        }

        /**
         * @return {@link Property} annotated fields
         */
        private static Set<Field> getPropertyFields(Object bean) {
            //
            PropsEntity propsEntity = bean.getClass().getAnnotation(PropsEntity.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @PropsEntity");
            }

            //
            Set<Field> propertyFields = new HashSet<>();

            Class<?> clazz = bean.getClass();
            while (clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    Property property = field.getAnnotation(Property.class);
                    if (property != null) {
                        propertyFields.add(field);
                    }
                }
                clazz = clazz.getSuperclass();
            }

            return propertyFields;
        }

        /**
         * @return {@link Env} annotated fields
         */
        private static Set<Field> getEnvFields(Object bean) {
            //
            PropsEntity propsEntity = bean.getClass().getAnnotation(PropsEntity.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @PropsEntity");
            }

            //
            Set<Field> envFields = new HashSet<>();

            Class<?> clazz = bean.getClass();
            while (clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    Env env = field.getAnnotation(Env.class);
                    if (env != null) {
                        envFields.add(field);
                    }
                }
                clazz = clazz.getSuperclass();
            }

            return envFields;
        }

        /** replacing key pattern */
        private static final Pattern pattern = Pattern.compile("\\$\\{[\\w\\.]+\\}");

        /**
         * Replaces <code>${Foo}</code> with <code>System.getProperty("Foo")</code> or <code>System.getenv("Foo")</code>.
         */
        private static String replaceWithEnvOrProps(String url) {
logger.finest("url: origin: " + url);
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
else {
 logger.finest("url: replaced with props: " + value);
}
               }
else {
 logger.finest("url: replaced with env: " + value);
}
               url = url.replace(key, value);
logger.finest("url: replace: " + key + ": " + url);
            }
            return url;
        }

        /**
         * <code>{#}</code> (# is 0, 1, 2...) is replaced by parameters args.
         */
        private static String replaceWithArgs(String name, String... args) {
            for (int i = 0; i < args.length; i++) {
                String key = "{" + i + "}";
                name = name.replace(key, args[i]);
logger.finest("replace: " + name + ", " + key + ", " + args[i]);
            }
            return name;
        }

        /* for "classpath" schema */
        static {
try {
            URLStreamHandlerUtil.loadService();
} catch (Throwable t) {
 t.printStackTrace();
}
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
         * @throws IOException when {@link PropsEntity#url()} is wrong, but after all default value will be set.
         */
        public static void bind(Object bean, String... args) throws IOException {
            // TODO check super classes
            PropsEntity propsEntity = bean.getClass().getAnnotation(PropsEntity.class);
            if (propsEntity == null) {
                throw new IllegalArgumentException("bean is not annotated with @PropsEntity");
            }

            boolean isSystem = false;
            IOException exception = null;

            Properties props;
            String baseUrl = getUrl(bean);
            if (!baseUrl.isEmpty()) {
                props = new Properties();
                String url = replaceWithArgs(replaceWithEnvOrProps(baseUrl), args);
logger.finest("url: finally: " + url);
                try {
                    props.load(new URL(url).openStream());
                } catch (IOException e) {
                    if (useSystem(bean)) {
logger.finer("url: useSystem is enabled");
                        props = System.getProperties();
                        isSystem = true;
                    } else {
                        exception = e;
                    }
                }
            } else {
logger.finest("url: use system properties");
                props = System.getProperties();
                isSystem = true;
            }

            // 1. property
            for (Field field : getPropertyFields(bean)) {
                String name = Property.Util.getName(field);
                String defaultValue = Property.Util.getValue(field);
logger.finest("before: " + name);
                name = replaceWithArgs(name, args);
logger.finest("after: " + name);
                String value = null;
                if (exception != null) {
                    if (!defaultValue.isEmpty()) {
logger.finer("bad url but has default");
                        value = defaultValue;
                    }
                } else {
                    if (!props.getProperty(name, "").isEmpty()) {
                        value = props.getProperty(name);
                    } else if (!defaultValue.isEmpty()) {
                        value = defaultValue;
                    }
                }
                if (!isSystem && Property.Util.useSystem(field) && !System.getProperty(name, "").isEmpty()) {
logger.finer("overridden by system properties");
                    value = System.getProperty(name);
                }
logger.finest("value: " + name + ", " + value);
                if (value != null) {
                    Binder binder = Property.Util.getBinder(field);
                    binder.bind(bean, field, field.getType(), value, value); // TODO elseValue is used for type String
                }
            }

            // 2. env
            for (Field field : getEnvFields(bean)) {
                String name = Env.Util.getName(field);
                String defaultValue = Env.Util.getValue(field);
logger.finest("before: " + name);
                name = replaceWithArgs(name, args);
logger.finest("after: " + name);
                String value = null;
                value = System.getenv(name);
                if (!defaultValue.isEmpty() && (value == null || value.isEmpty())) {
                    value = defaultValue;
                }
logger.finest("env: " + name + ", " + value);
                if (value != null) {
                    Binder binder = Env.Util.getBinder(field);
                    binder.bind(bean, field, field.getType(), value, value); // TODO elseValue is used for type String
                }
            }

            //
            if (exception != null) {
                throw exception;
            }
        }
    }
}

/* */
