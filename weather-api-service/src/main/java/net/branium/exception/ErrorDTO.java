package net.branium.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ErrorDTO {
    private LocalDateTime timeStamp;
    private int status;
    private String path;
    private List<String> errors;

}
