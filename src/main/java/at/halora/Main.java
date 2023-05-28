package at.halora;

import at.halora.messagelogic.MessageLogic;
import at.halora.persistence.Datasource;
import at.halora.persistence.IUserRepository;
import at.halora.persistence.UserRepository;
import at.halora.services.IMessagingService;
import at.halora.services.bot.CommandFactory;
import at.halora.services.bot.TelegramBot;
import at.halora.services.mqtt.TTNHandler;
import at.halora.utils.MessagingServiceType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) throws TelegramApiException {

        Datasource db = new Datasource();

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
        messageLogic.addMessagingService(MessagingServiceType.TELEGRAM, telegramBot);
        IMessagingService ttnClient = new TTNHandler(messageLogic, properties);
        messageLogic.addMessagingService(MessagingServiceType.DORA, ttnClient);

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