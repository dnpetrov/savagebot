package com.github.alessio29.savagebot.commands.cards;

import com.github.alessio29.savagebot.cards.Card;
import com.github.alessio29.savagebot.cards.Deck;
import com.github.alessio29.savagebot.cards.Decks;
import com.github.alessio29.savagebot.cards.Hands;
import com.github.alessio29.savagebot.commands.Category;
import com.github.alessio29.savagebot.commands.ICommand;
import com.github.alessio29.savagebot.internal.CommandExecutionResult;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class DealCardCommand implements ICommand {

    @Override
    public String getName() {
        return "deal";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Category getCategory() {

        return Category.CARDS;
    }

    @Override
    public String getDescription() {

        return "secretly deals n (1 by default) cards to user (to self by default)";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"CardCount", "User"};
    }

    @Override
    public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) {
        Deck deck = Decks.getDeck(event.getGuild(), event.getChannel());
        if (deck.isEmpty()) {
            event.getChannel().sendMessage("Shuffle is needed.");
            return new CommandExecutionResult();
        }
        int count = 1;
        if (args.length > 0) {
            try {
                count = Integer.parseInt(args[0]);
            } catch (Exception e) {
                // count will be 1
            }
        }
        Guild guild = event.getGuild();
        User user = event.getAuthor();

        String message = "";

        for (int i = 0; i < count; i++) {
            Card newCard = deck.getCard();
            if (newCard != null) {
                Hands.getHand(guild, user).getCards().add(newCard);
                message = message + newCard.toString() + " ";
            } else {
                break;
            }
        }
        return new CommandExecutionResult(message, 2, true);
    }
}
