package src.main.java;
import java.util.ArrayList;

import src.main.java.hash.*;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Benchmark
 * 
 * Classe responsável por executar o experimento de comparação entre as 
 * diferentes funções hash implementadas no pacote {@code src.main.java.hash}.
 * 
 * O programa lê o tamanho desejado para a tabela hash em uma entrada e um
 * arquivo de chaves (.txt ou .csv) informados pelo usuário, insere todas as chaves
 * insere todas as chaves em cada uma das funções hash disponíveis e, no final,
 * grava um arquivo CSV em resultados/ com as métricas coletadas (colisões, coeficiente
 * de vaciação, maior cadeia, e média das cadeias) para cada função testada.
 */
public class Benchmark{

    /**
     * Entrada de dados para o programa.
     * 
     * Solicita ao usuário o tamanho da tabela hash e o nome do arquivo de
     * chaves, carrega as chaves e insere-as em cada uma das funções hash
     * testadas e escreve os resultados obtidos em um arquivo CSV dentro da
     * pasta resultados/
     * 
     * @param args argumentos de linha de comando
     * @throws IOException se ocorrer um erro de leitura da entrada padrão
     * ou de escrita do arquivo de resultados
     */
    public static void main(String[] args)throws IOException{
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
        int tamanho = 0;

        // Usuário escolhe qual vai ser o tamanho fixo da tabela hash.
        while (true) {
            try {
                System.out.print("Tamanho da tabela: ");
                tamanho = Integer.parseInt(entrada.readLine());

                if (tamanho > 0)
                    break;

            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro positivo.");
            }
        }
        
        ArrayList<Integer> chaves = new ArrayList<>();
        
        // Coleta de chaves de um arquivo.
        System.out.print("Escolha um arquivo com as chaves: ");

        String arquivo = entrada.readLine();
        
        chaves = carregarChaves(arquivo);

        if (chaves == null) return;

        // Todas as funções hash que serão testadas:
        FuncaoHash modular = new Modular(tamanho);
        FuncaoHash multiplicativa = new Multiplicativa(tamanho);
        FuncaoHash meioDoQuadrado = new MeioDoQuadrado(tamanho);
        FuncaoHash murmurHash3 = new MurmurHash3(tamanho);
        FuncaoHash folding = new Folding(tamanho);
        FuncaoHash[] funcoes = {modular, multiplicativa, meioDoQuadrado,  folding, murmurHash3};

        // Inserindo as chaves em cada tabela hash.
        for(FuncaoHash funcao: funcoes){
            for(int chave: chaves){
                funcao.put(chave, null);
            }
            String titulo = " " + funcao.getNomeFunc() + " ";
            System.out.println("\n" + "=".repeat(20) + titulo + "=".repeat(20));
            System.out.println(funcao);
        }

        String nome = arquivo.substring(0, arquivo.lastIndexOf('.'));

        PrintWriter writer = new PrintWriter(new FileWriter("resultados/resultados_" + nome + "_" + tamanho+".csv"));

        writer.println("Funcao,Dataset,TamanhoTabela,Colisoes,CV,MaiorCadeia,MediaCadeia");

        for (FuncaoHash f : funcoes) {

            // executa benchmark

            writer.println(
                f.getNomeFunc() + "," +
                arquivo + "," +
                tamanho + "," +
                f.getColisoes() + "," +
                f.getCoeficienteVariacao() + "," +
                f.getMaiorCadeia() + "," +
                f.getMediaCadeia()
            );
        }

        writer.close();
   }

   /**
     * Carrega as chaves a serem inseridas nas tabelas hash a partir de um
     * arquivo localizado na pasta dados/.
     *
     * Suporta arquivos nos formatos .txt (uma chave por linha) e .csv (a
     * chave deve estar na primeira coluna). A primeira linha do arquivo é
     * sempre descartada, pois é considerada um cabeçalho.
     *
     * @param arquivo nome do arquivo (dentro da pasta dados/) contendo as chaves
     * @return uma lista com as chaves lidas, ou {@code null} caso o arquivo
     *         não seja encontrado, tenha um formato não suportado ou possua
     *         conteúdo inválido
     * @throws IOException se ocorrer um erro durante a leitura do arquivo
     */
    private static ArrayList<Integer> carregarChaves(String arquivo) throws IOException{
        FileReader arq;
        try{
            arq = new FileReader("dados/" + arquivo);
        }catch (FileNotFoundException e){
            System.out.println("Arquivo não encontrado.");
            return null;
        }

        BufferedReader leitorArq = new BufferedReader(arq);

        ArrayList<Integer> chaves = new ArrayList<>();

        String linha;
        try{
            leitorArq.readLine();
            if(arquivo.endsWith(".txt")){
                while((linha = leitorArq.readLine()) != null){
                    chaves.add(Integer.parseInt(linha));
                }
            }
            else if (arquivo.endsWith(".csv")){
                while((linha = leitorArq.readLine()) != null){
                    String[] info = linha.split(",");
                    chaves.add(Integer.parseInt(info[0]));
                }
            } 
            else {
                System.out.println("Formato não suportado.");
                fecha(arq,leitorArq);
                return null;
            }
        }catch (Exception e){

            System.out.println("Formato do arquivo inválido.");
            fecha(arq,leitorArq);
            return null;
        }
        fecha(arq,leitorArq);

        return chaves;
   }
   
    /**
     * Fecha os fluxos de leitura de arquivo utilizados para carregar as
     * chaves, ignorando silenciosamente qualquer exceção que ocorra durante
     * o fechamento.
     *
     * @param arq    o {@link FileReader} a ser fechado
     * @param leitor o {@link BufferedReader} a ser fechado
     */
    private static void fecha(FileReader arq, BufferedReader leitor){
        try{
            arq.close();
            leitor.close();
        }catch(Exception e){}
   }
}
