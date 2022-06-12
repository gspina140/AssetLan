\\PARSER
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
      | STOREW	rSrc=REGISTER n=NUMBER '(' rDest=REGISTER ')'                              
      | LOADW   rDest=REGISTER n=NUMBER '(' rSrc=REGISTER ')'          
      | STOREB	rSrc=REGISTER n=NUMBER '(' rDest=REGISTER ')'    
      | LOADB   rDest=REGISTER n=NUMBER '(' rSrc=REGISTER ')'    
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

\\LEXER
LOADI        : 'li' ; 
PUSH  	     : 'push' ; 	
POP	     : 'pop' ; 	
ADD	     : 'add' ;  	
SUB	     : 'sub' ;	
MULT	     : 'mult' ;
DIV	     : 'div' ;
STOREW	     : 'sw' ; 
LOADW	     : 'lw' ;
STOREB       : 'sb' ;	
LOADB	     : 'lb' ;
BRANCH	     : 'b' ;
BRANCHEQ     : 'beq' ;	
BRANCHLESST  : 'blt' ;	
BRANCHLESSEQ :'ble' ;	
JAL          : 'jal' ;	
PRINT	     : 'print' ;	
HALT	     : 'halt' ;	
MOVE         : 'move' ;	
ADDI         : 'addi' ;	
JR           : 'jr' ;	

COL	     : ':' ;
LABEL	     : ('a'..'z'|'A'..'Z')('a'..'z' | 'A'..'Z' | '0'..'9'|'_')* ;
NUMBER	     : '0' | ('-')?(('1'..'9')('0'..'9')*) ;
REGISTER     : '$' ('a0'|'t1'|'s0'|'ra'|'fp'|'sp'|'v0'|'al');

