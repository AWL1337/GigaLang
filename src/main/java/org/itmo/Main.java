package org.itmo;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.itmo.antlr.MyLanguageLexer;
import org.itmo.antlr.MyLanguageParser;

public class Main {
    public static void main(String[] args) {
        String code = "var arr = [2, 2, 8]; print arr[0] + arr[1];";
        System.out.println("code: " + code);
        CharStream input = org.antlr.v4.runtime.CharStreams.fromString(code);
        MyLanguageLexer lexer = new MyLanguageLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MyLanguageParser parser = new MyLanguageParser(tokens);
        ParseTree tree = parser.prog();
        System.out.println("Parse Tree: " + tree.toStringTree(parser));
    }
}
