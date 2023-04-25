package at.halora.services.bot.commands;

import at.halora.services.bot.TelegramBot;

public class RegisterCommand extends BotCommand {
    public RegisterCommand(TelegramBot bot, Long userId, String command) {
        super(bot, userId, command);
    }

    @Override
    public void execute() {
        //TODO: Check if user is already registered

        String[] parts = command.split(" ");
        if (parts.length != 2) {
            bot.sendMessage(userId, "Invalid format. Use /register <username> to begin.");
            return;
        }

        String username = parts[1];

        //TODO: Check if username already exists
        //TODO: Send username to messagelogic

        bot.sendMessage(userId, "Welcome " + username + "! Your username is now linked to your telegram account.");
        bot.sendMessage(userId, "You will now receive messages from other HaLoRa users through this chat!");
        bot.sendMessage(userId, "If you have a HaLoRa device and want to send and receive messages with LoRa, you can register it at any time with /device <device_id>.");
        bot.sendMessage(userId, "To send a new message, use /send <receiver> <message>.");
    }
}
