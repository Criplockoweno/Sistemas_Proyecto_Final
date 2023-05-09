
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

public class Log {

    private Stack<String> stack;
    private SimpleDateFormat dateFormat;
    private String fileName;

    public Log() {
        fileName = "default";
        this.stack = new Stack<>();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public synchronized void setName(String name) {
        //System.out.println("name is:"+name);
        fileName = name;
        //System.out.println("filename is:"+fileName);

    }

    public synchronized void add(String message) throws IOException {
        String timestamp = dateFormat.format(new Date());
        String logEntry = "[" + timestamp + "] " + message;
        stack.push(logEntry);

        writeToFile();
    }

    public synchronized void writeToFile() {
        try {
            File logsDir = new File("./logs");
            if (!logsDir.exists()) {
                logsDir.mkdir();
            }
            File logFile = new File(logsDir.getPath() + "/" + fileName + ".txt");
            FileWriter writer = new FileWriter(logFile, true);

            try ( PrintWriter printWriter = new PrintWriter(writer)) {
                while (!stack.empty()) {
                    printWriter.println(stack.pop());
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to file.");
            e.printStackTrace();
        }
    }

//
//    public synchronized void writeToFile() throws IOException {
//        
//        FileWriter fileWriter = new FileWriter(fileName, true);
//        try ( PrintWriter printWriter = new PrintWriter(fileWriter)) {
//            while (!stack.empty()) {
//                printWriter.println(stack.pop());
//            }
//        }
//    }
}
