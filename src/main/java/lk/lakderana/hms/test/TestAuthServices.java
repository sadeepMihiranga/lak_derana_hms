package lk.lakderana.hms.test;

import lk.lakderana.hms.LakDeranaHmsApplication;
import lk.lakderana.hms.entity.TMsRoleFunction;
import lk.lakderana.hms.repository.RoleFunctionRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = LakDeranaHmsApplication.class)
public class TestAuthServices {

    @Autowired
    private RoleFunctionRepository roleFunctionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Ignore
    @Test
    public void passwordResetTest() {

        String userEnterdCurrentPw = "1234";

        /*if(passwordEncoder.encode(userEnterdCurrentPw).equals(passwordEncoder.encode("$2a$10$SoFHsJwnge2u4gogcQ789.oOd9d8Rign0yy1W06X4BEWi162GciOm"))) {
            System.out.println("Matched");
        }*/

        if(passwordEncoder.matches(userEnterdCurrentPw, passwordEncoder.encode("$2a$10$SoFHsJwnge2u4gogcQ789.oOd9d8Rign0yy1W06X4BEWi162GciOm"))) {
            System.out.println("Matched");
        } else {
            System.out.println("No Matches");
        }
    }
}
