package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.BranchDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BranchService {

    List<BranchDTO> getAllBranches();
}
