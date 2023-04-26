package at.halora.services.bot;

import at.halora.messagelogic.IMessageLogic;
import at.halora.services.IMessagingService;
import at.halora.services.bot.commands.BotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot implements IMessagingService {

    private CommandFactory commandFactory;
    private final IMessageLogic messageLogic;

    public TelegramBot(IMessageLogic messageLogic) {
        this.messageLogic = messageLogic;
    }

    public void setCommandFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }


    @Override
    public void sendMessage(Long id, String message) {

    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!AllowedUsers.allowedUsers.contains(update.getMessage().getFrom().getId())) { //check if user is on allowedUsers list
            return;
        }



        if (update.getMessage().isCommand()) { //check if message is command
            BotCommand command = commandFactory.create(update.getMessage().getFrom().getId(), update.getMessage().getText());
            command.execute();
            return;
        }

        if (update.getMessage().hasText()) { //check if message is text
            handleText(update.getMessage().getFrom().getId(), update.getMessage().getText());
            return;
        }

    }

    private void handleCommand(Long userId, String command) {

        if (command.equals("/start")) {

            //TODO Check if user is already registered

            sendBotMessage(userId, "Welcome! \uD83D\uDC4B \nBefore you can start using HaLoRa, you need to associate your Telegram Account with a HaLoRa username. Use /register <username> to begin.");

        } else if (command.equals("/help")) {
            sendBotMessage(userId, "The following commands are available:");
            sendBotMessage(userId, "/register <username> - Register a new HaLoRa username");
            sendBotMessage(userId, "/device <device_id> - Register a new HaLoRa device");
            sendBotMessage(userId, "/telegram - Switch back to using this Telegram chat");
            sendBotMessage(userId, "/send <receiver> <message> - Send a new message");
            sendBotMessage(userId, "/help - Show this help message");
        } else if (command.matches("/register.*")) {

            //TODO: Check if user is already registered

            String[] parts = command.split(" ");
            if (parts.length != 2) {
                sendBotMessage(userId, "Invalid format. Use /register <username> to begin.");
                return;
            }

            String username = parts[1];

            //TODO: Check if username already exists
            //TODO: Send username to messagelogic

            sendBotMessage(userId, "Welcome " + username + "! Your username is now linked to your telegram account.");
            sendBotMessage(userId, "You will now receive messages from other HaLoRa users through this chat!");
            sendBotMessage(userId, "If you have a HaLoRa device and want to send and receive messages with LoRa, you can register it at any time with /device <device_id>.");
            sendBotMessage(userId, "To send a new message, use /send <receiver> <message>.");

        } else if (command.matches("/send.*")) {

            //TODO: Make sure user is registered with a username

            String[] parts = command.split(" ", 3);
            if (parts.length != 3) {
                sendBotMessage(userId, "Invalid format. Use /send <receiver> <message> to send a message.");
                return;
            }

            String receiver = parts[1];
            String message = parts[2];

            //TODO: Check if receiver exists

            //TODO: Send message to messagelogic

            sendBotMessage(userId, "Your message \"" + message + "\" has been sent!");

        } else if (command.matches("/device.*")) {
            //TODO: Make sure user is registered with a username

            String[] parts = command.split(" ");
            if (parts.length != 2) {
                sendBotMessage(userId, "Invalid format. Use /device <device_id> to switch to a HaLoRa device.");
                return;
            }

            String deviceId = parts[1];

            //TODO: Send device id to messagelogic

            sendBotMessage(userId, "Your device has been registered. You can now send and receive messages with it.");
            sendBotMessage(userId, "To switch back to using this Telegram chat, use /telegram.");
        } else if (command.equals("/telegram")) {

            //TODO: Send command to messagelogic to switch user back to telegram

            sendBotMessage(userId, "You are now using this Telegram chat to send and receive messages! Use /send <receiver> <message> to send a new message.");

        } else {
            sendBotMessage(userId, "Unknown command.");
        }

    }

    private void handleText(Long userId, String text) {
        sendBotMessage(userId, "Unknown command.");
        return;
    }

    @Override
    public String getBotUsername() {
        // Return bot username
        return "HaLoRaBot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return System.getenv(("HaLoRaBot_Token"));
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
}