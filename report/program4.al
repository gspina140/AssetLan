asset x ;
void f(int n)[asset u, asset v]{
	if (n == 0) {u -o x} ;
	else { u -o x ; v -o x ; }
}
void main()[asset a]{
	f(0)[a,a] ;	// semantica di f()[a,a] ?
	transfer x ;
}
main()[1] ;		// il contratto e' liquido
