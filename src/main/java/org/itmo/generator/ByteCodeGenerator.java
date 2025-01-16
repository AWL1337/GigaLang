package org.itmo.generator;

import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.itmo.VM.instructions.Instruction;
import org.itmo.VM.instructions.InstructionType;
import org.itmo.antlr.GigaLangBaseListener;
import org.itmo.antlr.GigaLangParser;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ByteCodeGenerator extends GigaLangBaseListener {
    private final List<Instruction> instructions = new ArrayList<>();

    @Override
    public void exitVariableAssignation(GigaLangParser.VariableAssignationContext ctx) {
        String variableName = ctx.ID().getText();
        Instruction store = Instruction.builder()
                .type(InstructionType.STORE)
                .name(variableName)
                .build();
        instructions.add(store);
    }

    @Override
    public void exitArrayAssignation(GigaLangParser.ArrayAssignationContext ctx) {
        String variableName = ctx.ID().getText();
        Long index = Long.parseLong(ctx.getText());

        Instruction store = Instruction.builder()
                .type(InstructionType.ARRAY_STORE)
                .name(variableName)
                .value(index)
                .build();
        instructions.add(store);
    }

    //statements
    @Override
    public void enterPresetArrayDeclaration(GigaLangParser.PresetArrayDeclarationContext ctx) {
        String variableName = ctx.ID().getText();
        List<String> split = List.of(ctx.list().getText().split(","));

        Instruction putSize = Instruction.builder()
                .type(InstructionType.PUSH)
                .value((long) split.size())
                .build();

        instructions.add(putSize);

        Instruction createArray = Instruction.builder()
                .type(InstructionType.ARRAY_CREATE)
                .name(variableName)
                .build();

        instructions.add(createArray);

        for (int i = 0; i < split.size(); i++) {
            Instruction pushVal = Instruction.builder()
                    .type(InstructionType.PUSH)
                    .value(Long.parseLong(split.get(i)))
                    .build();

            instructions.add(pushVal);

            Instruction arrayStore = Instruction.builder()
                    .type(InstructionType.ARRAY_STORE)
                    .value((long)i)
                    .build();

            instructions.add(arrayStore);
        }
    }

    @Override
    public void exitVariableDeclaration(GigaLangParser.VariableDeclarationContext ctx) {

        // Генерируем инструкцию STORE для сохранения результата в переменную
        String variableName = ctx.ID().getText();
        Instruction storeVariable = Instruction.builder()
                .type(InstructionType.STORE)
                .name(variableName)
                .build();
        instructions.add(storeVariable);
    }

    @Override
    public void exitArrayDeclaration(GigaLangParser.ArrayDeclarationContext ctx) {
        String variableName = ctx.ID().getText();
        Instruction storeVariable = Instruction.builder()
                .type(InstructionType.ARRAY_CREATE)
                .name(variableName)
                .build();
        instructions.add(storeVariable);
    }

    @Override
    public void enterIntLiteral(GigaLangParser.IntLiteralContext ctx) {
        long value = Long.parseLong(ctx.INT().getText());
        Instruction push = Instruction.builder()
                .type(InstructionType.PUSH)
                .value(value)
                .build();
        instructions.add(push);
    }

    @Override
    public void enterVariable(GigaLangParser.VariableContext ctx) {
        String variableName = ctx.ID().getText();
        Instruction load = Instruction.builder()
                .type(InstructionType.LOAD_VAR)
                .name(variableName)
                .build();
        instructions.add(load);
    }

    @Override
    public void enterReadArray(GigaLangParser.ReadArrayContext ctx) {
        String variableName = ctx.ID().getText();
        Long index = Long.parseLong(ctx.getText());

        Instruction read = Instruction.builder()
                .type(InstructionType.ARRAY_LOAD)
                .name(variableName)
                .value(index)
                .build();
        instructions.add(read);
    }

    //math
    @Override
    public void exitPowExpression(GigaLangParser.PowExpressionContext ctx) {

        Instruction pow = Instruction.builder()
                .type(InstructionType.POW)
                .build();
        instructions.add(pow);
    }

    @Override
    public void exitMulDivExpression(GigaLangParser.MulDivExpressionContext ctx) {

        if (ctx.MULT() != null) {
            Instruction mul = Instruction.builder()
                    .type(InstructionType.MUL)
                    .build();
            instructions.add(mul);
        } else if (ctx.DIV() != null) {
            Instruction div = Instruction.builder()
                    .type(InstructionType.DIV)
                    .build();
            instructions.add(div);
        }
    }

    @Override
    public void exitAddSubExpression(GigaLangParser.AddSubExpressionContext ctx) {

        if (ctx.PLUS() != null) {
            Instruction add = Instruction.builder()
                    .type(InstructionType.ADD)
                    .build();
            instructions.add(add);
        } else if (ctx.MINUS() != null) {
            Instruction sub = Instruction.builder()
                    .type(InstructionType.SUB)
                    .build();
            instructions.add(sub);
        }
    }

    @Override
    public void exitModExpression(GigaLangParser.ModExpressionContext ctx) {
        Instruction mod = Instruction.builder()
                .type(InstructionType.MOD)
                .build();
        instructions.add(mod);
    }

    //relational
    @Override
    public void exitGtExpression(GigaLangParser.GtExpressionContext ctx) {
        Instruction gt = Instruction.builder()
                .type(InstructionType.GT)
                .build();
        instructions.add(gt);
    }

    @Override
    public void exitGqExpression(GigaLangParser.GqExpressionContext ctx) {
        Instruction ge = Instruction.builder()
                .type(InstructionType.GE)
                .build();
        instructions.add(ge);
    }

    @Override
    public void exitLtExpression(GigaLangParser.LtExpressionContext ctx) {
        Instruction lt = Instruction.builder()
                .type(InstructionType.LT)
                .build();
        instructions.add(lt);
    }

    @Override
    public void exitLqExpression(GigaLangParser.LqExpressionContext ctx) {
        Instruction le = Instruction.builder()
                .type(InstructionType.LE)
                .build();
        instructions.add(le);
    }

    @Override
    public void exitEqExpression(GigaLangParser.EqExpressionContext ctx) {
        Instruction eq = Instruction.builder()
                .type(InstructionType.EQ)
                .build();
        instructions.add(eq);
    }

    @Override
    public void exitNqExpression(GigaLangParser.NqExpressionContext ctx) {
        Instruction ne = Instruction.builder()
                .type(InstructionType.NE)
                .build();
        instructions.add(ne);
    }

    //boolean
    @Override
    public void exitAndExpression(GigaLangParser.AndExpressionContext ctx) {
        Instruction and = Instruction.builder()
                .type(InstructionType.AND)
                .build();
        instructions.add(and);
    }

    @Override
    public void exitOrExpression(GigaLangParser.OrExpressionContext ctx) {
        Instruction or = Instruction.builder()
                .type(InstructionType.OR)
                .build();
        instructions.add(or);
    }

    @Override
    public void exitNotExpression(GigaLangParser.NotExpressionContext ctx) {
        Instruction not = Instruction.builder()
                .type(InstructionType.NOT)
                .build();
        instructions.add(not);
    }

    //if
    @Override
    public void enterIfStatement(GigaLangParser.IfStatementContext ctx) {
        // visitor

    }

}


