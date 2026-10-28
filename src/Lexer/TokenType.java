package Lexer;

public enum TokenType {
    SUM,          
    SUB,          
    MUL,          
    DIV,          
    LPARENT,      
    RPARENT,      
    SEMICOLON,    
    DOT,          
    COMMA,        
    COLON,        
    
    INT,          
    FLOAT,        
    STRING,       
    ID,           
    
    EQUAL,        
    ASSIGN,       // :=
    GREATER_THAN, 
    LESS_THAN,    
    
    INT_LITERAL,    
    FLOAT_LITERAL,  
    STRING_LITERAL, 
    
    PROGRAM,      
    VAR,          
    INTEGER,      
    REAL,         
    BOOLEAN,      
    BEGIN,        
    END,          
    IF,           
    THEN,         
    ELSE,         
    WHILE,        
    DO,           

    EOF           
}