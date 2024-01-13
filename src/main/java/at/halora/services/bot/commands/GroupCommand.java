package at.halora.services.bot.commands;

import at.halora.services.bot.TelegramBot;

public class GroupCommand extends BotCommand{
    public GroupCommand(TelegramBot bot, Long userId, String command) {
        super(bot, userId, command);
    }

    @Override
    public void execute() {

    }
}
