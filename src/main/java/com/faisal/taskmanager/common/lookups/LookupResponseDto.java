package com.faisal.taskmanager.common.lookups;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LookupResponseDto {

    private Integer id;

    private String name;

    public static LookupResponseDto fromEntity(BaseLookupEntity lookupEntity) {
        return new LookupResponseDto(
                lookupEntity.getId(),
                lookupEntity.getName()
        );
    }
}
