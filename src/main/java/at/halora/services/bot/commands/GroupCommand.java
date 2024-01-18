package at.halora.services.bot.commands;

import at.halora.persistence.Group;
import at.halora.persistence.User;
import at.halora.services.bot.TelegramBot;

public class GroupCommand extends BotCommand{
    public GroupCommand(TelegramBot bot, Long userId, String command) {
        super(bot, userId, command);
    }

    @Override
    public void execute() {
        //Make sure user is registered with a username
        User user = bot.getLogic().getUserByAccountId(userId.toString());
        if(user == null) {
            bot.sendBotMessage(userId, "Your telegram account is not yet registered. Please use /register <username> first.");
            return;
        }
        // /group create <name>
        // /group add <groupname> <username>
        String[] parts = command.split(" ", 4);
        if (parts.length != 3 && parts.length != 4  || (parts.length == 3 && !parts[1].equals("create")) ||
                (parts.length == 4 && !parts[1].equals("add"))){
            abort();
            return;
        }

        if (parts[1].equals("create")) {
            if (bot.getLogic().getGroupByName(parts[2]) == null) {
                Group group = new Group();
                group.setGroup_name(parts[2]);
                bot.getLogic().createGroup(group);
                bot.sendBotMessage(userId, "Group \"" + parts[2] + "\" created.");
            } else {
                bot.sendBotMessage(userId, "Group \"" + parts[2] + "\" already exists.");
            }
        }

        Group group;
        User groupMember;
        if (parts[1].equals("add")) {
            if ((group = bot.getLogic().getGroupByName(parts[2])) == null) {
                bot.sendBotMessage(userId, "Group \"" + parts[2] + "\" does not exist.");
                return;
            }
            if ((groupMember = bot.getLogic().getUserByName(parts[3])) == null) {
                bot.sendBotMessage(userId,"User \"" + parts[3] + "\" does not exist.");
            }
            group.addMember(groupMember);
            bot.getLogic().updateGroup(group);
            bot.sendBotMessage(userId,"User \"" + parts[3] + "\" does not exist.");
        }


    }





    public void abort() {
        bot.sendBotMessage(userId, """
                    Invalid format.
                    Use /contacts list users to list your user contacts.
                    Use /contacts list groups to list your group contacts.
                    Use /contacts add user NAME to add a new user contact.
                    Use /contacts add group NAME to add a new group contact.""");
    }
}
