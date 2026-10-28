package TestMain;
import Lexer.Lexer;
import Lexer.Token;
import Lexer.TokenType;
import Parser.Parser;
import Parser.Nodo;

public class Main {
    private String codigoFuente;
    
    public Main() {
        this.codigoFuente = 
            "PROGRAM miPrograma; " +
            "VAR total : INTEGER; " +
            "BEGIN " +
            " WHILE total DO "
           + "total := total + 1 " +
            "END."; 
    }
    
    public static void main(String[] args) {
        new Main().run();
    }
    
    private void run() {
        System.out.println("PRUEBA DEL LEXICO");
        // Instanciamos el primer Lexer solo para imprimir los tokens
        Lexer lexerPrueba = new Lexer(codigoFuente);
        Token token = lexerPrueba.nextToken();
        
        // Comprobar los tokens
        while(token.getAtributo() != TokenType.EOF) {
            System.out.println(token);
            token = lexerPrueba.nextToken();
        }

        System.out.println("\nPRUEBA DEL PARSER Y AST");
        Lexer lexerReal = new Lexer(codigoFuente);
        
        Parser parser = new Parser(lexerReal);
        
        Nodo arbol = parser.parseProgram();
        
        // Evaluamos el árbol sintáctico
        System.out.println("Resultado de la evaluacion matematica: " + arbol.evaluar());
    }
}