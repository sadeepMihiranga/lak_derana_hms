package lk.lakderana.hms.test;

import lk.lakderana.hms.LakDeranaHmsApplication;
import lk.lakderana.hms.entity.TMsRoleFunction;
import lk.lakderana.hms.repository.RoleFunctionRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = LakDeranaHmsApplication.class)
public class TestAuthServices {

    @Autowired
    private RoleFunctionRepository roleFunctionRepository;

    @Ignore
    @Test
    public void getPermissionsByRoleTest() {
        final List<TMsRoleFunction> tMsRoleFunctionList = roleFunctionRepository.findAllByRoleRoleId(14l);

        tMsRoleFunctionList.forEach(tMsRoleFunction -> {
            System.out.println(tMsRoleFunction.getFunction().getFuncId());
            System.out.println(tMsRoleFunction.getRofuId());
            System.out.println(tMsRoleFunction.getRofuStatus());
        });
    }
}
