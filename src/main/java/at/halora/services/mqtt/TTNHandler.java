package at.halora.services.mqtt;

import at.halora.messagelogic.IMessageLogic;
import at.halora.messagelogic.MessageLogic;
import at.halora.services.IMessagingService;

public class TTNHandler implements IMessagingService {

    private IMessageLogic messageLogic;

    public TTNHandler(IMessageLogic messageLogic) {
        this.messageLogic = messageLogic;
    }

    @Override
    public void sendMessage(Integer id, String message) {
    }
}
