package at.halora.services.bot.commands;

import at.halora.services.bot.TelegramBot;

public class StartCommand extends BotCommand {
    public StartCommand(TelegramBot bot, Long userId, String command) {
        super(bot, userId, command);
    }

    @Override
    public void execute() {
        //Check if user is already registered
        if (bot.getLogic().getUser(userId.toString()) == null) {
            bot.sendBotMessage(userId, "Welcome " + bot.getLogic().getUser(userId.toString()).getUsername() + "!");
        }
        bot.sendBotMessage(userId, "Welcome! \uD83D\uDC4B \nBefore you can start using HaLoRa, you need to associate your Telegram Account with a HaLoRa username. Use /register <username> to begin.");
    }
}
