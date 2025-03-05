package com.ubo.tp.message.components.message.controller;

/**
 * Exception levée lorsque la validation d’un message échoue.
 */
public class MessageValidationException extends Exception {
  public MessageValidationException(String message) {
    super(message);
  }
}
