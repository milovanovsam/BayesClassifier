import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class Main {

    public static void main(String[] args){
        String[] Headers = {"science", "style", "culture", "life", "economics", "business", "travel", "forces", "media", "sport"};
        String TrainPath = "src/news_train.txt";
        String TestPath = "src/news_test.txt";
        String AnswerPath = "src/output.txt";
        Charset charset = StandardCharsets.UTF_8;
        Dictionary KnowledgeBase = train(Headers, TrainPath, charset);
        System.out.println("Training complete.......");
        test(Headers, KnowledgeBase, TestPath, AnswerPath, charset);
        System.out.println("Testing complete........");
    }

    private static void test(String[] Headers, Dictionary Base, String TestPath, String AnswerPath, Charset charset){
        try(BufferedReader reader = Files.newBufferedReader(Paths.get(TestPath), charset);
            PrintWriter writer = new PrintWriter(AnswerPath, charset)){
            String line, AnswerClass = "";
            Double value, MaxValue = 0.0;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("[^A-Za-zА-Яа-яёЁ@#\\-]+");
                for ( String word: words)
                    word = MyCleaner(word);
                for (String head: Headers) {
                    value = Math.log(1.0*Base.ClassDocCount(head)/Base.getCountDoc());
                    for ( String word: words){
                        if (!word.isEmpty())
                            value+= Math.log(1.0*(Base.WordInClassCount(word, head)+1)/(Base.Size()+Base.ClassWordCount(head)));
                    }
                    if (head.equals(Headers[0])){
                        MaxValue = value;
                        AnswerClass = Headers[0];
                    } else if( value > MaxValue){
                        MaxValue = value;
                        AnswerClass = head;
                    }
                }
                writer.println(AnswerClass);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static Dictionary train(String[] Headers, String path, Charset charset) {
        Dictionary TrainingBase = new Dictionary(Headers);
        try(BufferedReader reader = Files.newBufferedReader(Paths.get(path), charset)){
            String line, currentClass;
            while ((line = reader.readLine()) != null){
                String[] words = line.split("[^A-Za-zА-Яа-яёЁ@#\\-]+");
                currentClass = words[0];
                TrainingBase.AddClassDoc(currentClass);
                for (int i = 1; i < words.length; i++) {
                    words[i] = MyCleaner(words[i]);
                    if (!words[i].isEmpty()) TrainingBase.putWord(words[i], currentClass);
                }
            }
            TrainingBase.printDictionary("src/dictionary.txt", charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TrainingBase;
    }

    private static String MyCleaner(String word){
        String copy = word;
        if (!copy.isEmpty())
            if ( (copy.charAt(0) == '-') || (copy.charAt(copy.length() - 1) == '-')
                    || (copy.length() < 3) || (copy.startsWith("@")) || (copy.startsWith("#")) )
                copy = "";
        return copy.toLowerCase();
    }
}
