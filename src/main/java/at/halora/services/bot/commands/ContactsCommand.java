package at.halora.services.bot.commands;

import at.halora.messagelogic.Message;
import at.halora.persistence.Group;
import at.halora.persistence.User;
import at.halora.services.bot.TelegramBot;

import java.time.LocalTime;

public class ContactsCommand extends BotCommand{
    public ContactsCommand(TelegramBot bot, Long userId, String command) {
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

        String[] parts = command.split(" ", 4);
        if (parts.length != 3 && parts.length != 4  || (parts.length == 3 && !parts[1].equals("list")) ||
                (parts.length == 4 && !parts[1].equals("add"))){
            abort();
            return;
        }

        if (parts[1].equals("list")) {
            if (parts[2].equals("users")) {
                StringBuilder contactList = new StringBuilder("User contacts:\n");
                for (User userContact : user.getUserContacts()) {
                    contactList.append(userContact.getUsername()).append("\n");
                }
                bot.sendBotMessage(userId, contactList.toString());
            } else if (parts[2].equals("groups")) {
                StringBuilder contactList = new StringBuilder("Group contacts:\n");
                for (Group groupContact : user.getGroupContacts()) {
                    contactList.append(groupContact.getGroup_name()).append("\n");
                }
                bot.sendBotMessage(userId, contactList.toString());
            } else {
                abort();
            }
        }

        if (parts[1].equals("add")) {
            if (parts[2].equals("user")) {
                //check if user exists
                User userContact = bot.getLogic().getUserByName(parts[3]);
                if (userContact == null) {
                    bot.sendBotMessage(userId, "Failed to add contact. User \"" + parts[3] + "\" does not exist.");
                    return;
                }
                user.addUserContact(userContact);
                bot.getLogic().updateUser(user);
                bot.sendBotMessage(userId, "Contact \"" + parts[3] + "\"added to your user contacts.");
            } else if (parts[2].equals("group")) {
                Group group = bot.getLogic().getGroupByName(parts[3]);
                if (group == null) {
                    bot.sendBotMessage(userId, "Failed to add contact. Group \"" + parts[3] + "\" does not exist.");
                    return;
                }
                user.addGroupContact(group);
                bot.getLogic().updateUser(user);
                bot.sendBotMessage(userId, "Contact \"" + parts[3] + "\"added to your group contacts.");
            } else {
                abort();
            }
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
