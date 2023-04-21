package at.halora.bot.commands;

import at.halora.bot.TelegramBot;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class BotCommand {

    TelegramBot bot;
    Long userId;
    String command;

    public abstract void execute();
}
