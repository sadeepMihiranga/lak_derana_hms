package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.dto.BranchDTO;
import lk.lakderana.hms.entity.TRfBranch;
import lk.lakderana.hms.mapper.BranchMapper;
import lk.lakderana.hms.repository.BranchRepository;
import lk.lakderana.hms.service.BranchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static lk.lakderana.hms.util.constant.Constants.STATUS_ACTIVE;

@Slf4j
@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    public BranchServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public List<BranchDTO> getAllBranches() {

        final List<TRfBranch> tRfBranchList = branchRepository.findAllByBrnhStatus(STATUS_ACTIVE.getShortValue());

        if(tRfBranchList.isEmpty() || tRfBranchList == null)
            return Collections.emptyList();

        List<BranchDTO> branchDTOList = new ArrayList<>();

        tRfBranchList.forEach(tRfBranch -> {
            branchDTOList.add(BranchMapper.INSTANCE.entityToDTO(tRfBranch));
        });

        return branchDTOList;
    }
}
