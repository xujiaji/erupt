package xyz.erupt.annotation.config;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Deprecated
public @interface AutoFill {

    String value();

}
