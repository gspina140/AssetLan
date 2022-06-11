package ast;

import java.util.ArrayList;
import java.util.HashMap;

import interpreter.ExecuteVM;
import parser.*;

public class AVMVisitorImpl extends AVMBaseVisitor<Void> {

    public int[] code = new int[ExecuteVM.CODESIZE]; // Each element of the array is an instruction of the program
    private int i = 0; // Program counter
    private HashMap<String, Integer> labelAdd = new HashMap<String, Integer>();
    private HashMap<Integer, String> labelRef = new HashMap<Integer, String>();
    private HashMap<String, Integer> registers = new HashMap<String, Integer>();

    /**
     * labelAdd false_branch:
     * f | 7
     * g | 10
     * main | 15
     * 
     * labelRef b false_branch
     * 9 | f
     * 16 | f
     * 18 | g
     * 
     * code[9] = labelAdd.get(labelRef.get(9)) -> code[9] = labelAdd.get(f) ->
     * code[9] = 7
     * code[16] = labelAdd.get(labelRef.get(16)) -> code[16] = labelAdd.get(f) ->
     * code[16] = 7
     * code[18] = labelAdd.get(labelRef.get(18)) -> code[18] = labelAdd.get(g) ->
     * code[18] = 10
     * 
     * In code c'è il numero di riga in cui e' definita l'etichetta a cui sto
     * facendo riferimento
     */

    @Override
    public Void visitAssembly(AVMParser.AssemblyContext ctx) {
        // 'a0'|'t1'|'s0'|'ra'|'fp'|'sp'|'v0'|'al'
        registers.put("$a0", 0);
        registers.put("$t1", 1);
        registers.put("$s0", 2);
        registers.put("$ra", 3);
        registers.put("$fp", 4);
        registers.put("$sp", 5);
        registers.put("$v0", 6);
        registers.put("$al", 7);

        visitChildren(ctx);
        for (Integer refAdd : labelRef.keySet()) {
            code[refAdd] = labelAdd.get(labelRef.get(refAdd));
        }
        return null;
    }
    
    @Override
    public Void visitInstruction(AVMParser.InstructionContext ctx) {
        switch (ctx.getStart().getType()) {
            case AVMLexer.LOADI:
                code[i++] = AVMParser.LOADI;
                code[i++] = Integer.parseInt(ctx.n.getText());
                code[i++] = registers.get(ctx.r.getText());
                break;
            case AVMLexer.PUSH:
                // push register
                code[i++] = AVMParser.PUSH;
                code[i++] = registers.get(ctx.r.getText());
                break;
            case AVMLexer.POP:
                code[i++] = AVMParser.POP;
                break;
            case AVMLexer.ADD:
                code[i++] = AVMParser.ADD;
                code[i++] = registers.get(ctx.r1.getText());
                code[i++] = registers.get(ctx.r2.getText());
                code[i++] = registers.get(ctx.rDest.getText());
                break;
            case AVMLexer.SUB:
                code[i++] = AVMParser.SUB;
                code[i++] = registers.get(ctx.r1.getText());
                code[i++] = registers.get(ctx.r2.getText());
                code[i++] = registers.get(ctx.rDest.getText());
                break;
            case AVMLexer.MULT:
                code[i++] = AVMParser.MULT;
                code[i++] = registers.get(ctx.r1.getText());
                code[i++] = registers.get(ctx.r2.getText());
                code[i++] = registers.get(ctx.rDest.getText());
                break;
            case AVMLexer.DIV:
                code[i++] = AVMParser.DIV;
                code[i++] = registers.get(ctx.r1.getText());
                code[i++] = registers.get(ctx.r2.getText());
                code[i++] = registers.get(ctx.rDest.getText());
                break;
            case AVMLexer.STOREW:
                code[i++] = AVMParser.STOREW;
                code[i++] = registers.get(ctx.rSrc.getText());
                code[i++] = Integer.parseInt(ctx.n.getText());
                code[i++] = registers.get(ctx.rDest.getText());
                break;
            case AVMLexer.LOADW:
                code[i++] = AVMParser.LOADW;
                code[i++] = registers.get(ctx.rSrc.getText());
                code[i++] = Integer.parseInt(ctx.n.getText());
                code[i++] = registers.get(ctx.rDest.getText());
                break;
            case AVMLexer.STOREB:
                code[i++] = AVMParser.STOREB;
                code[i++] = registers.get(ctx.rSrc.getText());
                code[i++] = Integer.parseInt(ctx.n.getText());
                code[i++] = registers.get(ctx.rDest.getText());
            case AVMLexer.LOADB:
                code[i++] = AVMParser.LOADB;
                code[i++] = registers.get(ctx.rSrc.getText());
                code[i++] = Integer.parseInt(ctx.n.getText());
                code[i++] = registers.get(ctx.rDest.getText());
            case AVMLexer.LABEL:
                labelAdd.put(ctx.l.getText(), i);
                break;
            case AVMLexer.BRANCH:
                code[i++] = AVMParser.BRANCH;
                labelRef.put(i++, (ctx.l != null ? ctx.l.getText() : null));
                break;
            case AVMLexer.BRANCHEQ:
                code[i++] = AVMParser.BRANCHEQ;
                labelRef.put(i++, (ctx.l != null ? ctx.l.getText() : null));
                code[i++] = registers.get(ctx.r1.getText());
                code[i++] = registers.get(ctx.r2.getText());
                break;
            case AVMLexer.BRANCHLESSEQ:
                code[i++] = AVMParser.BRANCHLESSEQ;
                labelRef.put(i++, (ctx.l != null ? ctx.l.getText() : null));
                code[i++] = registers.get(ctx.r1.getText());
                code[i++] = registers.get(ctx.r2.getText());
            case AVMLexer.BRANCHLESST:
                code[i++] = AVMParser.BRANCHLESST;
                labelRef.put(i++, (ctx.l != null ? ctx.l.getText() : null));
                code[i++] = registers.get(ctx.r1.getText());
                code[i++] = registers.get(ctx.r2.getText());
                break;
            case AVMLexer.JAL:
                code[i++] = AVMParser.JAL;
                labelRef.put(i++, (ctx.l != null ? ctx.l.getText() : null));
                break;
            case AVMLexer.PRINT:
                code[i++] = AVMParser.PRINT;
                code[i++] = registers.get(ctx.r.getText());
                break;
            case AVMLexer.HALT:
                code[i++] = AVMParser.HALT;
                break;
            case AVMLexer.MOVE:
                code[i++] = AVMParser.MOVE;
                code[i++] = registers.get(ctx.r2.getText());
                code[i++] = registers.get(ctx.r1.getText());
                break;
            case AVMLexer.ADDI:
                code[i++] = AVMParser.ADDI;
                code[i++] = registers.get(ctx.r1.getText());
                code[i++] = Integer.parseInt(ctx.n.getText());
                code[i++] = registers.get(ctx.rDest.getText());
                break;
            case AVMLexer.JR:
                code[i++] = AVMParser.JR;
                code[i++] = registers.get(ctx.r.getText());
                break;
            default:
                break; // Invalid instruction
        }
        return null;
    }

}
