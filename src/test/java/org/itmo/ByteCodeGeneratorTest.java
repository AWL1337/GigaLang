package org.itmo;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.itmo.VM.instructions.Instruction;
import org.itmo.VM.instructions.InstructionType;
import org.itmo.antlr.GigaLangLexer;
import org.itmo.antlr.GigaLangParser;
import org.itmo.generator.ByteCodeGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ByteCodeGeneratorTest {

    @Test
    public void declarationVariableTest() {
        String input = "var a = 3 + bbb;";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(tree);

        List<Instruction> expected = List.of(
                new Instruction(InstructionType.PUSH, null, 3L),
                new Instruction(InstructionType.LOAD_VAR, "bbb", null),
                new Instruction(InstructionType.ADD, null, null),
                new Instruction(InstructionType.STORE, "a", null)
        );

        assertEquals(expected, generator.getInstructions());
    }

    @Test
    public void declarationVariableExpressionTest() {
        String input = "var a = 3 + (3 - 2)**4 * 9;";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(tree);

        List<Instruction> expected = List.of(
                new Instruction(InstructionType.PUSH, null, 3L),
                new Instruction(InstructionType.PUSH, null, 3L),
                new Instruction(InstructionType.PUSH, null, 2L),
                new Instruction(InstructionType.SUB, null, null),
                new Instruction(InstructionType.PUSH, null, 4L),
                new Instruction(InstructionType.POW, null, null),
                new Instruction(InstructionType.PUSH, null, 9L),
                new Instruction(InstructionType.MUL, null, null),
                new Instruction(InstructionType.ADD, null, null),
                new Instruction(InstructionType.STORE, "a", null)
        );

        assertEquals(expected, generator.getInstructions());
    }

    @Test
    public void assignationVariableTest() {
        String input = "a = 3;";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(tree);

        List<Instruction> expected = List.of(
                new Instruction(InstructionType.PUSH, null, 3L),
                new Instruction(InstructionType.STORE, "a", null)
        );

        assertEquals(expected, generator.getInstructions());
    }

    @Test
    public void assignationVariableExpressionTest() {
        String input = "a = 3 + (3 - 2)**4 * 9;";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(tree);

        List<Instruction> expected = List.of(
                new Instruction(InstructionType.PUSH, null, 3L),
                new Instruction(InstructionType.PUSH, null, 3L),
                new Instruction(InstructionType.PUSH, null, 2L),
                new Instruction(InstructionType.SUB, null, null),
                new Instruction(InstructionType.PUSH, null, 4L),
                new Instruction(InstructionType.POW, null, null),
                new Instruction(InstructionType.PUSH, null, 9L),
                new Instruction(InstructionType.MUL, null, null),
                new Instruction(InstructionType.ADD, null, null),
                new Instruction(InstructionType.STORE, "a", null)
        );

        assertEquals(expected, generator.getInstructions());
    }

    @Test
    public void declarationArrayTest() {
        String input = "arr ar = new[4];";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(tree);

        List<Instruction> expected = List.of(
                new Instruction(InstructionType.PUSH, null, 4L),
                new Instruction(InstructionType.ARRAY_CREATE, "ar", null)
        );

        assertEquals(expected, generator.getInstructions());
    }

    @Test
    public void declarationArrayExpressionTest() {
        String input = "arr ar = new[4 + 8 * 2];";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(tree);

        List<Instruction> expected = List.of(
                new Instruction(InstructionType.PUSH, null, 4L),
                new Instruction(InstructionType.PUSH, null, 8L),
                new Instruction(InstructionType.PUSH, null, 2L),
                new Instruction(InstructionType.MUL, null, null),
                new Instruction(InstructionType.ADD, null, null),
                new Instruction(InstructionType.ARRAY_CREATE, "ar", null)
        );

        assertEquals(expected, generator.getInstructions());
    }

    @Test
    public void presetDeclarationArrayTest() {
        String input = "arr a = [1, 2* 2, 5];";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(tree);

        List<Instruction> expected = List.of(
                new Instruction(InstructionType.PUSH, null, 3L),
                new Instruction(InstructionType.ARRAY_CREATE, "a", null),
                new Instruction(InstructionType.PUSH, null, 0L),
                new Instruction(InstructionType.PUSH, null, 1L),
                new Instruction(InstructionType.ARRAY_STORE, "a", null),
                new Instruction(InstructionType.PUSH, null, 1L),
                new Instruction(InstructionType.PUSH, null, 2L),
                new Instruction(InstructionType.PUSH, null, 2L),
                new Instruction(InstructionType.MUL, null, null),
                new Instruction(InstructionType.ARRAY_STORE, "a", null),
                new Instruction(InstructionType.PUSH, null, 2L),
                new Instruction(InstructionType.PUSH, null, 5L),
                new Instruction(InstructionType.ARRAY_STORE, "a", null)
        );

        assertEquals(expected, generator.getInstructions());
    }

    @Test
    public void readArrayTest() {
        String input = "var id = a[4 + br / 2];";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(tree);

        List<Instruction> expected = List.of(
                new Instruction(InstructionType.PUSH, null, 4L),
                new Instruction(InstructionType.LOAD_VAR, "br", null),
                new Instruction(InstructionType.PUSH, null, 2L),
                new Instruction(InstructionType.DIV, null, null),
                new Instruction(InstructionType.ADD, null, null),
                new Instruction(InstructionType.ARRAY_LOAD, "a", null),
                new Instruction(InstructionType.STORE, "id", null)
        );

        assertEquals(expected, generator.getInstructions());
    }

    @Test
    public void writeArrayTest() {
        String input = "a[4 + br / 2] = id;";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(tree);

        List<Instruction> expected = List.of(
                new Instruction(InstructionType.PUSH, null, 4L),
                new Instruction(InstructionType.LOAD_VAR, "br", null),
                new Instruction(InstructionType.PUSH, null, 2L),
                new Instruction(InstructionType.DIV, null, null),
                new Instruction(InstructionType.ADD, null, null),
                new Instruction(InstructionType.LOAD_VAR, "id", null),
                new Instruction(InstructionType.ARRAY_STORE, "a", null)
        );

        assertEquals(expected, generator.getInstructions());
    }

    @Test
    public void relationalExpressionTest() {
        String input = "a > 1";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(tree);

        List<Instruction> expected = List.of(
                new Instruction(InstructionType.LOAD_VAR, "a", null),
                new Instruction(InstructionType.PUSH, null, 1L),
                new Instruction(InstructionType.GT, null, null)
        );

        assertEquals(expected, generator.getInstructions());
    }

    @Test
    public void booleanExpressionTest() {
        String input = "(a > 1) || (b == 0)";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(tree);

        List<Instruction> expected = List.of(
                new Instruction(InstructionType.LOAD_VAR, "a", null),
                new Instruction(InstructionType.PUSH, null, 1L),
                new Instruction(InstructionType.GT, null, null),
                new Instruction(InstructionType.LOAD_VAR, "b", null),
                new Instruction(InstructionType.PUSH, null, 0L),
                new Instruction(InstructionType.EQ, null, null),
                new Instruction(InstructionType.OR, null, null)
        );

        assertEquals(expected, generator.getInstructions());
    }

    @Test
    public void ifExpressionTest() {
        String input = "if (a > 0) {var c = 5;};";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(tree);

        List<Instruction> expected = List.of(
                new Instruction(InstructionType.LOAD_VAR, "a", null),
                new Instruction(InstructionType.PUSH, null, 0L),
                new Instruction(InstructionType.GT, null, null),
                new Instruction(InstructionType.JUMP_IF_FALSE, "0", null),
                new Instruction(InstructionType.PUSH, null, 5L),
                new Instruction(InstructionType.STORE, "c", null),
                new Instruction(InstructionType.LABEL, "0", null)
        );

        assertEquals(expected, generator.getInstructions());
    }

    @Test
    public void ifElseExpressionTest() {
        String input = "if (a > 0) {var c = 5;} else {var c = 4;};";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(tree);

        List<Instruction> expected = List.of(
                new Instruction(InstructionType.LOAD_VAR, "a", null),
                new Instruction(InstructionType.PUSH, null, 0L),
                new Instruction(InstructionType.GT, null, null),
                new Instruction(InstructionType.JUMP_IF_FALSE, "0", null),
                new Instruction(InstructionType.PUSH, null, 5L),
                new Instruction(InstructionType.STORE, "c", null),
                new Instruction(InstructionType.JUMP, "1", null),
                new Instruction(InstructionType.LABEL, "0", null),
                new Instruction(InstructionType.PUSH, null, 4L),
                new Instruction(InstructionType.STORE, "c", null),
                new Instruction(InstructionType.LABEL, "1", null)
        );

        assertEquals(expected, generator.getInstructions());
    }

    @Test
    public void whileExpressionTest() {
        String input = "var a = 3; while (a < 6) {a = a + 1;}";
        GigaLangLexer lexer = new GigaLangLexer(CharStreams.fromString(input));
        GigaLangParser parser = new GigaLangParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(tree);

        List<Instruction> expected = List.of(
                new Instruction(InstructionType.PUSH, null, 3L),
                new Instruction(InstructionType.STORE, "a", null),
                new Instruction(InstructionType.LABEL, "0", null),
                new Instruction(InstructionType.LOAD_VAR, "a", null),
                new Instruction(InstructionType.PUSH, null, 6L),
                new Instruction(InstructionType.LT, null, null),
                new Instruction(InstructionType.JUMP_IF_FALSE, "1", null),
                new Instruction(InstructionType.LOAD_VAR, "a", null),
                new Instruction(InstructionType.PUSH, null, 1L),
                new Instruction(InstructionType.ADD, null, null),
                new Instruction(InstructionType.STORE, "a", null),
                new Instruction(InstructionType.JUMP, "0", null),
                new Instruction(InstructionType.LABEL, "1", null)
        );

        assertEquals(expected, generator.getInstructions());
    }
}
