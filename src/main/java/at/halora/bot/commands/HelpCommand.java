package at.halora.bot.commands;

import at.halora.bot.TelegramBot;

public class HelpCommand extends BotCommand {
    public HelpCommand(TelegramBot bot, Long userId, String command) {
        super(bot, userId, command);
    }

    @Override
    public void execute() {
        bot.sendMessage(userId, "The following commands are available:");
        bot.sendMessage(userId, "/register <username> - Register a new HaLoRa username");
        bot.sendMessage(userId, "/device <device_id> - Register a new HaLoRa device");
        bot.sendMessage(userId, "/telegram - Switch back to using this Telegram chat");
        bot.sendMessage(userId, "/send <receiver> <message> - Send a new message");
        bot.sendMessage(userId, "/help - Show this help message");
    }
}
