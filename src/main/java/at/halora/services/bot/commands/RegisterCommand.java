package at.halora.services.bot.commands;

import at.halora.services.bot.TelegramBot;

public class RegisterCommand extends BotCommand {
    public RegisterCommand(TelegramBot bot, Long userId, String command) {
        super(bot, userId, command);
    }

    @Override
    public void execute() {

        //format validation
        String[] parts = command.split(" ");
        if (parts.length != 2) {
            bot.sendBotMessage(userId, "Invalid format. Use /register <username> to begin.");
            return;
        }

        String username = parts[1].toLowerCase();


        //character check
        if (!username.matches("[a-z0-9-_]+")) {
            bot.sendBotMessage(userId, "Username must only contain letters, numbers, dashes, underscores.");
            return;
        }

        //Check length
        if(username.length() > 10) {
            bot.sendBotMessage(userId, "Username is too long, max. 10 chars.");
            return;
        }

        //Check if username already exists
        if (bot.getLogic().getUserByAccountId(userId.toString()) != null) {
            bot.sendBotMessage(userId, "Username already taken.");
            return;
        }
        //Send username to messagelogic
        if (bot.getLogic().registerUser(userId)) {
            bot.sendBotMessage(userId, "Welcome " + username + "! Your username is now linked to your telegram account.");
            bot.sendBotMessage(userId, "You will now receive messages from other HaLoRa users through this chat!");
            bot.sendBotMessage(userId, "If you have a HaLoRa device and want to send and receive messages with LoRa, you can register it at any time with /device <device_id>.");
            bot.sendBotMessage(userId, "To send a new message, use /send <receiver> <message>.");

        } else {
            bot.sendBotMessage(userId, "Register failed. Please try again later");
        }

    }
}
