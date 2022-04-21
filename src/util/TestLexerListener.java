package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;

public class TestLexerListener extends BaseErrorListener {

    /**
     * Override of the syntaxError function to redirect lexical error messages to a log file
     * Once created, the file can be found in the src directory
     * @param Recognizer<?,?> the recognizer that catched the error (lexer/parser)
     * @param Object the symbol that caused the error; if the recognizer is the lexer, it is always null
     * @param int the line where the error has been found
     * @param int the position of the token than caused the error in the line
     * @param String a message containing information about the kinf of error
     * @param RecognitionException the catched exception
     * @return void
     */
    @Override
    public void syntaxError(Recognizer<?,?> recognizer,
                            Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg,
                            RecognitionException e) {

        // Date and time of when the error occured, as the file will be reused in the future
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        
        // The string containing the error to be printed
        String outError = formattedDate + ": Found Lexical error at line " + line + " at position " + charPositionInLine + ": " + msg;

        // Try printing to a file the string outError in append; if the file don't exist, create it
        try(FileWriter fw = new FileWriter("log.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(outError);
        } catch (IOException ioe) {
            System.err.println("Error writing to file");
        }
    }
}