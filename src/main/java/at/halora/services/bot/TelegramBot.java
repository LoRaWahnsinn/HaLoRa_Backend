package at.halora.services.bot;

import at.halora.messagelogic.IMessageLogic;
import at.halora.services.IMessagingService;
import at.halora.services.bot.commands.BotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class TelegramBot extends TelegramLongPollingBot implements IMessagingService {

    private CommandFactory commandFactory;

    private final IMessageLogic messageLogic;

    private final Properties config;

    public TelegramBot(IMessageLogic messageLogic, Properties configProperties) {
        this.messageLogic = messageLogic;
        this.config = configProperties;
    }

    public void setCommandFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public IMessageLogic getLogic() {
        return messageLogic;
    }


    @Override
    public void sendMessage(Long id, String message) {

    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!getAllowedUsers().contains(update.getMessage().getFrom().getId())) {
            sendBotMessage(update.getMessage().getFrom().getId(), "Unauthorized User ID. Please Contact the Halora Administrator.");
            return;
        }

        if (update.getMessage().isCommand()) { //check if message is command
            BotCommand command = commandFactory.create(update.getMessage().getFrom().getId(), update.getMessage().getText());
            command.execute();
            return;
        }

        if (update.getMessage().hasText()) { //check if message is text
            handleText(update.getMessage().getFrom().getId(), update.getMessage().getText());
        }

    }

    private List<Long> getAllowedUsers() {
        var idList = new ArrayList<Long>();
        Arrays.stream(config.getProperty("BOT_ALLOWED_USERS").split(",")).toList().forEach((s) -> idList.add(Long.valueOf(s)));
        return idList;

    }

    private void handleText(Long userId, String text) {
        sendBotMessage(userId, "Unknown command.");
    }

    @Override
    public String getBotUsername() {
        // Return bot username
        return config.getProperty("BOT_USERNAME");
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return config.getProperty("BOT_TOKEN");
    }

    public void sendBotMessage(Long userId, String text) {
        SendMessage sm = SendMessage.builder()
                .chatId(userId.toString())
                .text(text).build();

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

    }
}