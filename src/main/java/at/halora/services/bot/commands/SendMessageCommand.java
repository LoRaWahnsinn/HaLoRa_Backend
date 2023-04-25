package at.halora.services.bot.commands;

import at.halora.services.bot.TelegramBot;

public class SendMessageCommand extends BotCommand {

    public SendMessageCommand(TelegramBot bot, Long userId, String command) {
        super(bot, userId, command);
    }

    @Override
    public void execute() {
        //TODO: Make sure user is registered with a username

        String[] parts = command.split(" ", 3);
        if (parts.length != 3) {
            bot.sendMessage(userId, "Invalid format. Use /send <receiver> <message> to send a message.");
            return;
        }

        String receiver = parts[1];
        String message = parts[2];

        //TODO: Check if receiver exists

        //TODO: Send message to messagelogic

        bot.sendMessage(userId, "Your message \"" + message + "\" has been sent!");
    }
}
