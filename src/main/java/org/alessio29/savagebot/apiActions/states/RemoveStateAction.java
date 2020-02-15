package org.alessio29.savagebot.apiActions.states;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.iterators.RemoveStatesParamIterator;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RemoveStateAction implements IBotAction {

    @Override
    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("Character name missing!", 1);
        }
        if (args.length < 2) {
            return new CommandExecutionResult("State(s) name missing!", 2);
        }

        RemoveStatesParamIterator it = new RemoveStatesParamIterator(args);
        List<String> list = new ArrayList<>();

        while (it.hasNext()) {
            String value = it.next();
            if (!it.isEntity(value)) {
                return new CommandExecutionResult("Provide character name!", args.length + 1);
            }
            Character character = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), value);
            if (character == null) {
                continue;
            }
            if (!it.nextIsModifier()) {
                return new CommandExecutionResult("No valid states to remove for character " + value, args.length + 1);
            }
            while (it.nextIsModifier()) {
                list.add(it.process(value, it.next(), character));
            }
            Characters.storeCharacter(message.getGuildId(), message.getChannelId(), character);
        }
        String response = "State(s) removed from character(s) " + StringUtils.join(list, ", ");
        return new CommandExecutionResult(response, args.length + 1);
    }
}