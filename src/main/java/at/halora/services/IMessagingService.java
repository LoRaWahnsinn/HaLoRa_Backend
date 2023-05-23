package at.halora.services;

public interface IMessagingService extends Runnable {
    void sendMessage(Long id, String message);
}
