package at.halora.services.bot.commands;

import at.halora.persistence.User;
import at.halora.services.bot.TelegramBot;

public class StartCommand extends BotCommand {
    public StartCommand(TelegramBot bot, Long userId, String command) {
        super(bot, userId, command);
    }

    @Override
    public void execute() {
        //Check if user is already registered
        User user = bot.getLogic().getUserByAccountId(userId.toString());

        if (user != null) {
            bot.sendBotMessage(userId, "Welcome " + user.getUsername() + "!");
            return;
        }
        bot.sendBotMessage(userId, "Welcome! \uD83D\uDC4B \nBefore you can start using HaLoRa, you need to associate your Telegram Account with a HaLoRa username. Use /register <username> to begin.");
    }
}
