package lexical;

public class LineColum {


    private int line;
    private int colum;

    
    public LineColum() {
        this.line = 1;
        this.colum = 0;
    }


    public void countLineAndColum(char c){

        //se nao encontrar o \n então incrementa a coluna
        if(c != '\n'){
            
            this.colum++;
            
        }else{
            this.line++;
            this.colum = 0;
        }


    }


    public void setLine(int line) {
        this.line = line;
    }


    public void setColum(int colum) {
        this.colum = colum;
    }


    public int getLine() {
        return line;
    }


    public int getColum() {
        return colum;
    }

    public void incrementLine(){
        this.line++;
    }

    public void incrementcolumn(){
        this.colum++;
    }

    public void decrementLine(){
        this.line--;   
    }

    public void decrementeColumn(){
        this.colum--;
    }



    
    
}
