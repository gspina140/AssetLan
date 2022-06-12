package interpreter;

import parser.AVMParser;

public class ExecuteVM {

    public static final int CODESIZE = 10000;
    public static final int MEMSIZE = 10000;

    private int[] code;

    // 'a0'|'t1'|'s0'|'ra'|'fp'|'sp'|'v0'|'al'
    private int[] registers = new int[8];

    private int[] memory = new int[MEMSIZE];

    private int ip = 0; // Program counter
    private int hp = 0; // Heap pointer
    //private int rv;

    public ExecuteVM(int[] code) {
        this.code = code;
        registers[5] = MEMSIZE-1;   // sp
    }
    
    public void cpu() {
        while (true) {
            if (hp + 1 >= registers[5]) {
                System.out.println("\nError: Out of memory");
                return;
            } else {
                // String String String int String
                // Instruction $r1 $r2 offset Label
               // System.out.println(code[294]);
                int bytecode = code[ip++]; // fetch
                int v1, v2;
                int address;

                switch (bytecode) {
                    case AVMParser.LOADI:
                        v1 = code[ip++];
                        registers[code[ip++]] = v1;
                        break;
                    case AVMParser.PUSH:
                        push(registers[code[ip++]]);
                        break;
                    case AVMParser.POP:
                        pop();
                        break;
                    case AVMParser.ADD:
                        v1 = registers[code[ip++]];
                        v2 = registers[code[ip++]];
                        registers[code[ip++]] = v1 + v2;
                        //System.out.println(("Register["+code[ip-1]+"]="+(v1+v2)));  // DEBUG
                        break;
                    case AVMParser.MULT:
                        v1 = registers[code[ip++]];
                        v2 = registers[code[ip++]] * v1;
                        registers[code[ip++]] = v2;
                        break;
                    case AVMParser.DIV:
                        v1 = registers[code[ip++]];
                        v2 = registers[code[ip++]] / v1;
                        registers[code[ip++]] = v2;
                        break;
                    case AVMParser.SUB:
                        v1 = registers[code[ip++]];
                        v2 = registers[code[ip++]] - v1;
                        registers[code[ip++]] = v2;
                        break;
                    case AVMParser.STOREW: //

                        v1 = registers[code[ip++]]; // source register value
                        v2 = code[ip++];    // n
                        // registers[v2+code[ip++]]=v1;
                        memory[v2 + registers[code[ip++]]] = v1;
                        break;
                    case AVMParser.LOADW: //
                        // check if object address where we take the method label
                        // is null value (-10000)
                        if (memory[registers[5]] == -10000) {
                            System.out.println("\nError: Null pointer exception");
                            return;
                        }
                        
                        v1 = registers[code[ip++]];
                        v2 = code[ip++];
                        //System.out.println("V1- "+v1+" V2- "+v2+"\n");
                        registers[code[ip++]] = memory[v1 + v2];
                        break;
                    case AVMParser.BRANCH:
                        address = code[ip];
                        ip = address;
                        break;
                    case AVMParser.BRANCHEQ: //
                        address = code[ip++];
                        v1 = registers[code[ip++]];
                        v2 = registers[code[ip++]];
                        if (v2 == v1)
                            ip = address;
                        break;
                    case AVMParser.BRANCHLESSEQ:
                        address = code[ip++];
                        v1 = registers[code[ip++]];
                        v2 = registers[code[ip++]];
                        if (v2 <= v1)
                            ip = address;
                        break;
                    case AVMParser.BRANCHLESST:
                        address = code[ip++];
                        v1 = registers[code[ip++]];
                        v2 = registers[code[ip++]];
                        if (v2 < v1)
                            ip = address;
                        break;
                    case AVMParser.JAL:
                        /**
                         * DEBUG
                         */
                        System.out.println("Sp="+registers[5]+"\n");
                        for (int i = MEMSIZE - 1 - 28; i < MEMSIZE; i+=4) {
                            System.out.println("Stack["+i+"]="+memory[i]+"\n");
                        }
                        //System.exit(0);
                        /**
                         * DEBUG
                        */

                        registers[3] = ip + 1; // return address
                        ip = code[ip];
                        break;
                    case AVMParser.PRINT:
                        System.out.println(registers[code[ip++]]);
                        break;
                    case AVMParser.HALT:
                        // to print the result
                        System.out.println("\nResult, the wallet is: " + registers[2] + "\n");

                        /**
                         * DEBUG
                         */
                        /*
                        for (int i = 0; i < 8; i++) {
                            System.out.println("Register["+i+"]="+registers[i]+"\n");
                        }
                        for (int i = MEMSIZE - 1 - 28; i < MEMSIZE; i+=4) {
                            System.out.println("Stack["+i+"]="+memory[i]+"\n");
                        }
                        System.exit(0);
                        /**
                         * DEBUG
                        */

                        return;
                    case AVMParser.MOVE:
                        v1 = registers[code[ip++]];
                        registers[code[ip++]] = v1;
                        break;
                    case AVMParser.ADDI:
                        v1 = registers[code[ip++]];
                        v2 = code[ip++];
                        registers[code[ip++]] = v1 + v2;
                        break;
                    case AVMParser.JR:
                        address = registers[code[ip]];
                        ip = address;
                        break;
                    case AVMParser.LOADB:

                        if (memory[registers[5]] == -10000) {
                            System.out.println("\nError: Null pointer exception");
                            return;
                        }

                        v1 = registers[code[ip++]];
                        v2 = code[ip++];
                        registers[code[ip++]] = memory[v1 + v2];
                        break;
                    case AVMParser.STOREB: //

                        v1 = registers[code[ip++]];
                        v2 = code[ip++];
                        // registers[v2+code[ip++]]=v1;
                        memory[v2 + code[ip++]] = v1;
                        break;
                }
            }
        }
    }

    private void pop() {
        registers[5]+=4;
    }

    private void push(int v) {
        registers[5]-=4;
        if(registers[5] < 0){
            System.out.println("\nError: Out of memory");
            return;
        }
        memory[registers[5]] = v;
    }

}