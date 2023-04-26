package at.halora.services.bot.commands;

import at.halora.services.bot.TelegramBot;

public class DeviceCommand extends BotCommand {
    public DeviceCommand(TelegramBot bot, Long userId, String command) {
        super(bot, userId, command);
    }

    @Override
    public void execute() {
        //TODO: Make sure user is registered with a username

        String[] parts = command.split(" ");
        if (parts.length != 2) {
            bot.sendBotMessage(userId, "Invalid format. Use /device <device_id> to switch to a HaLoRa device.");
            return;
        }

        String deviceId = parts[1];

        //TODO: Send device id to messagelogic

        bot.sendBotMessage(userId, "Your device has been registered. You can now send and receive messages with it.");
        bot.sendBotMessage(userId, "To switch back to using this Telegram chat, use /telegram.");
    }
}
