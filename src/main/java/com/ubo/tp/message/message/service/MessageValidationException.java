package com.ubo.tp.message.message.service;

/**
 * Exception levée lorsque la validation d’un message échoue.
 */
public class MessageValidationException extends Exception {
  public MessageValidationException(String message) {
    super(message);
  }
}
