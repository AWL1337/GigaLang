package org.itmo.generator;

import lombok.Getter;
import org.itmo.VM.instructions.Instruction;
import org.itmo.VM.instructions.InstructionType;
import org.itmo.antlr.GigaLangBaseVisitor;
import org.itmo.antlr.GigaLangParser;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ByteCodeGenerator extends GigaLangBaseVisitor<Void> {
    private final List<Instruction> instructions = new ArrayList<>();

    @Override
    public Void visitVariableAssignation(GigaLangParser.VariableAssignationContext ctx) {
        visit(ctx.expression());
        String variableName = ctx.ID().getText();
        instructions.add(Instruction.builder()
                .type(InstructionType.STORE)
                .name(variableName)
                .build());
        return null;
    }

    @Override
    public Void visitArrayAssignation(GigaLangParser.ArrayAssignationContext ctx) {
        ctx.expression().forEach(this::visit);
        String variableName = ctx.ID().getText();
        instructions.add(Instruction.builder()
                .type(InstructionType.ARRAY_STORE)
                .name(variableName)
                .build());
        return null;
    }

    @Override
    public Void visitPresetArrayDeclaration(GigaLangParser.PresetArrayDeclarationContext ctx) {
        String variableName = ctx.ID().getText();
        int size = ctx.expressionList().expression().size();

        instructions.add(Instruction.builder()
                .type(InstructionType.PUSH)
                .value((long) size)
                .build());

        instructions.add(Instruction.builder()
                .type(InstructionType.ARRAY_CREATE)
                .name(variableName)
                .build());

        var list = ctx.expressionList().expression();

        for (int i = 0; i < list.size(); i++) {
            instructions.add(Instruction.builder()
                    .type(InstructionType.PUSH)
                    .value((long)i)
                    .build());

            visit(list.get(i));

            instructions.add(Instruction.builder()
                    .type(InstructionType.ARRAY_STORE)
                    .name(variableName)
                    .build());
        }
        return null;
    }

    @Override
    public Void visitVariableDeclaration(GigaLangParser.VariableDeclarationContext ctx) {
        visit(ctx.expression());
        String variableName = ctx.ID().getText();
        instructions.add(Instruction.builder()
                .type(InstructionType.STORE)
                .name(variableName)
                .build());
        return null;
    }

    @Override
    public Void visitArrayDeclaration(GigaLangParser.ArrayDeclarationContext ctx) {
        visit(ctx.expression());
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
        visit(ctx.expression());
        String variableName = ctx.ID().getText();

        instructions.add(Instruction.builder()
                .type(InstructionType.ARRAY_LOAD)
                .name(variableName)
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

    //relational
    @Override
    public Void visitGtExpression(GigaLangParser.GtExpressionContext ctx) {
        visit(ctx.expression(0));
        visit(ctx.expression(1));
        Instruction gt = Instruction.builder()
                .type(InstructionType.GT)
                .build();
        instructions.add(gt);
        return null;
    }

    @Override
    public Void visitGqExpression(GigaLangParser.GqExpressionContext ctx) {
        visit(ctx.expression(0));
        visit(ctx.expression(1));
        Instruction ge = Instruction.builder()
                .type(InstructionType.GE)
                .build();
        instructions.add(ge);
        return null;
    }

    @Override
    public Void visitLtExpression(GigaLangParser.LtExpressionContext ctx) {
        visit(ctx.expression(0));
        visit(ctx.expression(1));
        Instruction lt = Instruction.builder()
                .type(InstructionType.LT)
                .build();
        instructions.add(lt);
        return null;
    }

    @Override
    public Void visitLqExpression(GigaLangParser.LqExpressionContext ctx) {
        visit(ctx.expression(0));
        visit(ctx.expression(1));
        Instruction le = Instruction.builder()
                .type(InstructionType.LE)
                .build();
        instructions.add(le);
        return null;
    }

    @Override
    public Void visitEqExpression(GigaLangParser.EqExpressionContext ctx) {
        visit(ctx.expression(0));
        visit(ctx.expression(1));
        Instruction eq = Instruction.builder()
                .type(InstructionType.EQ)
                .build();
        instructions.add(eq);
        return null;
    }

    @Override
    public Void visitNqExpression(GigaLangParser.NqExpressionContext ctx) {
        visit(ctx.expression(0));
        visit(ctx.expression(1));
        Instruction ne = Instruction.builder()
                .type(InstructionType.NE)
                .build();
        instructions.add(ne);
        return null;
    }

    //boolean
    @Override
    public Void visitAndExpression(GigaLangParser.AndExpressionContext ctx) {
        visit(ctx.booleanExpression(0));
        visit(ctx.booleanExpression(1));

        Instruction and = Instruction.builder()
                .type(InstructionType.AND)
                .build();
        instructions.add(and);
        return null;
    }

    @Override
    public Void visitOrExpression(GigaLangParser.OrExpressionContext ctx) {
        visit(ctx.booleanExpression(0));
        visit(ctx.booleanExpression(1));
        Instruction or = Instruction.builder()
                .type(InstructionType.OR)
                .build();
        instructions.add(or);
        return null;
    }

    @Override
    public Void visitNotExpression(GigaLangParser.NotExpressionContext ctx) {
        visit(ctx.booleanExpression());
        Instruction not = Instruction.builder()
                .type(InstructionType.NOT)
                .build();
        instructions.add(not);
        return null;
    }
}