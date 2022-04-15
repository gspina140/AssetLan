grammar AssetLan;

// THIS IS THE PARSER INPUT

program		: field* asset* function* initcall ;
						// la portata di field e asset e' soltanto function

field		: type ID ('=' exp)? ';' ;

asset		: 'asset' ID ';' ;

function	: (type | 'void') ID '(' (par = dec)? ')' '[' (adec)? ']'
			  '{' (innerDec = dec*) statement* '}' ;

dec			: type ID (',' type ID)* ;

adec		: 'asset' ID (',' 'asset' ID)*; 

statement	: assignment ';'    #assignExp
			| move ';'		    #moveAsset          // sposta un asset da una parte all'altra
			| print ';'         #printExp
			| transfer ';'	    #transferAsset      // trasferisce l'asset all' utente (chi esegue il codice)
			| ret ';'           #returnExp
			| ite               #ifElseExp
			| call ';'          #callFun;

type		: 'int'
			| 'bool';

assignment	: ID '=' exp;

move		: ID '-o' ID;

print		: 'print' exp;

transfer	: 'transfer' ID;

ret			: 'return' (exp)?;

ite			: 'if' '(' exp ')' (statement | '{' statement+ '}') ('else' (statement | '{' statement+ '}') )?;
                //'if' '(' exp ')' statement ('else' statement)?;
			    // Nella grammatica iniziale erano possibili soltanto uno ed un solo statement per l'if
			    // ed uno ed un solo statement per l'else; abbiamo aggiunto la possibilità di mettere uno
			    // o più statement per l'if e per l'else fra parentesi graffe (c-like)
			    // (questo e' utilizzato dal programma 3 dell'esercizio 3 e dai programmi dell'esercizio 4)

call		: ID '(' (exp (',' exp)* )? ')' '[' (ID (',' ID)* )? ']';

initcall	: ID '(' (exp (',' exp)* )? ')' '[' (exp (',' exp)* )? ']';

exp			: '(' exp ')'											# baseExp
			| '-' exp												# negExp
			| '!' exp												# notExp
			| ID													# derExp
			| left = exp op = ('*' | '/')				right = exp	# binExp
			| left = exp op = ('+' | '-') 				right = exp	# binExp
			| left = exp op = ('<' | '<=' | '>' | '>=')	right = exp	# binExp
			| left = exp op = ('==' | '!=')				right = exp	# binExp
			| left = exp op = '&&'						right = exp	# binExp
			| left = exp op = '||'						right = exp	# binExp
			| call													# callExp
			| BOOL													# boolExp
			| NUMBER												# valExp;

// THIS IS THE LEXER INPUT

//Booleans
BOOL		: 'true'|'false';

//IDs
fragment CHAR		: 'a'..'z'|'A'..'Z' ;
ID			: CHAR (CHAR | DIGIT)* ;

//Numbers
fragment DIGIT		: '0'..'9';
NUMBER		: DIGIT+;

//ESCAPE SEQUENCES
WS				: (' '|'\t'|'\n'|'\r')-> skip;
LINECOMMENTS	: '//' (~('\n'|'\r'))* -> skip;
BLOCKCOMMENTS	: '/*' (~('/'|'*')|'/'~'*'|'*'~'/'|BLOCKCOMMENTS)* '*/' -> skip;

/*
 SEMANTICA DI ASSETLAN
 
 In AssetLan le funzioni possono essere dichiarate con asset. Ad esempio
 
 void f(int a, bool b)[asset u, asset v]{ Body }
 
 Quando la funzione viene invocata, ad esempio
 
 f(5,true)[x,y]
 
 quello che accade e` che l'asset x e y VENGONO SVUOTATI e memorizzati nei parametri
 formali u e v, rispettivamente. Quindi, a seguito dell'invocazione, i valori di x e
 di y sono 0.
 
 Gli asset possono essere spostati SOLAMENTE
 
 * mediante l'operazione move x -o y il cui significato e`
 		(a) il valore di x viene sommato a quello di y e il totale memorizzato in y
 		(b) il valore di x diventa 0
   (i 2 argomenti di move devono essere 2 asset)
 
 * mediante l'operazione transfer:	transfer x significa che
		(a) il valore di x viene trasferito al chiamante di initcall
		(b) il valore di x diventa 0
 
 */