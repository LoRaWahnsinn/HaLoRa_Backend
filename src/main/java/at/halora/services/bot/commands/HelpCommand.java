package at.halora.services.bot.commands;

import at.halora.services.bot.TelegramBot;

public class HelpCommand extends BotCommand {
    public HelpCommand(TelegramBot bot, Long userId, String command) {
        super(bot, userId, command);
    }

    @Override
    public void execute() {
        bot.sendBotMessage(userId, "The following commands are available:");
        bot.sendBotMessage(userId, "/register <username> - Register a new HaLoRa username");
        bot.sendBotMessage(userId, "/device <device_id> - Register a new HaLoRa device");
        bot.sendBotMessage(userId, "/telegram - Switch back to using this Telegram chat");
        bot.sendBotMessage(userId, "/send <receiver> <message> - Send a new message");
        bot.sendBotMessage(userId, "/help - Show this help message");
    }
}
