package com.github.alessio29.savagebot.commands.admin;

import com.github.alessio29.savagebot.commands.Category;
import com.github.alessio29.savagebot.commands.ICommand;
import com.github.alessio29.savagebot.internal.CommandExecutionResult;
import com.github.alessio29.savagebot.internal.Prefixes;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PrefixCommand implements ICommand {

    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }

    @Override
    public String getDescription() {
        return "Sets <character> as custom user-defined command prefix or shows current prefix";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"[<character>]"};
    }

    @Override
    public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) {
        CommandExecutionResult result;
        if (args.length > 0) {
            String newPrefix = args[0].trim();
            if (newPrefix.length() > 1) {
                result = new CommandExecutionResult("Prefix must be one-character long!", 1);
            } else {
                Prefixes.setPrefix(event.getAuthor(), newPrefix);
                result = new CommandExecutionResult("Prefix is set to " + newPrefix, 2);
            }
        } else {
            String prfx = Prefixes.getPrefix(event.getAuthor());
            if (prfx == null) {
                result = new CommandExecutionResult("Custom prefix is not set! Default prefix is " + Prefixes.DEFAULT_PREFIX, 1);
            } else {
                result = new CommandExecutionResult("Prefix is '" + prfx + "'", 1);
            }
        }
        return result;
    }
}
