package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedEntity {

    private List<Object> entities;
    private Integer totalNoOfPages;
    private Long totalNoOfRecords;
}
