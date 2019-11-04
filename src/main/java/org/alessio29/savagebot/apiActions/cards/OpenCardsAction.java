package org.alessio29.savagebot.apiActions.cards;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.cards.Card;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class OpenCardsAction implements IDiscordAction {
    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {
        int count = 1;
        int index = 1;
        if (args.length > 0) {
            try {
                count = Integer.parseInt(args[0]);
                index = 2;
            } catch (Exception e) {
                // count will be 1
            }
        }
        Deck deck = Decks.getDeck(event.getGuild(), event.getChannel());
        if(deck.isEmpty()) {
            return new CommandExecutionResult("Shuffle is needed..", index);
        }

        ReplyBuilder replyBuilder = new ReplyBuilder();
        for (int i=0; i<count;i++) {
            Card newCard = deck.getNextCard();
            if (newCard != null) {
                replyBuilder.attach(newCard.toString()).space();
            } else {
                break;
            }
        }
        return new CommandExecutionResult(replyBuilder.toString(), index);
    }
}
