package com.github.alessio29.savagebot.parser2.desugar;

import com.github.alessio29.savagebot.internal.MessageTextBuilder;
import com.github.alessio29.savagebot.parser2.tree.statements.Statement;
import com.github.alessio29.savagebot.parser2.tree.statements.CommentStatement;
import com.github.alession29.savagebot.grammar.RollLexer;
import com.github.alession29.savagebot.grammar.RollParser;
import org.antlr.v4.runtime.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final MessageTextBuilder errors;

    public Parser(MessageTextBuilder errors) {
        this.errors = errors;
    }

    public List<Statement> parseStatements(String[] inputArgs) {
        List<Statement> result = new ArrayList<>();

        String joinedArgs = String.join(" ", inputArgs);

        StringBuilder nextStatement = new StringBuilder();
        int parenthesisBalance = 0;
        int squareBracketBalance = 0;
        int curlyBracketBalance = 0;

        for (int i = 0, length = joinedArgs.length(); i < length; ++i) {
            char ch = joinedArgs.charAt(i);
            if ((Character.isWhitespace(ch) || ch == ';') &&
                    parenthesisBalance == 0 && squareBracketBalance == 0 && curlyBracketBalance == 0) {
                Statement statement = parseSingleStatement(nextStatement.toString());
                nextStatement.setLength(0);
                if (statement != null) {
                    result.add(statement);
                }
            } else if (ch == '\\' && i + 1 < length) {
                nextStatement.append(joinedArgs.charAt(++i));
            } else {
                nextStatement.append(ch);
                switch (ch) {
                    case '(':
                        ++parenthesisBalance;
                        break;
                    case ')':
                        if (parenthesisBalance > 0) {
                            --parenthesisBalance;
                        }
                        break;
                    case '[':
                        ++squareBracketBalance;
                        break;
                    case ']':
                        if (squareBracketBalance > 0) {
                            --squareBracketBalance;
                        }
                        break;
                    case '{':
                        ++curlyBracketBalance;
                        break;
                    case '}':
                        if (curlyBracketBalance > 0) {
                            --curlyBracketBalance;
                        }
                        break;
                }
            }
        }

        if (nextStatement.length() > 0) {
            Statement statement = parseSingleStatement(nextStatement.toString());
            if (statement != null) {
                result.add(statement);
            }
        }

        return result;
    }

    private Statement parseSingleStatement(String string) {
        RollLexer lexer = new RollLexer(CharStreams.fromString(string));
        lexer.removeErrorListeners();
        lexer.addErrorListener(THROWING_ERROR_LISTENER);

        RollParser parser = new RollParser(new BufferedTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(THROWING_ERROR_LISTENER);

        try {
            RollParser.StmtContext stmt = parser.stmt();
            try {
                return new StatementDesugarer().visit(stmt);
            } catch (DesugarErrorException e) {
                errors.appendMessage("Error", "'" + string + "': " + e.getMessage());
                return null;
            }
        } catch (ParseErrorException e) {
            string = string.trim();
            if (string.length() > 0) {
                return new CommentStatement(string);
            } else {
                return null;
            }
        }
    }

    private static final ANTLRErrorListener THROWING_ERROR_LISTENER = new BaseErrorListener() {
        @Override
        public void syntaxError(
                Recognizer<?, ?> recognizer,
                Object offendingSymbol,
                int line,
                int charPositionInLine,
                String msg,
                RecognitionException e
        ) {
            throw new ParseErrorException("@" + charPositionInLine + ": " + msg, e);
        }
    };


}
