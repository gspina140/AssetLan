# Progetto AA 2021-22 -- AssetLan

AssetLan è un semplice linguaggio imperativo con ASSET, in cui i parametri possono 
essere sia standard che asset, con RICORSIONE 
e senza MUTUA RICORSIONE. 

PROGETTO COMPLETO: Sviluppo di un Compilatore per AssetLan + report in cui si 
spiegano le scelte progettuali

## ESERCIZIO 1
L'analizzatore lessicale deve ritornare la lista degli errori lessicali in un file 
di output. Il report deve contenere la discussione di tre esempi e degli errori segnalati.

## ESERCIZIO 2
Sviluppare la tabella dei simboli del programma. Decidere se implementarlo come 
lista di hash-table o come hash-table di liste.
Il codice sviluppato deve controllare:

* variabili/funzioni non dichiarate
* variabili/funzioni dichiarate piu` volte nello stesso ambiente 

## ESERCIZIO 3
Sviluppare un'analisi semantica che verifica:
* la correttezza dei tipi (in particolare numero e tipo dei parametri attuali 
  se conformi al numero e tipo dei parametri formali)
* liquidity
  significa che 
	(a) per ogni funzione, i parametri formali asset devono essere 0 alla fine della
	    sua esecuzione (i valori sono stati spostati nei campi asset oppure trasferiti
	    con una transfer)
	(b) alla fine del programma i campi asset sono 0

Il report deve contenere TUTTE le regole semantiche utilizzate e relativa discussione.
Si faccia attenzione all'aliasing.

Codici da verificare/discutere:

1)   
```
     asset x, y ;
     void f()[u,v]{
	u -o y ;
	v -o x ;
     }
     void main()[u,v]{
        u -o x ;
	u -o y ;
	f()[x,y] ;
     }
     main()[2,3]	// scambia i valori di x e y; il contratto non e` liquido
```

2)   
```
     int a; int b = 0 ;
     asset z ;
     void g()[]{
	transfer z ;
     }	
     void f(int x)[asset y]{
	a = y ;		// non e` errore
        b = b+x ;      
        y -o z ;
	g()[] ;
     }
     f(1)[2] 		// il contratto e` liquido
```

3)
```
     int a ; 
     asset x ;
     void f(int n)[asset u, asset v, asset w]{ 
        u -o x ; 
	f(v,w,u) ;
     }
     void main()[asset a, asset b, asset c]{
	f()[a,b,c] ;
	transfer x ;
     }
     main()[1,2,3] ;	// il contratto e` liquido
```

4)
```
     asset x ;
     void f(int n)[asset u, asset v]{ 
        if (n == 0) u -o x ; 
	else { u -o x ; v -o x ; }
     }
     void main()[asset a]{
	f(0)[a,a] ;	// semantica di f()[a,a] ?
	transfer x ;
     }
     main()[1] ;	// il contratto e` liquido
```

## ESERCIZIO 4
Definire un linguaggio bytecode per eseguire programmi in SimpLanPlus
e implementare l'interprete. In particolare:

*  Il bytecode deve avere istruzioni per una macchina a pila che memorizza in un 
   apposito registro il valore dell'ultima istruzione calcolata [vedi slide delle lezioni]
*  Implementare l'interprete per il bytecode
*  Compilare ed eseguire i programmi del linguaggio ad alto livello

CODICI DA VERIFICARE:

```
     int x = 1;
     void f(int n)[]{ 
          if (n == 0) { print(x) ; }
          else { x = x * n ; f(n-1)[] ; }	
     }
     f(10) ;
```

=====

IMPLEMENTARE I CODICI DELL'ESERCIZIO 3

=====

Cosa succede se nel codice 4) di sopra rimpiazzo il main con il seguente:

```
     void main()[asset a, asset b]{
	f(0)[a,b] ;
	print x ;	
	transfer x ;
     }
     main()[1,2] ;	// il contratto e` liquido?
```

=====

CONSEGNA: Come ultima consegna, bisogna inviare il pacchetto completo di tutte le fasi della compilazione.
E` possibile anche modificare i file degli assegnamenti precedenti, se necessario.
Bisogna sempre allegare un pdf di presentazione.

Il pacchetto dovra' essere chiamato Cognome1_CON_#n.zip (dove il Cognome1 e' il
cognome del primo componente del gruppo in ordine alfabetico e #n e` il numero di 
consegna). 

Tempistiche: 

* CONSEGNA PRIMI 2 ESERCIZI: Domenica 24 Aprile ore 24.00
* CONSEGNA ULTIMI 2 ESERCIZI: Domenica 29 Maggio ore 24.00

A seguire: discussione dell'intero progetto.