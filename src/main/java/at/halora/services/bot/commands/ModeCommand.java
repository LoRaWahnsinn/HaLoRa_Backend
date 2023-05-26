package at.halora.services.bot.commands;

import at.halora.services.bot.TelegramBot;
import at.halora.utils.DeviceType;

import java.util.Objects;

public class ModeCommand extends BotCommand {
    public ModeCommand(TelegramBot bot, Long userId, String command) {
        super(bot, userId, command);
    }

    @Override
    public void execute() {
        if(!bot.getLogic().userExists(userId)) {
            bot.sendBotMessage(userId, "Your telegram account is not yet registered. Please use /register <username> first.");
            return;
        }

        String[] parts = command.split(" ");
        if (parts.length != 2) {
            bot.sendBotMessage(userId, "Invalid format. Use /mode <dora|telegram> to switch to a HaLoRa device.");
            return;
        }

        String mode = parts[1].toLowerCase();

        DeviceType type = Objects.equals(mode, DeviceType.DORA.name()) ? DeviceType.DORA : (Objects.equals(mode, DeviceType.TELEGRAM.name()) ? DeviceType.TELEGRAM : null);

        if (type == null) {
            bot.sendBotMessage(userId, "Invalid mode type.");
            return;
        }

        bot.getLogic().setReceiveMode(bot.getLogic().getUsername(userId), type);

    }
}
