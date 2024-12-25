package org.itmo.ast;

import org.itmo.antlr.MyLanguageBaseVisitor;
import org.itmo.antlr.MyLanguageParser;

import java.util.stream.Collectors;

public class ASTBuilder extends MyLanguageBaseVisitor<ASTNode> {

    @Override
    public ASTNode visitProg(MyLanguageParser.ProgContext ctx) {
        return new ProgramNode(ctx.statement().stream()
                .map(this::visit)
                .collect(Collectors.toList()));
    }

    @Override
    public ASTNode visitPrintStatement(MyLanguageParser.PrintStatementContext ctx) {
        return new PrintNode(visit(ctx.expr()));
    }

    @Override
    public ASTNode visitVarDeclaration(MyLanguageParser.VarDeclarationContext ctx) {
        return new VarDeclarationNode(ctx.ID().getText(), visit(ctx.expr()));
    }

    @Override
    public ASTNode visitAssignment(MyLanguageParser.AssignmentContext ctx) {
        return new AssignmentNode(ctx.ID().getText(), visit(ctx.expr(0)));
    }


    @Override
    public ASTNode visitExpr(MyLanguageParser.ExprContext ctx) {
        if (ctx.baseExpr().size() == 1) {
            return visit(ctx.baseExpr(0));
        }

        ASTNode left = visit(ctx.baseExpr(0));
        for (int i = 1; i < ctx.baseExpr().size(); i++) {
            String operator = ctx.getChild(2 * i - 1).getText();
            ASTNode right = visit(ctx.baseExpr(i));
            left = new BinaryOperationNode(operator, left, right);
        }
        return left;
    }

    @Override
    public ASTNode visitAtom(MyLanguageParser.AtomContext ctx) {
        if (ctx.STRING() != null) {
            return new LiteralNode(ctx.STRING().getText());
        }
        if (ctx.INT() != null) {
            return new LiteralNode(Integer.parseInt(ctx.INT().getText()));
        }
        if (ctx.ID() != null) {
            return new LiteralNode(ctx.ID().getText());
        }
        if (ctx.arrayCreation() != null) {
            return visit(ctx.arrayCreation());
        }
        return visit(ctx.expr());
    }

    @Override
    public ASTNode visitArrayCreation(MyLanguageParser.ArrayCreationContext ctx) {
        return new ArrayNode(ctx.expr().stream()
                .map(this::visit)
                .collect(Collectors.toList()));
    }
}
