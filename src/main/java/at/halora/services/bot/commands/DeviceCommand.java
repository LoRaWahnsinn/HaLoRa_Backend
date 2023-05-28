package at.halora.services.bot.commands;

import at.halora.services.bot.TelegramBot;
import at.halora.utils.MessagingServiceType;

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

        //Send device id to messagelogic
        var user = bot.getLogic().getUserByAccountId(userId.toString());
        user.addAccount(MessagingServiceType.DORA, deviceId);
        if(bot.getLogic().registerTTNDevice(user)) {
            bot.sendBotMessage(userId, "Your device has been registered. You can now send and receive messages with it.");
            bot.sendBotMessage(userId, "To switch back to using this Telegram chat, use /mode telegram.");
        } else {
            bot.sendBotMessage(userId, "Failed to register device, please try again later.");
        }

    }
}
