package triaina.commons.workerservice.annotation;

import java.lang.annotation.ElementType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import triaina.commons.workerservice.Worker;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Assign {
	@SuppressWarnings("rawtypes")
	Class<? extends Worker> worker();
	
	@SuppressWarnings("rawtypes")
	Class<? extends Worker>[] decorators() default {};
}
