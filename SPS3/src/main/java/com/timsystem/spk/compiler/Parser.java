package com.timsystem.spk.compiler;

import com.timsystem.spk.compiler.ast.*;
import com.timsystem.spk.compiler.lib.Token;
import com.timsystem.spk.compiler.lib.TokenType;
import com.timsystem.spk.vm.BinaryOperators;
import com.timsystem.spk.vm.SPKException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Parser {

    private Lexer lexer;
    private ParserAccumulator accumulator;
    private int label_num = -1;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.accumulator = new ParserAccumulator();
        accumulator.previous = new Token(TokenType.EOF, "\0", -1);
        accumulator.current = accumulator.previous;
        advance();
    }

    public AST parse() {
        ArrayList<AST> block = new ArrayList<>();
        while (accumulator.current.getType() != TokenType.EOF) {
            block.add(statement());
            match(TokenType.SEMICOLON);
        }
        return new ProgramAST(block);
    }

    public AST statement() {
        if (match(TokenType.IF)) {
            AST expr = root();
            AST body = blockOrStatement();
            AST elseBody = null;
            if (match(TokenType.ELSE)) {
                elseBody = blockOrStatement();
            }
            return new BranchIfAST(expr, body, elseBody);
        }
        if(match(TokenType.DO)) {
            return new DoAST(blockOrStatement(), label_num, line());
        }
        if (match(TokenType.VAR)) {
            String name = consume(TokenType.WORD).getText();
            consume(TokenType.EQ);
            return new DefineVariableAST(root(), name, line());
        }

        if(match(TokenType.WORD)){
            String name = accumulator.previous.getText();
            if(match(TokenType.EQ)){
                return new EditVariableAST(root(), name, line());
            }
            return term();
        }
        if (match(TokenType.OUT)) {
            consume(TokenType.COLON);
            return new StdOutAST(root(), line());
        }
        if (match(TokenType.WHILE)) {
            AST expr = root();
            return new WhileAST(blockOrStatement(), expr, line());
        }
        if (match(TokenType.FOR)) {
            consume(TokenType.LPAREN);
            AST initializer = statement();
            consume(TokenType.COMMA);
            AST condition = root();
            consume(TokenType.COMMA);
            AST termination = statement();
            consume(TokenType.RPAREN);
            AST body = blockOrStatement();
            return new ForAST(initializer, condition, termination, body);
        }


        return new PopAST(root());
    }

    private AST root() {
        return conditional();
    }

    private AST conditional() {
        AST expr1 = term();

        if (match(TokenType.LT)) {
            return new BinaryAST(expr1, term(), BinaryOperators.LOWER, line());
        }
        if (match(TokenType.GT)) {
            return new BinaryAST(expr1, term(), BinaryOperators.GREATER, line());
        }
        if (match(TokenType.LTEQ)) {
            return new BinaryAST(expr1, term(), BinaryOperators.EQUAL_LOWER, line());
        }
        if (match(TokenType.GTEQ)) {
            return new BinaryAST(expr1, term(), BinaryOperators.EQUAL_GREATER, line());
        }
        if (match(TokenType.EQEQ)) {
            return new BinaryAST(expr1, term(), BinaryOperators.EQUAL, line());
        }
        if (match(TokenType.NOTEQ)) {
            return new BinaryAST(expr1, term(), BinaryOperators.NOT_EQUAL, line());
        }

        return expr1;
    }

    private AST term() {
        AST expr = factor();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            char operation = accumulator.previous.getType() == TokenType.PLUS ?
                    '+' : '-';
            expr = new BinaryAST(expr, factor(), operation, accumulator.previous.getLine());
        }
        return expr;
    }
    private AST factor() {
        AST expr = unary();
        while (match(TokenType.POW, TokenType.STAR, TokenType.SLASH, TokenType.REMAINDER)) {
            char operation = ' ';
            if (accumulator.previous.getType() == TokenType.POW) operation = '^';
            else if (accumulator.previous.getType() == TokenType.STAR) operation = '*';
            else if (accumulator.previous.getType() == TokenType.SLASH) operation = '/';
            else if (accumulator.previous.getType() == TokenType.REMAINDER) operation = '%';
            expr = new BinaryAST(expr, unary(), operation, accumulator.previous.getLine());
        }
        return expr;
    }

    private AST unary() {
        AST expr = null;
        while (match(TokenType.MINUS)) {
            expr = new UnaryAST(expr == null ? primary() : expr, '-', accumulator.previous.getLine());
        }
        return expr == null ? primary() : expr;
    }

    private AST primary() {
        advance();
        switch (accumulator.previous.getType()) {
            case NUMBER -> {
                return new ConstantAST(Double.parseDouble(accumulator.previous.getText()), line());
            }
            case HEX_NUMBER -> {
                return new ConstantAST(Long.parseLong(accumulator.previous.getText(), 16), line());
            }
            case LPAREN -> {
                AST expr = root();
                match(TokenType.RPAREN);
                return expr;
            }
            case WORD -> {
                return new VariableAST(accumulator.previous.getText(), line());
            }
            case STRING ->{
                return new StringAST(accumulator.previous.getText(), line());
            }
            case BOOLEAN ->{
                return new BooleanAST(accumulator.previous.getText(), line());
            }
            case NULL ->{
                return new NullAST(line());
            }
            case INPUT -> {
                consume(TokenType.COLON);
                return new StdInputAST(root(), line());
            }
            default -> {
                System.out.println(accumulator.previous);
                throw new SPKException("ParseException", "bad expression " + accumulator.current, line());
            }
        }
    }

    private AST blockOrStatement(){
        if(match(TokenType.LBRACE)){
            ArrayList<AST> arr = new ArrayList<>();
            while (!match(TokenType.RBRACE)){
                arr.add(statement());
            }
            return new BlockAST(arr);
        }
        return statement();
    }

    private int line() {
        return accumulator.current.getLine();
    }

    private Token consume(TokenType... types) {
        if (match(types)) {
            return accumulator.previous;
        } else {
            throw new SPKException("ParseException", "unexpected token '" + accumulator.current + "', expected '" + Arrays.toString(types), line());
        }
    }

    private boolean match(TokenType... types) {
        TokenType current = accumulator.current.getType();
        for (TokenType type : types) {
            if (current == type) {
                advance();
                return true;
            }
        }
        return false;
    }

    private void advance() {
        accumulator.previous = accumulator.current;
        accumulator.current = lexer.lex();
    }

}

class ParserAccumulator {
    public Token current, previous;
}
