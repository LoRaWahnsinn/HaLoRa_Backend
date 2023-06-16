package at.halora.services.bot.commands;

import at.halora.services.bot.TelegramBot;
import at.halora.utils.MessagingServiceType;

import java.util.Objects;

public class ModeCommand extends BotCommand {
    public ModeCommand(TelegramBot bot, Long userId, String command) {
        super(bot, userId, command);
    }

    @Override
    public void execute() {
        if(bot.getLogic().getUserByAccountId(userId.toString()) == null) {
            bot.sendBotMessage(userId, "Your telegram account is not yet registered. Please use /register <username> first.");
            return;
        }

        String[] parts = command.split(" ");
        if (parts.length != 2) {
            bot.sendBotMessage(userId, "Invalid format. Use /mode <dora|telegram> to switch to a HaLoRa device.");
            return;
        }

        String mode = parts[1].toLowerCase();

        MessagingServiceType type = Objects.equals(mode, MessagingServiceType.DORA.getName()) ? MessagingServiceType.DORA : (Objects.equals(mode, MessagingServiceType.TELEGRAM.getName()) ? MessagingServiceType.TELEGRAM : null);

        if (type == null) {
            bot.sendBotMessage(userId, "Invalid mode type.");
            return;
        }

        System.out.println(type.getName());

        bot.getLogic().setReceiveMode(bot.getLogic().getUserByAccountId(userId.toString()).getUsername(), type);

    }
}
