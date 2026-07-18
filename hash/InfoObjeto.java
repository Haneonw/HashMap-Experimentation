package hash;
public class InfoObjeto {
    private int chave;
    private String info;

    InfoObjeto(int chave, String info){
        this.chave = chave;
        this.info = info;
    }

    public int getChave() {
        return chave;
    }

    public String getInfo() {
        return info;
    }
}
