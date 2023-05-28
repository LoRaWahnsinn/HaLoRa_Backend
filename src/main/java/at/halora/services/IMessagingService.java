package at.halora.services;

import at.halora.messagelogic.Message;

public interface IMessagingService extends Runnable {
    boolean sendMessage(Message message);
}
