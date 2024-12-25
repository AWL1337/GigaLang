package org.itmo.ast;

import java.util.List;

public abstract class ASTNode {}

class ProgramNode extends ASTNode {
    List<ASTNode> statements;

    public ProgramNode(List<ASTNode> statements) {
        this.statements = statements;
    }
}

class PrintNode extends ASTNode {
    ASTNode expression;

    public PrintNode(ASTNode expression) {
        this.expression = expression;
    }
}

class VarDeclarationNode extends ASTNode {
    String name;
    ASTNode value;

    public VarDeclarationNode(String name, ASTNode value) {
        this.name = name;
        this.value = value;
    }
}

class AssignmentNode extends ASTNode {
    String name;
    ASTNode value;

    public AssignmentNode(String name, ASTNode value) {
        this.name = name;
        this.value = value;
    }
}

class BinaryOperationNode extends ASTNode {
    String operator;
    ASTNode left;
    ASTNode right;

    public BinaryOperationNode(String operator, ASTNode left, ASTNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }
}

class LiteralNode extends ASTNode {
    Object value;

    public LiteralNode(Object value) {
        this.value = value;
    }
}

class ArrayNode extends ASTNode {
    List<ASTNode> elements;

    public ArrayNode(List<ASTNode> elements) {
        this.elements = elements;
    }
}
