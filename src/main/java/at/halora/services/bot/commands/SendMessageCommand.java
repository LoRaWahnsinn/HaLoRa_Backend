package at.halora.services.bot.commands;

import at.halora.messagelogic.Message;
import at.halora.services.bot.TelegramBot;

import java.time.LocalTime;

public class SendMessageCommand extends BotCommand {

    public SendMessageCommand(TelegramBot bot, Long userId, String command) {
        super(bot, userId, command);
    }

    @Override
    public void execute() {
        //Make sure user is registered with a username
        if(bot.getLogic().getUserByAccountId(userId.toString()) == null) {
            bot.sendBotMessage(userId, "Your telegram account is not yet registered. Please use /register <username> first.");
            return;
        }

        String[] parts = command.split(" ", 3);
        if (parts.length != 3) {
            bot.sendBotMessage(userId, "Invalid format. Use /send <receiver> <message> to send a message.");
            return;
        }

        String receiver = parts[1].toLowerCase();
        String message = parts[2];

        //Check if receiver exists
        if (bot.getLogic().getUserByAccountId(receiver) == null) {
            bot.sendBotMessage(userId, "Failed to send message, user '" + receiver + "' does not exist.");
            return;
        }

        //Send message to messagelogic
        bot.getLogic().sendMessage(new Message(LocalTime.now().toString(), bot.getLogic().getUserByAccountId(userId.toString()).getUsername(), receiver, message));

        bot.sendBotMessage(userId, "Your message \"" + message + "\" has been sent!");
    }
}
