int a ; int b = 0 ;
asset z ;
void g()[]{
	transfer z ;
}
void f(int x)[asset y]{
	a = y ;		// non e' errore
	b = b+x ;
	y -o z ;
	g()[] ;
}
f(1)[2]			// il contratto e' liquido
