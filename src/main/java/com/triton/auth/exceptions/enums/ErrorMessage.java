package com.triton.auth.exceptions.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {

  // BAD_REQUEST
  INVALID_INPUT("The given %s is not valid"),

  // NOT_FOUND
  NOT_FOUND("%s not found."),

  // CONFLICT
  ALREADY_EXIST("%s already exist for given %s"),

  // NOT_IMPLEMENTED
  API_NOT_IMPLEMENTED("API has not been implemented yet."),
  APPLICATION_PROCESSING_ERROR("Server error in processing the request"),

  // UNAUTHORIZED
  INVALID_JWT_TOKEN("Invalid Token"),
  UNAUTHORIZED_ACCESS("Unauthorized Access");

  private final String message;

  ErrorMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return this.message;
  }
}
