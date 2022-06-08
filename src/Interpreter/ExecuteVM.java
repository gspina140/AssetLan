package interpreter;

import parser.AVMParser;

public class ExecuteVM {
    
    public static final int CODESIZE = 10000;
    public static final int MEMSIZE = 10000;
 
    private int[] code;
    private int[] memory = new int[MEMSIZE];
    
    private int ip = 0;         // Program counter
    private int sp = MEMSIZE;   // Stack Pointer
    private int hp = 0;         // Heap pointer
    private int fp = MEMSIZE;   // Frame pointer
    private int ra;             // Return address           
    private int rv;
    
    public ExecuteVM(int[] code) {
      this.code = code;
    }
    
    /**
     * TODO: aggiorna rispettivamente alla nostra codegen
     */
    public void cpu() {
      while ( true ) {
    	if(hp+1>=sp) {
    		System.out.println("\nError: Out of memory");
            return;
    	}
    	else {
            // String       String  String  int     String
            // Instruction  $r1     $r2     offset  Label
    		int bytecode = code[ip++]; // fetch
            int v1,v2;
            int address;
            switch ( bytecode ) {
              case AVMParser.PUSH:
                push( code[ip++] );
                break;
              case AVMParser.POP:
                pop();
                break;
              case AVMParser.ADD :
                v1=pop();
                v2=pop();
                push(v2 + v1);
                break;
              case AVMParser.MULT :
                v1=pop();
                v2=pop();
                push(v2 * v1);
                break;
              case AVMParser.DIV :
                v1=pop();
                v2=pop();
                push(v2 / v1);
                break;
              case AVMParser.SUB :
                v1=pop();
                v2=pop();
                push(v2 - v1);
                break;
              case AVMParser.STOREW : //
                address = pop();
                memory[address] = pop();    
                break;
              case AVMParser.LOADW : //
            	// check if object address where we take the method label
            	// is null value (-10000)
                if (memory[sp] == -10000) {
                	System.out.println("\nError: Null pointer exception");
                	return;
                }  
                push(memory[pop()]);
                break;
              case AVMParser.BRANCH : 
                address = code[ip];
                ip = address;
                break;
              case AVMParser.BRANCHEQ : //
                address = code[ip++];
                v1=pop();
                v2=pop();
                if (v2 == v1) ip = address;
                break;
              case AVMParser.BRANCHLESSEQ :
                address = code[ip++];
                v1=pop();
                v2=pop();
                if (v2 <= v1) ip = address;
                break;
              case AVMParser.JS : //	
            	address = pop();
                ra = ip;
                ip = address;
                break;
             case AVMParser.STORERA : //
                ra=pop();
                break;
             case AVMParser.LOADRA : //
                push(ra);
                break;
             case AVMParser.STORERV : //
                rv=pop();
                break;
             case AVMParser.LOADRV : //
                push(rv);
                break;
             case AVMParser.LOADFP : //
                push(fp);
                break;
             case AVMParser.STOREFP : //
                fp=pop();
                break;
             case AVMParser.COPYFP : //
                fp=sp;
                break;
             case AVMParser.STOREHP : //
                hp=pop();
                break;
             case AVMParser.LOADHP : //
                push(hp);
                break;
             case AVMParser.PRINT :
                System.out.println((sp<MEMSIZE)?memory[sp]:"Empty stack!");
                break;
             case AVMParser.HALT :
            	//to print the result 
             	System.out.println("\nResult: " + memory[sp] + "\n");
             	return;
            }
    	} 
      }
    }
    
    private int pop() {
      return memory[sp++];
    }

    private void push(int v) {
      memory[--sp] = v;
    }
    
}