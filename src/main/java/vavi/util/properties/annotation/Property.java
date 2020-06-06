/*
 * Copyright (c) 2010 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.properties.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import vavi.beans.AdvancedBinder;
import vavi.beans.Binder;


/**
 * Property.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2010/09/30 nsano initial version <br>
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    /**
     * Property name.
     * When this is not set, the field name will be used.
     * <code>{#}</code> (# is 0, 1, 2...) will be replaced by parameters (String...) of {@link PropsEntity.Util#bind(Object, String...)}
     */
    String name() default "";

    /**
     * default value for this property.
     */
    String value() default "";

    /**
     * フィールドに値を代入する実装クラス
     */
    Class<? extends Binder> binder() default AdvancedBinder.class;

    /**
     * if true, when the url has error, system properties are used.
     */
    boolean useSystem() default false;

    /**
     * TODO アノテーションがメソッド指定の場合
     */
    final class Util {

        private Util() {
        }

        /**
         * @param field {@link Property} annotated
         * @return When {@link Property#name()} is not set, the field name will be return.
         */
        static String getName(Field field) {
            Property target = field.getAnnotation(Property.class);
            if (target == null) {
                throw new IllegalArgumentException("bean is not annotated with @Property");
            }

            String name = target.name();
            if (name.isEmpty()) {
                name = field.getName();
            }
            return name;
        }

        /**
         * @param field {@link Property} annotated
         */
        static String getValue(Field field) {
            Property target = field.getAnnotation(Property.class);
            if (target == null) {
                throw new IllegalArgumentException("bean is not annotated with @Property");
            }

            return target.value();
        }

        /**
         * @param field {@link Property} annotated
         */
        static boolean useSystem(Field field) {
            Property target = field.getAnnotation(Property.class);
            if (target == null) {
                throw new IllegalArgumentException("bean is not annotated with @Property");
            }

            return target.useSystem();
        }

        /**
         * @param field @{@link Property} annotated field.
         */
        static <T> Binder getBinder(Field field) {
            Property target = field.getAnnotation(Property.class);
            if (target == null) {
                throw new IllegalArgumentException("bean is not annotated with @Property");
            }

            try {
                Binder binder = target.binder().getDeclaredConstructor().newInstance();
                return binder;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
}

/* */
