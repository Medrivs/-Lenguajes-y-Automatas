package Parser;
import java.util.ArrayList;
import java.util.List;

public abstract class Nodo {
    public abstract float evaluar();
}

class NodoNumerico extends Nodo {
    private int valor; 
    
    public NodoNumerico(int valor){ 
        this.valor = valor; 
    }
    
    @Override 
    public float evaluar(){ 
        return valor; 
    }
}

class NodoOperador extends Nodo {
    private char operador; 
    private Nodo izq;      
    private Nodo der;      
    
    public NodoOperador(char operador, Nodo izq, Nodo der){
        this.operador = operador; 
        this.izq = izq; 
        this.der = der;
    }
    
    @Override 
    public float evaluar(){
        switch(this.operador){
            case '+': return this.izq.evaluar() + this.der.evaluar();
            case '-': return this.izq.evaluar() - this.der.evaluar();
            case '*': return this.izq.evaluar() * this.der.evaluar();
            case '/': return this.izq.evaluar() / this.der.evaluar();
            default : throw new RuntimeException("Operador ilegal...");    
        }
    }
}

class NodoID extends Nodo {
    private String lexema; 
    
    public NodoID(String lexema){ 
        this.lexema = lexema; 
    }
    
    @Override 
    public float evaluar(){ 
        // El integrante 3 que hara el semantico tendra que actualizar este metodo 
        return 0; 
    }
}

class NodoAsignacion extends Nodo {
    private String id; 
    private char op;   
    private Nodo der;  
    
    public NodoAsignacion(char op, String id, Nodo der){
        this.op = op; 
        this.id = id; 
        this.der = der;
    }
    
    @Override 
    public float evaluar(){ 
        return der.evaluar(); 
    }
}

class NodoIf extends Nodo {
    private Nodo condicion;
    private Nodo ramaVerdadera;
    private Nodo ramaFalsa; 

    public NodoIf(Nodo condicion, Nodo ramaVerdadera, Nodo ramaFalsa) {
        this.condicion = condicion;
        this.ramaVerdadera = ramaVerdadera;
        this.ramaFalsa = ramaFalsa;
    }

    @Override
    public float evaluar() {
        if (condicion.evaluar() != 0) {
            return ramaVerdadera.evaluar();
        } else if (ramaFalsa != null) {
            return ramaFalsa.evaluar();
        }
        return 0;
    }
}

class NodoWhile extends Nodo {
    private Nodo condicion;
    private Nodo cuerpo;

    public NodoWhile(Nodo condicion, Nodo cuerpo) {
        this.condicion = condicion;
        this.cuerpo = cuerpo;
    }

    @Override
    public float evaluar() {
        while (condicion.evaluar() != 0) {
            cuerpo.evaluar();
        }
        return 0;
    }
}

class NodoBlock extends Nodo {
    private List<Nodo> sentencias;

    public NodoBlock() {
        this.sentencias = new ArrayList<>();
    }

    public void agregarSentencia(Nodo sentencia) {
        this.sentencias.add(sentencia);
    }

    @Override
    public float evaluar() {
        float ultimoValor = 0;
        for (Nodo sentencia : sentencias) {
            ultimoValor = sentencia.evaluar(); // Evalúa cada sentencia en orden
        }
        return ultimoValor; 
    }
}