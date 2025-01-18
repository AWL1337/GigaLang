package org.itmo;

public class Main {
    public static void main(String[] args) {

        String code = "a = 5; print a;";

        var codeExecutor = new GigaExecutor();

        codeExecutor.executeCode(code);
    }
}
