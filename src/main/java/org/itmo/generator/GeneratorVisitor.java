package org.itmo.generator;

import lombok.Getter;
import org.itmo.VM.instructions.Instruction;
import org.itmo.VM.instructions.InstructionType;
import org.itmo.antlr.GigaLangBaseVisitor;
import org.itmo.antlr.GigaLangParser;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GeneratorVisitor extends GigaLangBaseVisitor<Void> {
    private final List<Instruction> instructions = new ArrayList<>();

    @Override
    public Void visitVariableAssignation(GigaLangParser.VariableAssignationContext ctx) {
        String variableName = ctx.ID().getText();
        instructions.add(Instruction.builder()
                .type(InstructionType.STORE)
                .name(variableName)
                .build());
        return null;
    }

    @Override
    public Void visitArrayAssignation(GigaLangParser.ArrayAssignationContext ctx) {
        String variableName = ctx.ID().getText();
        Long index = Long.parseLong(ctx.expression());

        instructions.add(Instruction.builder()
                .type(InstructionType.ARRAY_STORE)
                .name(variableName)
                .value(index)
                .build());
        return null;
    }

    @Override
    public Void visitPresetArrayDeclaration(GigaLangParser.PresetArrayDeclarationContext ctx) {
        String variableName = ctx.ID().getText();
        List<String> split = List.of(ctx.list().getText().split(","));

        instructions.add(Instruction.builder()
                .type(InstructionType.PUSH)
                .value((long) split.size())
                .build());

        instructions.add(Instruction.builder()
                .type(InstructionType.ARRAY_CREATE)
                .name(variableName)
                .build());

        for (int i = 0; i < split.size(); i++) {
            instructions.add(Instruction.builder()
                    .type(InstructionType.PUSH)
                    .value(Long.parseLong(split.get(i)))
                    .build());

            instructions.add(Instruction.builder()
                    .type(InstructionType.ARRAY_STORE)
                    .value((long) i)
                    .build());
        }
        return null;
    }

    @Override
    public Void visitVariableDeclaration(GigaLangParser.VariableDeclarationContext ctx) {
        String variableName = ctx.ID().getText();
        instructions.add(Instruction.builder()
                .type(InstructionType.STORE)
                .name(variableName)
                .build());
        return null;
    }

    @Override
    public Void visitArrayDeclaration(GigaLangParser.ArrayDeclarationContext ctx) {
        String variableName = ctx.ID().getText();
        instructions.add(Instruction.builder()
                .type(InstructionType.ARRAY_CREATE)
                .name(variableName)
                .build());
        return null;
    }

    @Override
    public Void visitIntLiteral(GigaLangParser.IntLiteralContext ctx) {
        long value = Long.parseLong(ctx.INT().getText());
        instructions.add(Instruction.builder()
                .type(InstructionType.PUSH)
                .value(value)
                .build());
        return null;
    }

    @Override
    public Void visitVariable(GigaLangParser.VariableContext ctx) {
        String variableName = ctx.ID().getText();
        instructions.add(Instruction.builder()
                .type(InstructionType.LOAD_VAR)
                .name(variableName)
                .build());
        return null;
    }

    @Override
    public Void visitReadArray(GigaLangParser.ReadArrayContext ctx) {
        String variableName = ctx.ID().getText();
        Long index = Long.parseLong(ctx.expression().getText());

        instructions.add(Instruction.builder()
                .type(InstructionType.ARRAY_LOAD)
                .name(variableName)
                .value(index)
                .build());
        return null;
    }

    @Override
    public Void visitPowExpression(GigaLangParser.PowExpressionContext ctx) {
        visit(ctx.expression(0));
        visit(ctx.expression(1));

        instructions.add(Instruction.builder()
                .type(InstructionType.POW)
                .build());
        return null;
    }

    @Override
    public Void visitMulDivExpression(GigaLangParser.MulDivExpressionContext ctx) {
        visit(ctx.expression(0));
        visit(ctx.expression(1));

        if (ctx.MULT() != null) {
            instructions.add(Instruction.builder()
                    .type(InstructionType.MUL)
                    .build());
        } else if (ctx.DIV() != null) {
            instructions.add(Instruction.builder()
                    .type(InstructionType.DIV)
                    .build());
        }
        return null;
    }

    @Override
    public Void visitAddSubExpression(GigaLangParser.AddSubExpressionContext ctx) {
        visit(ctx.expression(0));
        visit(ctx.expression(1));

        if (ctx.PLUS() != null) {
            instructions.add(Instruction.builder()
                    .type(InstructionType.ADD)
                    .build());
        } else if (ctx.MINUS() != null) {
            instructions.add(Instruction.builder()
                    .type(InstructionType.SUB)
                    .build());
        }
        return null;
    }

    @Override
    public Void visitModExpression(GigaLangParser.ModExpressionContext ctx) {
        visit(ctx.expression(0));
        visit(ctx.expression(1));

        instructions.add(Instruction.builder()
                .type(InstructionType.MOD)
                .build());
        return null;
    }

}