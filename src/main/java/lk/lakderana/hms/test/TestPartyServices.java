package lk.lakderana.hms.test;

import lk.lakderana.hms.LakDeranaHmsApplication;
import lk.lakderana.hms.dto.PartyDTO;
import lk.lakderana.hms.repository.NumberGeneratorRepository;
import lk.lakderana.hms.service.PartyService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LakDeranaHmsApplication.class)
public class TestPartyServices {

    @Autowired
    private PartyService partyService;

    @Ignore
    @Test
    public void filterContactNoTest() {
        final PartyDTO partyDTO = partyService.getPartyByPartyCode("CC00000004");

        final List<String> cnmbl = partyDTO.getContactList()
                .stream()
                .filter(partyContactDTO -> partyContactDTO.getContactType().equals("CNMBL"))
                .map(partyContactDTO -> partyContactDTO.getContactNumber())
                .collect(Collectors.toList());

        cnmbl.forEach(System.out::println);
    }
}
