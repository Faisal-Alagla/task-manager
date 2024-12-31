package com.faisal.taskmanager.common.lookups;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.faisal.taskmanager.utils.Interfaces.BaseLookupResponseInterface;

@Data
@AllArgsConstructor
public class LookupResponseDto {

    private Integer id;

    private String name;

    static LookupResponseDto fromEntity(BaseLookupResponseInterface fileStatus) {
        return new LookupResponseDto(
                fileStatus.getId(),
                fileStatus.getName()
        );
    }
}
