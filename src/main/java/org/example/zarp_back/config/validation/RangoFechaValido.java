package org.example.zarp_back.config.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RangoFechaValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RangoFechaValido {

    String message() default "La fecha de fin debe ser posterior o igual a la fecha de inicio";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
