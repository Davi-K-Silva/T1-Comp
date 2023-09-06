// Davi k, Eduardo S, Luis Gabriel
import java.io.*;

public class AsdrMinijava {

  private static final int BASE_TOKEN_NUM = 301;
  
   public static final int IDENT		  = 301;
   public static final int INT_LITERAL = 302;
   public static final int CLASS_TOKEN = 303;
   public static final int PUBLIC_TOKEN = 304;
   public static final int STATIC_TOKEN = 305;
   public static final int VOID_TOKEN = 306;
   public static final int MAIN_TOKEN = 307;
   public static final int STRING_TOKEN = 308;
   public static final int EXTENDS_TOKEN = 309;
   public static final int RETURN_TOKEN = 3010;
   public static final int INT_TOKEN = 311;
   public static final int BOOLEAN_TOKEN = 312;
   public static final int CMDO = 313;

   public static final String tokenList[] = 
   {"IDENTIFIER",
      "INT_LITERAL", 
      "class",
      "public",
      "static",
      "void",
      "main",
      "String",
      "extends",
      "return",
      "int",
      "boolean",
      "cmdo"};


                                      
  /* referencia ao objeto Scanner gerado pelo JFLEX */
  private MinijavaLex lexer;

  public ParserVal yylval;

  private static int laToken;
  private boolean debug;

  
  /* construtor da classe */
  public AsdrMinijava (Reader r) {
      lexer = new MinijavaLex (r, this);
  }

  /***** Gramática Transformada para LL1 e removendo Expression
Goal 	::= 	MainClass ( ClassDeclaration )* <EOF>
MainClass 	::= 	"class" Identifier "{" "public" "static" "void" "main" "(" "String" "[" "]" Identifier ")" "{" Statement "}" "}"
ClassDeclaration 	::= 	"class" Identifier ( "extends" Identifier )? "{" ( VarDeclaration )* ( MethodDeclaration )* "}"
VarDeclaration 	::= 	Type Identifier ";"
MethodDeclaration 	::= 	"public" Type Identifier "(" ( Type Identifier ( "," Type Identifier )* )? ")" "{" ( VarDeclaration )* ( Statement )* "return" Identifier ";" "}"
Type 	::= 	"int" (RestoType)?
                |  "boolean"
                |  Identifier
Statement ::= cmdo
RestoType := "[" "]"
Identifier 	::= 	<IDENTIFIER>
***/ 

   private void Goal() {
      if (debug) System.out.println("Goal --> MainClass ( ClassDeclaration )* <EOF>");
      MainClass();
      while(laToken == CLASS_TOKEN){
         ClassDeclaration();
      }
   }

   private void MainClass(){
       if (debug) System.out.println("MainClass --> \"class\" Identifier \"{\" \"public\" \"static\" \"void\" \"main\" \"(\" \"String\" \"[\" \"]\" Identifier \")\" \"{\" Statement \"}\" \"}\"");
      verifica(CLASS_TOKEN);
      Identifier();
      verifica((int)'{');
      verifica(PUBLIC_TOKEN);
      verifica(STATIC_TOKEN);
      verifica(VOID_TOKEN);
      verifica(MAIN_TOKEN);
      verifica((int)'(');
      verifica(STRING_TOKEN);
      verifica((int)'[');
      verifica((int)']');
      Identifier();
      verifica((int)')');
      verifica((int)'{');
      Statement();
      verifica((int)'}');
      verifica((int)'}');
   }

   private void ClassDeclaration(){
      if (debug) System.out.println("ClassDeclaration --> \"class\" Identifier ( \"extends\" Identifier )? \"{\" ( VarDeclaration )* ( MethodDeclaration )* \"}\"");
      verifica(CLASS_TOKEN);
      Identifier();
      if (laToken == EXTENDS_TOKEN){
         verifica(EXTENDS_TOKEN);
         Identifier();
      }
      verifica((int)'{');
      while(laToken != (int)'}' && laToken != PUBLIC_TOKEN){
         VarDeclaration();
      }
      while(laToken == PUBLIC_TOKEN){
         MethodDeclaration();
      }
      verifica((int)'}');
      
   }

   private void VarDeclaration(){
      if (debug) System.out.println("VarDeclaration --> Type Identifier \";\"");
      Type();
      Identifier();
      verifica((int)';');
   }

   private void MethodDeclaration(){
      if (debug) System.out.println("MethodDeclaration --> \"public\" Type Identifier \"(\" ( Type Identifier ( \",\" Type Identifier )* )? \")\" \"{\" ( VarDeclaration )* ( Statement )* \"return\" Expression \";\" \"}\"");
      verifica(PUBLIC_TOKEN);
      Type();
      Identifier();
      verifica((int)'(');
      if(laToken != (int)')'){
         Type();
         Identifier();
         while(laToken == (int)','){
            verifica((int)',');
            Type();
            Identifier();
         }
      }
      verifica((int)')');
      verifica((int)'{');
      while(laToken != RETURN_TOKEN && laToken!=CMDO){
         VarDeclaration();
      }
      while(laToken == CMDO){
         Statement();
      }
      verifica(RETURN_TOKEN);
      Identifier();
      verifica((int)';');
      verifica((int)'}');
   }

   private void Type(){
      if(laToken == INT_TOKEN){
         if (debug) System.out.println("Type -> int");
         verifica(INT_TOKEN);
         if (laToken == (int)'['){
            RestoType();
         }
      }  
      else if (laToken == BOOLEAN_TOKEN){
         if (debug) System.out.println("Type -> boolean");
         verifica(BOOLEAN_TOKEN);
      } 
      else  if (laToken == IDENT){
         if (debug) System.out.println("Type -> Identifier");
         Identifier();
      }
      else yyerror("Esperado int ou boolean ou Ide");
   }

   private void RestoType(){
      if (debug) System.out.println("RestoType -> \"[\" \"]\"");
      verifica((int)'[');
      verifica((int)']');
   }

   private void Statement(){
      if (debug) System.out.println("Statement -> cmdo");
      verifica(CMDO);
   }

   private void Identifier(){
      if (debug) System.out.println("Identifier -> IDENT");
      verifica(IDENT);
   }


  private void verifica(int expected) {
      if (laToken == expected)
         laToken = this.yylex();
      else {
         String expStr, laStr;       

		expStr = ((expected < BASE_TOKEN_NUM )
                ? ""+(char)expected
			     : tokenList[expected-BASE_TOKEN_NUM]);
         
		laStr = ((laToken < BASE_TOKEN_NUM )
                ? Character.toString(laToken)
                : tokenList[laToken-BASE_TOKEN_NUM]);

          yyerror( "esperado token: " + expStr +
                   " na entrada: " + laStr);
     }
   }

   /* metodo de acesso ao Scanner gerado pelo JFLEX */
   private int yylex() {
       int retVal = -1;
       try {
           yylval = new ParserVal(0); //zera o valor do token
           retVal = lexer.yylex(); //le a entrada do arquivo e retorna um token
       } catch (IOException e) {
           System.err.println("IO Error:" + e);
          }
       return retVal; //retorna o token para o Parser 
   }

  /* metodo de manipulacao de erros de sintaxe */
  public void yyerror (String error) {
     System.err.println("Erro: " + error);
     System.err.println("Entrada rejeitada");
     System.out.println("\n\nFalhou!!!");
     System.exit(1);
     
  }

  public void setDebug(boolean trace) {
      debug = true;
  }


  /**
   * Runs the scanner on input files.
   *
   * This main method is the debugging routine for the scanner.
   * It prints debugging information about each returned token to
   * System.out until the end of file is reached, or an error occured.
   *
   * @param args   the command line, contains the filenames to run
   *               the scanner on.
   */
  public static void main(String[] args) {
     AsdrMinijava parser = null;
     try {
         if (args.length == 0)
            parser = new AsdrMinijava(new InputStreamReader(System.in));
         else 
            parser = new  AsdrMinijava( new java.io.FileReader(args[0]));

          parser.setDebug(false);
          laToken = parser.yylex();          
          parser.Goal();
          
          if (laToken== MinijavaLex.YYEOF)
             System.out.println("\n\nSucesso!");
          else     
             System.out.println("\n\nFalhou - esperado EOF.");               

        }
        catch (java.io.FileNotFoundException e) {
          System.out.println("File not found : \""+args[0]+"\"");
        }
//        catch (java.io.IOException e) {
//          System.out.println("IO error scanning file \""+args[0]+"\"");
//          System.out.println(e);
//        }
//        catch (Exception e) {
//          System.out.println("Unexpected exception:");
//          e.printStackTrace();
//      }
    
  }
  
}

