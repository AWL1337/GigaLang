package org.itmo;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.itmo.VM.VirtualMachine;
import org.itmo.antlr.GigaLangLexer;
import org.itmo.antlr.GigaLangParser;
import org.itmo.generator.ByteCodeGenerator;

public class GigaExecutor {
    private final ByteCodeGenerator byteCodeGenerator = new ByteCodeGenerator();

    private final VirtualMachine virtualMachine = new VirtualMachine();

    public void executeCode(String code) {
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(code));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        byteCodeGenerator.visit(tree);

        virtualMachine.interpret(byteCodeGenerator.getInstructions());
    }
}
