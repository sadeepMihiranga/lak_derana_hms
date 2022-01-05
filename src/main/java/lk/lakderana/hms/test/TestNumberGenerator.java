package lk.lakderana.hms.test;

import lk.lakderana.hms.LakDeranaHmsApplication;
import lk.lakderana.hms.repository.NumberGeneratorRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LakDeranaHmsApplication.class)
public class TestNumberGenerator {

    @Autowired
    private NumberGeneratorRepository numberGeneratorRepository;

    @Ignore
    @Test
    public void numberGeneratorTest() {
        final String generateNumber = numberGeneratorRepository.generateNumber("IN", "Y", "#", "#",
                "#", "#", "#", "#");

        System.out.println(generateNumber);
    }
}
