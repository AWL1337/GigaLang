package org.itmo;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.itmo.VM.instructions.Instruction;
import org.itmo.antlr.GigaLangLexer;
import org.itmo.antlr.GigaLangParser;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        String input = "bb == 0 || (a > 0 && q < 1)";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator listener = new ByteCodeGenerator();
        ParseTreeWalker.DEFAULT.walk(listener, tree);

        List<Instruction> instructions = listener.getInstructions();
        for (Instruction instruction : instructions) {
            System.out.println(instruction);
        }
    }
}
