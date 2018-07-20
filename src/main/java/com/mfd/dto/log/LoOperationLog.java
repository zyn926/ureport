package com.mfd.dto.log;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class LoOperationLog {
    @NonNull
    private String id;
    @NonNull
    private String username;
    @NonNull
    private String name;
    @NonNull
    private String operationContent;

    private Date createTime;
}
