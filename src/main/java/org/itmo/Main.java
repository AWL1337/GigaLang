package org.itmo;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.itmo.VM.instructions.Instruction;
import org.itmo.antlr.GigaLangLexer;
import org.itmo.antlr.GigaLangParser;
import org.itmo.generator.ByteCodeGenerator;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        String input = "if (a > 0) { var b = 3\n }\n";
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
