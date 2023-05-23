package at.halora;

import at.halora.messagelogic.IMessageLogic;
import at.halora.messagelogic.MessageLogic;
import at.halora.persistance.IUserRepository;
import at.halora.persistance.UserRepository;
import at.halora.services.IMessagingService;
import at.halora.services.bot.CommandFactory;
import at.halora.services.bot.TelegramBot;
import at.halora.services.mqtt.TTNHandler;
import at.halora.utils.DeviceType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws TelegramApiException {

        //load configuration
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String configPath = rootPath + "halora.properties";
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(configPath);
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //create message logic
        MessageLogic messageLogic = new MessageLogic();

        //create messaging services and add to message logic
        TelegramBot telegramBot = new TelegramBot(messageLogic, properties);
        messageLogic.addMessagingService(DeviceType.TELEGRAM, telegramBot);
        IMessagingService ttnClient = new TTNHandler(messageLogic, properties);
        messageLogic.addMessagingService(DeviceType.DORA, ttnClient);

        //create persistence layer and add to message logic
        IUserRepository userRepository = new UserRepository();
        messageLogic.setUserRepository(userRepository);

        //inject command factory
        CommandFactory commandFactory = new CommandFactory(telegramBot);
        telegramBot.setCommandFactory(commandFactory);

        //activate telegram bot
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(telegramBot);

        messageLogic.initMessagingServices();
    }

}