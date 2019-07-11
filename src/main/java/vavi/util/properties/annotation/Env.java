/*
 * Copyright (c) 2019 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;


/**
 * Env.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2019/06/20 umjammer initial version <br>
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Env {

    /**
     * Environment variable name.
     * When this is not set, the field name will be used.
     * <code>{#}</code> (# is 0, 1, 2...) will be replaced by parameters (String...) of {@link PropsEntity.Util#bind(Object, String...)}
     */
    String name() default "";

    /**
     * default value for this property.
     */
    String value() default "";

    /**
     * TODO アノテーションがメソッド指定の場合
     */
    class Util {

        private Util() {
        }

        /**
         * @param field {@link Env} annotated
         * @param args TODO not used???
         * @return When {@link Env#name()} is not set, the field name will be return.
         */
        public static String getName(Field field, String... args) {
            Env target = field.getAnnotation(Env.class);
            if (target == null) {
                throw new IllegalArgumentException("bean is not annotated with @Env");
            }

            String name = target.name();
            if (name.isEmpty()) {
                name = field.getName();
            }
            return name;
        }

        /**
         * @param field {@link Env} annotated
         */
        public static String getValue(Field field) {
            Env target = field.getAnnotation(Env.class);
            if (target == null) {
                throw new IllegalArgumentException("bean is not annotated with @Env");
            }

            return target.value();
        }
    }
}

/* */
