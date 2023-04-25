package at.halora;

import at.halora.messagelogic.IMessageLogic;
import at.halora.messagelogic.MessageLogic;
import at.halora.persistance.IUserRepository;
import at.halora.persistance.UserRepository;
import at.halora.services.IMessagingService;
import at.halora.services.bot.CommandFactory;
import at.halora.services.bot.TelegramBot;
import at.halora.services.mqtt.TTNHandler;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException {


        MessageLogic messageLogic = new MessageLogic();

        //create messaging services
        TelegramBot telegramBot = new TelegramBot(messageLogic);
        //IMessagingService ttnClient = new TTNHandler(messageLogic);

        //create persistence layer
        IUserRepository userRepository = new UserRepository();

        //inject command factory
        CommandFactory commandFactory = new CommandFactory(telegramBot);
        telegramBot.setCommandFactory(commandFactory);

        //set message logic dependencies
        //messageLogic.setTelegramBot(telegramBot);
        //messageLogic.setTtnClient(ttnClient);
        //messageLogic.setUserRepository(userRepository);

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        botsApi.registerBot(telegramBot);
    }

}