package org.itmo;

public class Main {
    public static void main(String[] args) {

        String code = "a = 5; while (a < 7) {print a; a = a + 1;}";

        var codeExecutor = new GigaExecutor();

        codeExecutor.executeCode(code);
    }
}
