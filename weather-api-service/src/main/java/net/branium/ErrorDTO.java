package net.branium;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDTO {
    private LocalDateTime timeStamp;
    private int status;
    private String error;
    private String path;
}
