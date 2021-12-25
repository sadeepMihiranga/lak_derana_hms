package lk.lakderana.hms.config;

import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.util.Constants;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.util.Set;

public class EntityValidator {

    @Autowired
    private LocalValidatorFactoryBean localValidatorFactoryBean;

    @Autowired
    private HttpServletRequest request;

    protected void validateEntity(Object dto) {
        Set<ConstraintViolation<Object>> violations = localValidatorFactoryBean.validate(dto);
        if (violations.stream().findFirst().isPresent()) {
            throw new DataNotFoundException(violations.stream().findFirst().get().getMessage());
        }
    }

    protected final Long captureBranchId() {

        String branchesString = request.getAttribute(Constants.REQUEST_BRANCHES.getValue()).toString();

        if(Strings.isNullOrEmpty(branchesString))
            throw new NoRequiredInfoException("Branch Id is missing");

        String[] branchesArray = branchesString
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .replaceAll("\\s", "")
                .split(",");

        return  Long.valueOf(branchesArray[0]);
    }
}
