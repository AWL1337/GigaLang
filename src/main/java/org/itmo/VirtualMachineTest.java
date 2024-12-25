package org.itmo;

import org.itmo.VM.VirtualMachine;
import org.itmo.VM.instructions.Instruction;
import org.itmo.VM.instructions.InstructionType;


public class VirtualMachineTest {
    public static void main(String[] args) {
        VirtualMachine vm = new VirtualMachine();

        /*
        * a = 30
        * b = 12
        * add = a + b
        * sub = a - b
        * MUL = a * b
        * DIV = a / b
        * MOD = a % b
        * b = [1,2,3,4]
        * */
        vm.execute(Instruction.builder().type(InstructionType.PUSH)
                .value(30L).build());
        vm.execute(Instruction.builder().type(InstructionType.STORE)
                .name("a").build());
        vm.execute(Instruction.builder().type(InstructionType.PUSH)
                .value(12L).build());
        vm.execute(Instruction.builder().type(InstructionType.STORE)
                .name("b").build());
        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("a").build());
        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("b").build());
        vm.execute(Instruction.builder().type(InstructionType.ADD)
                .build());
        vm.execute(Instruction.builder().type(InstructionType.STORE)
                .name("add").build());

        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("a").build());
        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("b").build());
        vm.execute(Instruction.builder().type(InstructionType.SUB)
                .build());
        vm.execute(Instruction.builder().type(InstructionType.STORE)
                .name("sub").build());

        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("a").build());
        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("b").build());
        vm.execute(Instruction.builder().type(InstructionType.MUL)
                .build());
        vm.execute(Instruction.builder().type(InstructionType.STORE)
                .name("mul").build());

        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("a").build());
        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("b").build());
        vm.execute(Instruction.builder().type(InstructionType.DIV)
                .build());
        vm.execute(Instruction.builder().type(InstructionType.STORE)
                .name("div").build());

        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("a").build());
        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("b").build());
        vm.execute(Instruction.builder().type(InstructionType.MOD)
                .build());
        vm.execute(Instruction.builder().type(InstructionType.STORE)
                .name("mod").build());

        vm.execute(Instruction.builder().type(InstructionType.PUSH)
                .value(10L).build());
        vm.execute(Instruction.builder().type(InstructionType.ARRAY_CREATE)
                .name("list").build());

        for (int i = 0; i < 10; i++){
            vm.execute(Instruction.builder().type(InstructionType.PUSH)
                    .value((long)i + 10).build());
            vm.execute(Instruction.builder().type(InstructionType.ARRAY_STORE)
                    .name("list").index(i).build());
        }

        for (int i = 0; i < 10; i++){
            vm.execute(Instruction.builder().type(InstructionType.ARRAY_LOAD)
                    .name("list").index(i).build());
            System.out.println(vm.stack.pop());
        }
        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("mod").build());
        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("div").build());
        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("mul").build());
        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("sub").build());
        vm.execute(Instruction.builder().type(InstructionType.LOAD_VAR)
                .name("add").build());

        System.out.println(vm.stack);
        System.out.println("ADD: " + vm.stack.pop());
        System.out.println("SUB: " + vm.stack.pop());
        System.out.println("MUL: " + vm.stack.pop());
        System.out.println("DIV: " + vm.stack.pop());
        System.out.println("MOD: " + vm.stack.pop());
    }
}