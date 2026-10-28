package Parser;

import Lexer.Lexer;
import Lexer.Token;
import Lexer.TokenType;

public class Parser {

    private Lexer lexer;
    private Token current; 

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.current = lexer.nextToken(); 
    }

    private void match(TokenType type) {
        if (current.getAtributo() == type) {
            current = lexer.nextToken();
        } else {
            throw new RuntimeException("Error Sintáctico: Se esperaba " + type + " pero se encontró " + current.getAtributo());
        }
    }

    public Nodo parseProgram() {
        match(TokenType.PROGRAM); 
        match(TokenType.ID);
        match(TokenType.SEMICOLON); 
        
        Nodo bloque = parseBlock();
        
        match(TokenType.DOT); // Exige el punto final .
        return bloque;
    }

    // block -> declaration_part statement_part
    private Nodo parseBlock() {
        parseDeclarationPart(); 
        return parseCompoundStatement();
    }

    // declaration_part -> VAR declaration_list | ε
    private void parseDeclarationPart() {
        if (current.getAtributo() == TokenType.VAR) { 
            match(TokenType.VAR);
            
            while (current.getAtributo() == TokenType.ID) {
                parseDeclaration();
                match(TokenType.SEMICOLON);
            }
        }
    }

    // declaration -> identifier_list : type
    private void parseDeclaration() {
        match(TokenType.ID);
        
        while (current.getAtributo() == TokenType.COMMA) { 
            match(TokenType.COMMA);
            match(TokenType.ID);
        }
        
        match(TokenType.COLON); // Exige los dos puntos :
        
        if (current.getAtributo() == TokenType.INTEGER || 
            current.getAtributo() == TokenType.REAL || 
            current.getAtributo() == TokenType.BOOLEAN) {
            match(current.getAtributo()); 
        } else {
            throw new RuntimeException("Error: Tipo de dato no válido");
        }
    }

    // compound_statement -> BEGIN statement_list END
    private Nodo parseCompoundStatement() {
        match(TokenType.BEGIN); 
        
        NodoBlock nodoBloque = new NodoBlock(); 
        nodoBloque.agregarSentencia(parseStatement());
        
        while (current.getAtributo() == TokenType.SEMICOLON) {
            match(TokenType.SEMICOLON);
            
            if (current.getAtributo() == TokenType.END) {
                break;
            }
            
            nodoBloque.agregarSentencia(parseStatement());
        }
        
        match(TokenType.END); 
        return nodoBloque;
    }

    // STATEMENTS (Sentencias de control y asignación)
    public Nodo parseStatement() {
        if (current.getAtributo() == TokenType.ID) {
            return parseAssignment();
        } else if (current.getAtributo() == TokenType.IF) {
            return parseIfStatement();
        } else if (current.getAtributo() == TokenType.WHILE) {
            return parseWhileStatement();
        } else if (current.getAtributo() == TokenType.BEGIN) {
            return parseCompoundStatement();
        } else {
            throw new RuntimeException("Sentencia no reconocida: " + current.getAtributo());
        }
    }

    // assignment -> identifier := expression
    private Nodo parseAssignment() {
        String idName = current.getValue();
        match(TokenType.ID);
        match(TokenType.ASSIGN);
        Nodo expr = parseExpr();

        return new NodoAsignacion('=', idName, expr);
    }

    // if_statement -> IF expression THEN statement ELSE statement
    private Nodo parseIfStatement() {
        match(TokenType.IF);
        Nodo condicion = parseExpr(); 
        match(TokenType.THEN); 

        Nodo ramaVerdadera = parseStatement();
        Nodo ramaFalsa = null;

        if (current.getAtributo() == TokenType.ELSE) {
            match(TokenType.ELSE);
            ramaFalsa = parseStatement();
        }

        return new NodoIf(condicion, ramaVerdadera, ramaFalsa);
    }

    // while_statement -> WHILE expression DO statement
    private Nodo parseWhileStatement() {
        match(TokenType.WHILE);
        Nodo condicion = parseExpr();
        match(TokenType.DO); 

        Nodo cuerpo = parseStatement();

        return new NodoWhile(condicion, cuerpo);
    }

    public Nodo parseExpr() {
        Nodo nodo = parseTerm();

        while (current.getAtributo() == TokenType.SUM || current.getAtributo() == TokenType.SUB) {
            Token op = current;   
            match(op.getAtributo());   
            Nodo der = parseTerm(); 
            nodo = new NodoOperador(op.getLexema().charAt(0), nodo, der);
        }
        return nodo;
    }

    // TÉRMINOS (Multiplicaciones y Divisiones)
    private Nodo parseTerm() {
        Nodo nodo = parseFactor();

        while (current.getAtributo() == TokenType.MUL || current.getAtributo() == TokenType.DIV) {
            Token op = current;
            match(op.getAtributo());
            Nodo der = parseFactor();
            nodo = new NodoOperador(op.getLexema().charAt(0), nodo, der);
        }
        return nodo;
    }

    // FACTORES (Identificadores, Números o Paréntesis)
    private Nodo parseFactor() {
        if (current.getAtributo() == TokenType.ID) {
            String val = current.getValue(); 
            match(TokenType.ID);
            return new NodoID(val);

        } else if (current.getAtributo() == TokenType.INT_LITERAL) {
            String val = current.getValue();
            match(TokenType.INT_LITERAL);
            return new NodoNumerico(Integer.parseInt(val));

        } else if (current.getAtributo() == TokenType.FLOAT_LITERAL) {
            throw new RuntimeException("Evaluación de flotantes aún no implementada en Nodos");

        } else if (current.getAtributo() == TokenType.LPARENT) {
            match(TokenType.LPARENT); 
            Nodo nodo = parseExpr();  
            match(TokenType.RPARENT); 
            return nodo;

        } else {
            throw new RuntimeException("Error en factor: Token inesperado " + current);
        }
    }
}