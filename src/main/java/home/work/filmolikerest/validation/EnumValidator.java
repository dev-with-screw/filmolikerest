package home.work.filmolikerest.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EnumValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@NotNull(message = "Should not be null")
@ReportAsSingleViolation
public @interface EnumValidator
{
    Class<? extends Enum<?>> enumClazz();
    String message() default "Должно содержать значение из соответствующего enum";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default  {};
}
