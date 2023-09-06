# T1-Comp

Davi K, Eduardo S, Luis Gabriel P.

## Gramatica Final
Transformada para LL(1) com Expression removido e Statement alterado.

```
Goal 	::= 	MainClass ( ClassDeclaration )* <EOF>
MainClass 	::= 	"class" Identifier "{" "public" "static" "void" "main" "(" "String" "[" "]" Identifier ")" "{" Statement "}" "}"
ClassDeclaration 	::= 	"class" Identifier ( "extends" Identifier )? "{" ( VarDeclaration )* ( MethodDeclaration )* "}"
VarDeclaration 	::= 	Type Identifier ";"
MethodDeclaration 	::= 	"public" Type Identifier "(" ( Type Identifier ( "," Type Identifier )* )? ")" "{" ( VarDeclaration )* ( Statement )* "return" Identifier ";" "}"
Type 	::= 	"int" (RestoType)?
                |  "boolean"
                |  Identifier
RestoType := "[" "]"
Identifier 	::= 	<IDENTIFIER>
```

**OBS**: no lugar do Expression foi colocado um Identifier em ( ... "return" Identifier ";" ...)

## Como rodar

Rodar jflex em MiniJava.flex

Compilar: 
- MinijavaLex.java
- ParseVal.java
- AsdrMinijava.java

E então utilizar o comando:

    java AsdrMinijava Testes/<TesteDesejado>

Com arquivo:

    java AsdrMinijava Testes/minijavaTest.java