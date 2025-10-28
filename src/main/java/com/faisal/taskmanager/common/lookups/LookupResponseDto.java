package com.faisal.taskmanager.common.lookups;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.faisal.taskmanager.utils.Interfaces.BaseLookupInterface;

@Data
@AllArgsConstructor
public class LookupResponseDto {

    private Integer id;

    private String name;

    public static LookupResponseDto fromEntity(BaseLookupInterface item) {
        return new LookupResponseDto(
                item.getId(),
                item.getName()
        );
    }
}
