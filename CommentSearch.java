/*
 Author: Kevin Sandoval
 Principles of Programming Languages: Project 2
 Lexical and Syntax Analysis
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SyntaxAnalysis {

    static File file;
    static FileReader fileReader;
    static LineNumberReader lnr;

    /**
     *
     * @param program: The File to be parsed through
     * @return: An ArrayList<String> which contains all comments found
     * @throws Exception
     */
    public static ArrayList<String> analyzeComments(File program) throws Exception {
        ArrayList<String> commentsList = new ArrayList<>();

        fileReader = new FileReader(program);

        lnr = new LineNumberReader(fileReader);
        String line;

        for (line = null; (line = lnr.readLine()) != null;) {
            if (line.startsWith("/*") && line.endsWith("*/")) {
                commentsList.add(line);
            } else if (line.startsWith("/*") && !line.endsWith("*/")) {
                String multilineComment = "";
                ArrayList<String> lineCount = (ArrayList<String>) Files.readAllLines(Paths.get(file.getName()));
                multilineComment += line;
                for (int i = lnr.getLineNumber(); i < lineCount.size(); i++) {
                    line = lnr.readLine();
                    if (line.endsWith("*/")) {
                        multilineComment += line;
                        break;
                    } else {
                        multilineComment += line + "\n";
                    }
                }
                commentsList.add(multilineComment);
            } else if (line.contains("/*")) {
                String comment = line.substring(line.indexOf("/*"));
                if (comment.endsWith("*/"))
                    commentsList.add(comment);
                else {
                    String multilineComment = comment;
                    ArrayList<String> lineCount = (ArrayList<String>) Files.readAllLines(Paths.get(file.getName()));
                    for (int i = lnr.getLineNumber(); i < lineCount.size(); i++) {
                        comment = lnr.readLine();
                        if (comment.endsWith("*/")) {
                            multilineComment += comment;
                            commentsList.add(multilineComment);
                            break;
                        } else {
                            multilineComment += comment + "\n";
                        }
                    }
                }
            }
        }

        fileReader.close();
        lnr.close();

        return commentsList;
    }

    public static void main(String[] args) throws Exception {

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".c Files", "c");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = new File(chooser.getSelectedFile().getPath());
        } else {
            System.out.println("ONLY .C FILES ACCEPTED.");
            chooser.cancelSelection();
            System.exit(1);
        }

        ArrayList<String> comments = analyzeComments(file);

        if (comments.size() == 0) {
            System.out.println("NO MULTI-LINE STYLE COMMENTS WERE DETECTED IN FILE \"" + file.getName() + "\"");
        } else {
            System.out.println("MULTI-LINE STYLE COMMENTS DETECTED IN FILE \"" + file.getName() + "\": [ "
                    + comments.size() + " ]" +
                    "\n__________________________________________________________________");
            for (int i = 0; i < comments.size(); i++) {
                System.out.println("COMMENT #" + (i + 1) + ":\n");
                System.out.println(
                        comments.get(i) + "\n__________________________________________________________________");
            }
        }

        System.exit(1);

    }

}
