// Esercizio 3
// Programma 1

asset x, y ;		// versione corretta: asset x; asset y;
void f()[u,v]{		// versione corretta: void f()[asset u, asset v]{
	u -o y ;
	v -o x ;
}
void main()[u,v]{	// versione corretta: void main()[asset u, asset v]{
	u -o x ;
	u -o y ;
	f()[x,y] ;
}
main()[2,3]			// scambia i valori di x e y; il contratto non e' liquido