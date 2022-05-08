// Esercizio 3
// Programma 3

int a ;
asset x ;
void f(int n)[asset u, asset v, asset w]{
	u -o x ;
	f(v,w,u)[] ;
}
void main()[asset a, asset b, asset c]{
	f()[a,b,c] ;
	transfer x ;
}
main()[1,2,3] ; // il contratto e' liquido
