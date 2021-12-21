package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.dto.BranchDTO;
import lk.lakderana.hms.entity.TRfBranch;
import lk.lakderana.hms.mapper.BranchMapper;
import lk.lakderana.hms.repository.BranchRepository;
import lk.lakderana.hms.service.BranchService;
import lk.lakderana.hms.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    public BranchServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public List<BranchDTO> getAllBranches() {

        final List<TRfBranch> tRfBranchList = branchRepository.findAllByBrnhStatus(Constants.STATUS_ACTIVE.getShortValue());

        if(tRfBranchList.isEmpty() || tRfBranchList == null)
            return Collections.emptyList();

        List<BranchDTO> branchDTOList = new ArrayList<>();

        tRfBranchList.forEach(tRfBranch -> {
            branchDTOList.add(BranchMapper.INSTANCE.entityToDTO(tRfBranch));
        });

        return branchDTOList;
    }
}
