package org.itmo;

public class Main {
    public static void main(String[] args) {

        String code = "a = 5;";

        var codeExecutor = new GigaExecutor();

        codeExecutor.executeCode(code);
    }
}
