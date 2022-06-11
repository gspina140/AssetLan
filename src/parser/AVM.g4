grammar AVM;

@header {
import java.util.HashMap;
}

@lexer::members {
public int lexicalErrors=0;
}

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/
  
assembly: (instruction)* ;

instruction:
    (  LOADI r=REGISTER  n=NUMBER
      | PUSH r=REGISTER
	  | MOVE r1=REGISTER r2=REGISTER   
	  | POP		    
	  | ADD     rDest=REGISTER r1=REGISTER r2=REGISTER	
      | ADDI  rDest=REGISTER r1=REGISTER n=NUMBER	    
	  | SUB	    rDest=REGISTER r1=REGISTER r2=REGISTER	    
	  | MULT    rDest=REGISTER r1=REGISTER r2=REGISTER	    
	  | DIV	    rDest=REGISTER r1=REGISTER r2=REGISTER	    
	  | STOREW	rSrc=REGISTER n=NUMBER '(' rDest=REGISTER ')'    //for integer
	  | LOADW   rDest=REGISTER n=NUMBER '(' rSrc=REGISTER ')'    //for integer      
	  | STOREB	rSrc=REGISTER n=NUMBER '(' rDest=REGISTER ')'    //for bool
	  | LOADB   rDest=REGISTER n=NUMBER '(' rSrc=REGISTER ')'    //for bool
	  | l=LABEL COL     
	  | BRANCH l=LABEL  
	  | BRANCHEQ  r1=REGISTER r2=REGISTER l=LABEL 
	  | BRANCHLESSEQ r1=REGISTER r2=REGISTER l=LABEL 
	  | BRANCHLESST r1=REGISTER r2=REGISTER l=LABEL 
	  | JAL l=LABEL      
      | JR r=REGISTER 
	  | PRINT r=REGISTER           
	  | HALT
	  ) ;
 	 
/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

LOADI: 'li' ; 
PUSH  	 : 'push' ; 	// pushes constant in the stack
POP	 : 'pop' ; 	// pops from stack
ADD	 : 'add' ;  	// add two values from the stack
SUB	 : 'sub' ;	// sub two values from the stack
MULT	 : 'mult' ;  	// mult two values from the stack
DIV	 : 'div' ;	// div two values from the stack
STOREW	 : 'sw' ; 	// store in the memory cell pointed by top the value next
LOADW	 : 'lw' ;	// load a value from the memory cell pointed by top
STOREB     : 'sb' ;	
LOADB	 : 'lb' ;
BRANCH	 : 'b' ;	// jump to label
BRANCHEQ : 'beq' ;	// jump to label if top == next
BRANCHLESST : 'blt' ;	
BRANCHLESSEQ:'ble' ;	// jump to label if top <= next
JAL  : 'jal' ;	// jump and link
PRINT	 : 'print' ;	// print top of stack
HALT	 : 'halt' ;	// stop execution
MOVE     : 'move' ;	// move a value from one register to another
ADDI     : 'addi' ;	// add a constant to a register
JR : 'jr' ;	// jump to register

COL	 : ':' ;
LABEL	 : ('a'..'z'|'A'..'Z')('a'..'z' | 'A'..'Z' | '0'..'9'|'_')* ;
NUMBER	 : '0' | ('-')?(('1'..'9')('0'..'9')*) ;
REGISTER : '$' ('a0'|'t1'|'s0'|'ra'|'fp'|'sp'|'v0'|'al');

WHITESP  : ( '\t' | ' ' | '\r' | '\n' )+   -> channel(HIDDEN);

ERR   	 : . { System.err.println("Invalid char: "+ getText()); lexicalErrors++;  } -> channel(HIDDEN); 