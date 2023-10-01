package com.triton.auth.exceptions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ExceptionResponse {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM d, yyyy h:mm a")
  @Schema(example = "Sep 29, 2023 2:30 PM", description = "The Time")
  private LocalDateTime timestamp;

  @Schema(example = "/api/v1/test-end-point", description = "Resource Path")
  private String path;

  @Schema(example = "Auth api failure message", description = "Reason of error in api call")
  private String message;

}
