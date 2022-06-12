asset x ;
void f(int n)[asset u, asset v]{
	if (n == 0) {u -o x ;}
	else { u -o x ; v -o x ; }
}
void main()[asset a, asset b]{
	f(0)[a,b] ;
	print x ;
	transfer x ;
}
main()[1,2] ;		// il contratto e' liquido?
