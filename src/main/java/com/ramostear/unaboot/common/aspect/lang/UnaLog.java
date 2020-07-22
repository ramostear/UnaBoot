package com.ramostear.unaboot.common.aspect.lang;

import java.lang.annotation.*;

/**
 * @author :    ramostear/树下魅狐
 * @version :   Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/7/22 0022 12:13.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.METHOD})
public @interface UnaLog {

    String title() default "";

    LogType type() default LogType.OTHER;

}
