package at.halora.services;

public interface IMessagingService extends Runnable {
    boolean sendMessage(String id, String message);

}
