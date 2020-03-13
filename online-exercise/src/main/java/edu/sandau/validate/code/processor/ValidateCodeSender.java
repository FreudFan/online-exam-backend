package edu.sandau.validate.code.processor;

public interface ValidateCodeSender {
    void send(String destination, String code);
}
