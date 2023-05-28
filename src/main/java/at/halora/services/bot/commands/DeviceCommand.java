package at.halora.services.bot.commands;

import at.halora.services.bot.TelegramBot;

public class DeviceCommand extends BotCommand {
    public DeviceCommand(TelegramBot bot, Long userId, String command) {
        super(bot, userId, command);
    }

    @Override
    public void execute() {
        //Make sure user is registered with a username
        if(bot.getLogic().getUserByAccountId(userId.toString()) == null) {
            bot.sendBotMessage(userId, "Your telegram account is not yet registered. Please use /register <username> first.");
            return;
        }

        String[] parts = command.split(" ");
        if (parts.length != 2) {
            bot.sendBotMessage(userId, "Invalid format. Use /device <device_id> to switch to a HaLoRa device.");
            return;
        }

        String deviceId = parts[1].toLowerCase();

        if (!deviceId.matches("eui-[0-9a-f]+")) {
            bot.sendBotMessage(userId, "Device Id has wrong format, should look like: 'eui-50f404de40305010'");
            return;
        }

        //Send device id to messagelogic
        if(bot.getLogic().registerTTNDevice(bot.getLogic().getUserByAccountId(userId.toString()).getUsername(), deviceId)) {
            bot.sendBotMessage(userId, "Your device has been registered. You can now send and receive messages with it.");
            bot.sendBotMessage(userId, "To switch back to using this Telegram chat, use /mode telegram.");
        } else {
            bot.sendBotMessage(userId, "Failed to register device, please try again later.");
        }

    }
}
