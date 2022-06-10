package ast;

import java.util.HashMap;

//import interpreter.ExecuteVM;
import parser.*;
	
public class AVMVisitorImpl extends AVMBaseVisitor<Void>{


    public int[] code = new int[10000];    // Each element of the array is an instruction of the program
    private int i = 0;  // Program counter
    private HashMap<String,Integer> labelAdd = new HashMap<String,Integer>();
    private HashMap<Integer,String> labelRef = new HashMap<Integer,String>();

    /**
     * labelAdd   false_branch:
     * f    | 7
     * g    | 10
     * main | 15
     * 
     * labelRef  b false_branch
     * 9    | f
     * 16   | f
     * 18   | g
     * 
     * code[9]  = labelAdd.get(labelRef.get(9))  -> code[9]  = labelAdd.get(f) -> code[9]  = 7
     * code[16] = labelAdd.get(labelRef.get(16)) -> code[16] = labelAdd.get(f) -> code[16] = 7
     * code[18] = labelAdd.get(labelRef.get(18)) -> code[18] = labelAdd.get(g) -> code[18] = 10
     * 
     * In code c'è il numero di riga in cui e' definita l'etichetta a cui sto facendo riferimento
     */
    
    @Override 
    public Void visitAssembly(AVMParser.AssemblyContext ctx) { 
    	visitChildren(ctx);
    	for (Integer refAdd: labelRef.keySet()) {
            code[refAdd]=labelAdd.get(labelRef.get(refAdd));
    	}
    	return null;
    }
    
    /**
     * TODO: aggiornare con le istruzioni di cui abbiamo bisogno
     */
    @Override 
    public Void visitInstruction(AVMParser.InstructionContext ctx) { 
    	switch (ctx.getStart().getType()) {
            case AVMLexer.LOADI:
                code[i++]= AVMParser.LOADI;
                break;
			case AVMLexer.PUSH:
				if(ctx.n != null) {
					code[i++] = AVMParser.PUSH; 
	                code[i++] = Integer.parseInt(ctx.n.getText());
				}else if(ctx.r != null){
                    //push register
                }
                /*
				else if(ctx.l != null){
					code[i++] = AVMParser.PUSH; 
		            labelRef.put(i++, ctx.l.getText());
				}*/
				break;
			case AVMLexer.POP:
				code[i++] = AVMParser.POP;
				break;
			case AVMLexer.ADD:
				code[i++] = AVMParser.ADD;
				break;
			case AVMLexer.SUB:
				code[i++] = AVMParser.SUB;
				break;
			case AVMLexer.MULT:
				code[i++] = AVMParser.MULT;
				break;
			case AVMLexer.DIV:
				code[i++] = AVMParser.DIV;
				break;
			case AVMLexer.STOREW:
				code[i++] = AVMParser.STOREW;
				break;
			case AVMLexer.LOADW:
				code[i++] = AVMParser.LOADW;
				break;
            case AVMLexer.STOREB:
                code[i++] = AVMParser.STOREB;
            case AVMLexer.LOADB:
                code[i++] = AVMParser.LOADB;
			case AVMLexer.LABEL:
				labelAdd.put(ctx.l.getText(),i);
				break;
			case AVMLexer.BRANCH:
				code[i++] = AVMParser.BRANCH;
                labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
				break;
			case AVMLexer.BRANCHEQ:
				code[i++] = AVMParser.BRANCHEQ; 
                labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
                break;
			case AVMLexer.BRANCHLESSEQ:
				code[i++] = AVMParser.BRANCHLESSEQ; 
                labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
            case AVMLexer.BRANCHLESST:
                code[i++] = AVMParser.BRANCHLESST; 
                labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
                break;
            case AVMLexer.JAL:
                code[i++] = AVMParser.JAL;
                labelRef.put(i++,(ctx.l!=null?ctx.l.getText():null));
                break;
			case AVMLexer.PRINT:
				code[i++] = AVMParser.PRINT;
				break;
			case AVMLexer.HALT:
				code[i++] = AVMParser.HALT;
				break;             
			default:
	            break;	// Invalid instruction
    	}
    	return null;
    }

}
