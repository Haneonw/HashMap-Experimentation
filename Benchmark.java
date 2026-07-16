public class Benchmark{
    public static void main(String[] args){
        FuncaoHash modular = new Modular(10);

        modular.put(0, null);
        modular.put(1, null);
        modular.put(5, null);
        modular.put(15, null);
        modular.put(25, null);
        modular.put(11, null);

        System.out.print(modular.toString());
    }
}