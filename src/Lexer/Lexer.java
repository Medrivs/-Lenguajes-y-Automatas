package Lexer;
import java.util.function.Predicate;

public class Lexer {
    private final String entrada;
    private int pos;
    private char caracterActual;

    public Lexer(String entrada) {
        this.entrada = entrada;
        this.pos = 0;
        this.caracterActual = (entrada.length() > 0) ? entrada.charAt(0) : '\0';
    }

    private void advance() {
        this.pos++;
        caracterActual = (pos < entrada.length()) ? entrada.charAt(pos) : '\0';
    }
    
    private void saltateElEspacio() {
        while (Character.isWhitespace(caracterActual)) {
            advance();
        }
    }
    
    private String readWhile(Predicate<Character> condicion) {
        StringBuilder sb = new StringBuilder();
        while (caracterActual != '\0' && condicion.test(caracterActual)) {
            sb.append(caracterActual);
            advance();
        }
        return sb.toString();
    }
    
    private Token number() {
        StringBuilder sb = new StringBuilder();
        sb.append(readWhile(Character::isDigit));
        if (caracterActual == '.') {
            sb.append('.');
            advance();
            if (Character.isDigit(caracterActual)) {
                sb.append(readWhile(Character::isDigit));
                return new Token(sb.toString(), TokenType.FLOAT_LITERAL);
            } else {
                throw new RuntimeException("Número mal formado");
            }
        }
        return new Token(sb.toString(), TokenType.INT_LITERAL);
    }

    private boolean isValidChar(char c) {
        if (Character.isLetterOrDigit(c)) {
            return true; 
        } else if (c == '_') {
            return true; 
        } else {
            return false; 
        }
    }
    
    private Token identifier() {
        StringBuilder sb = new StringBuilder();

        while (caracterActual != '\0' && isValidChar(caracterActual)) {
            sb.append(caracterActual); 
            advance();                 
        }
        
        String id = sb.toString().toUpperCase(); // Lo pasamos a mayúscula para coincidir con Micro
        
        switch (id) {
            case "PROGRAM": return new Token(id, TokenType.PROGRAM);
            case "VAR": return new Token(id, TokenType.VAR);
            case "INTEGER": return new Token(id, TokenType.INTEGER);
            case "REAL": return new Token(id, TokenType.REAL);
            case "BOOLEAN": return new Token(id, TokenType.BOOLEAN);
            case "BEGIN": return new Token(id, TokenType.BEGIN);
            case "END": return new Token(id, TokenType.END);
            case "IF": return new Token(id, TokenType.IF);
            case "THEN": return new Token(id, TokenType.THEN);
            case "ELSE": return new Token(id, TokenType.ELSE);
            case "WHILE": return new Token(id, TokenType.WHILE);
            case "DO": return new Token(id, TokenType.DO);
        }
        // Si no es palabra reservada, asume que es el nombre de una variable (ID)
        return new Token(sb.toString(), TokenType.ID); // Devolvemos el original sin mayúsculas
    }
    
    private Token texto() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        advance(); 
        while (caracterActual != '\0' && caracterActual != '"') {
            sb.append(caracterActual);
            advance();
        }
        if (caracterActual == '"') {
            sb.append('"');
            advance();
            return new Token(sb.toString(), TokenType.STRING_LITERAL);
        } else {
            throw new RuntimeException("String no cerrado");
        }
    }
    
    public Token nextToken() {
        while (caracterActual != '\0') {
            if (Character.isWhitespace(caracterActual)) {
                saltateElEspacio();
                continue;
            }
            if (Character.isDigit(caracterActual)) {
                return number();
            } else if (Character.isLetter(caracterActual)) {
                return identifier();
            } else if (caracterActual == '"') {
                return texto();
            }
            
            switch(caracterActual){
                case '+': advance(); return new Token("+", TokenType.SUM);
                case '-': advance(); return new Token("-", TokenType.SUB);
                case '*': advance(); return new Token("*", TokenType.MUL);
                case '/': advance(); return new Token("/", TokenType.DIV); 
                case '(': advance(); return new Token("(", TokenType.LPARENT);
                case ')': advance(); return new Token(")", TokenType.RPARENT);
                case ';': advance(); return new Token(";", TokenType.SEMICOLON);
                case '.': advance(); return new Token(".", TokenType.DOT);
                case ',': advance(); return new Token(",", TokenType.COMMA);
                
                case ':': 
                    advance(); 
                    if(caracterActual == '=') {
                        advance(); 
                        return new Token(":=", TokenType.ASSIGN);
                    } else {
                        return new Token(":", TokenType.COLON); // Sirve para VAR x : INTEGER
                    }
                    
                case '=': advance(); return new Token("=", TokenType.EQUAL);
                case '>': advance(); return new Token(">", TokenType.GREATER_THAN);
                case '<': advance(); return new Token("<", TokenType.LESS_THAN);
                default : throw new RuntimeException("Char ilegal [" + caracterActual + "]");
            }
        }
        return new Token("", TokenType.EOF);
    }
}