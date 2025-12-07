package com.pismo.code.assessment.domain.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AccountCreationDto {

    @JsonIgnore
    private UUID accountId = UUID.randomUUID();
    private Long documentNumber;
}
