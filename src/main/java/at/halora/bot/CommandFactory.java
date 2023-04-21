package at.halora.bot;

import at.halora.bot.commands.*;

class CommandFactory {

    private final TelegramBot bot;

    public CommandFactory(TelegramBot bot) {
        this.bot = bot;
    }

    public BotCommand create(Long userId, String command) {
        if (command.equals("/start")) {
            return new StartCommand(bot, userId, command);
        } else if (command.matches("/send.*")){
            return new SendMessageCommand(bot, userId, command);
        } else if (command.matches("/register.*")) {
            return new RegisterCommand(bot, userId, command);
        } else if (command.equals("/help")) {
            return new HelpCommand(bot, userId, command);
        } else if (command.equals("/device")) {
            return new DeviceCommand(bot, userId, command);
        } else {
            return new BotCommand(bot, userId, command) {
                @Override
                public void execute() {
                    bot.sendMessage(userId, "Unknown command.");
                }
            };
        }
    }
}