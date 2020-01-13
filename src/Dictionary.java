import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

class Dictionary {

    private static Map<String, int[]> Words;
    private static Map<String, int[]> ClassesInfo;
    private static List<String> Classes;
    private static Integer CountClasses;
    private static Integer CountDoc;

    Dictionary(String[] Headers){
        CountDoc = 0;
        Classes = Arrays.asList(Headers);
        Words = new HashMap<>();
        ClassesInfo = new HashMap<>();
        CountClasses = Classes.size();
        for (String TextClass: Classes){
            ClassesInfo.put(TextClass, new int[CountClasses]);
        }
    }

    void putWord(String word, String TextClass){
        if (!Words.containsKey(word))
            Words.put(word, new int[CountClasses]);
        if (Words.get(word)[Classes.indexOf(TextClass)] == 0)
            ClassesInfo.get(TextClass)[0]++;
        Words.get(word)[Classes.indexOf(TextClass)]++;
    }
    /**|V| - количество уникальных слов во всех документах обучающей выборки*/
    Integer Size(){
        return Words.size();
    }
    /**D - количество документов в обучающей выборки*/
    Integer getCountDoc(){
        return CountDoc;
    }
    /**L<sub>c</sub> суммарное количество слов в документах класса <strong>TextClass</strong> в обучающей выборке*/
    Integer ClassWordCount(String TextClass){
        return ClassesInfo.get(TextClass)[0];
    }
    /**D<sub>c</sub> - количество строк в обучающей выборке принадлежащих классу  <strong>TextClass</strong>*/
    Integer ClassDocCount(String TextClass){
        return ClassesInfo.get(TextClass)[1];
    }
    void AddClassDoc(String TextClass){
        ClassesInfo.get(TextClass)[1]++;
        CountDoc++;
    }
    /**W<sub>ic</sub> - сколько раз слово <strong>word</strong> встречалось в документах класса <strong>TextClass</strong> в обучающей выборке*/
    Integer WordInClassCount(String word, String TextClass){
        if (Words.containsKey(word))
            return Words.get(word)[Classes.indexOf(TextClass)];
        else return 0;
    }

    void printDictionary(String path, Charset charset){
        try(PrintWriter writer = new PrintWriter(path, charset)){
            writer.printf("%-25s", "Словарь");
            for (String TextClass: Classes)
                writer.printf("%-10s", TextClass);
            writer.println();
            for (String word: Words.keySet()) {
                writer.printf("%-25s", word);
                for (int i = 0; i < CountClasses; i++)
                    writer.printf("%-10d", Words.get(word)[i]);
                writer.println();
            }
            writer.printf("%-25s", "Words");
            for (int i = 0; i < CountClasses; i++)
                writer.printf("%-10d", ClassesInfo.get(Classes.get(i))[0]);
            writer.println();
            writer.printf("%-25s", "Docs");
            for (int i = 0; i < CountClasses; i++)
                writer.printf("%-10d", ClassesInfo.get(Classes.get(i))[1]);

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
