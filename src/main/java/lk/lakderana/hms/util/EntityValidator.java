package lk.lakderana.hms.util;

import lk.lakderana.hms.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class EntityValidator {

    @Autowired
    LocalValidatorFactoryBean localValidatorFactoryBean;

    protected void validateDTO(Object dto) {
        Set<ConstraintViolation<Object>> violations = localValidatorFactoryBean.validate(dto);
        if (violations.stream().findFirst().isPresent()) {
            throw new DataNotFoundException(violations.stream().findFirst().get().getMessage());
        }
    }
}
