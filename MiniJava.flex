%%
// Davi k, Eduardo S, Luis Gabriel

%class MinijavaLex

%{
  private AsdrMinijava yyparser;

  public MinijavaLex(java.io.Reader r, AsdrMinijava yyparser) {
    this(r);
    this.yyparser = yyparser;
  }

%} 

%integer
%char
%unicode
%line

DIGIT=		[0-9]
LETTER=		[a-zA-Z]
WHITESPACE=	[ \t]
LineTerminator = \r|\n|\r\n  
IDENTIFIER = {LETTER}({LETTER}|{DIGIT}|_)*
 
%%


"class"                       {return AsdrMinijava.CLASS_TOKEN;}
"main"                        {return AsdrMinijava.MAIN_TOKEN;}                  
"public"                      {return AsdrMinijava.PUBLIC_TOKEN;}
"static"                      {return AsdrMinijava.STATIC_TOKEN;}
"void"                        {return AsdrMinijava.VOID_TOKEN;}
"String"                      {return AsdrMinijava.STRING_TOKEN;}  
"boolean"                     {return AsdrMinijava.BOOLEAN_TOKEN;}
"int"                         {return AsdrMinijava.INT_TOKEN;}
"extends"                     {return AsdrMinijava.EXTENDS_TOKEN;}
"cmdo"                        {return AsdrMinijava.CMDO;}
"return"                      {return AsdrMinijava.RETURN_TOKEN;}

{DIGIT}{DIGIT}* {return AsdrMinijava.INT_LITERAL;}
{IDENTIFIER} {return AsdrMinijava.IDENT;}

"]" |
"[" |
"=" |
";" |
"{" |
"}" |
"," |
"(" |
")"                         {return yytext().charAt(0);}
{WHITESPACE}+               { }
{LineTerminator}		{}
.          {yyparser.yyerror(yyline+1 + ": caracter invalido: "+yytext());}



// Goal 	::= 	MainClass ( ClassDeclaration )* <EOF>
// MainClass 	::= 	"class" Identifier "{" "public" "static" "void" "main" "(" "String" "[" "]" Identifier ")" "{" Statement "}" "}"
// ClassDeclaration 	::= 	"class" Identifier ( "extends" Identifier )? "{" ( VarDeclaration )* ( MethodDeclaration )* "}"
// VarDeclaration 	::= 	Type Identifier ";"
// MethodDeclaration 	::= 	"public" Type Identifier "(" ( Type Identifier ( "," Type Identifier )* )? ")" "{" ( VarDeclaration )* ( Statement )* "return" Expression ";" "}"
// Type 	::= 	"int" "[" "]"
// 	| 	"boolean"
// 	| 	"int"
// 	| 	Identifier
// Statement 	::= cmdo
// Identifier 	::= 	<IDENTIFIER>