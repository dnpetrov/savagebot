package com.github.alessio29.savagebot.commands.info;

import com.github.alessio29.savagebot.commands.Category;
import com.github.alessio29.savagebot.commands.ICommand;
import com.github.alessio29.savagebot.internal.CommandExecutionResult;
import com.github.alessio29.savagebot.internal.CommandRegistry;
import com.github.alessio29.savagebot.internal.Messages;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;


public class HelpCommand implements ICommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.INFO;
    }

    @Override
    public String getDescription() {
        return "Lists the description and syntax for every registered command.";
    }

    @Override
    public String[] getArguments() {
        return null;
    }

    @Override
    public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) {
        CategorizedMap categories = new CategorizedMap();
        CommandRegistry.current().getRegisteredCommands().forEach(c -> {
            if (c.getCategory() != null) {
                categories.put(c.getCategory(), c);
            } else {
                categories.put(Category.OTHER, c);
            }
        });

        StringBuilder b = new StringBuilder();
        categories.keySet().stream().sorted(Category::compareTo).forEach(category -> {
            b.append(Messages.underlined(Messages.bold("\n" + category.toString() + " category\n")));
            categories.get(category).forEach(command ->
                    b.append("\n").append(command.asHelpString()).append("\n")
            );
        });
        return new CommandExecutionResult(b.toString(), 1, true);
    }

    private class CategorizedMap extends HashMap<Category, HashSet<ICommand>> {

        private static final long serialVersionUID = 634791493137861944L;

        /**
         * Associates the specified value with the specified key in this map. If the map
         * previously contained a mapping for the key, the old value is replaced.
         *
         * @param key   key with which the specified value is to be associated
         * @param value value to be associated with the specified key
         * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if
         * there was no mapping for <tt>key</tt>. (A <tt>null</tt> return can
         * also indicate that the map previously associated <tt>null</tt> with
         * <tt>key</tt>.)
         */
        public ICommand put(Category key, ICommand value) {
            if (get(key) == null) {
                super.put(key, new HashSet<>(Collections.singletonList(value)));
            }
            get(key).add(value);
            return value;
        }

    }


}
