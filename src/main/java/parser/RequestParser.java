package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author root
 * @since 18.04.16.
 */
public class RequestParser {
    private static final Pattern PATTERN_FOR_BOUNDER =Pattern.compile("(.*)");
    private static final Pattern PATTERN_FOR_FILE_NAME=Pattern.compile("(?<=filename=\")(.*?)(?=\")");

    private static final String PATTERN_FOR_TEXT_CONTENT = "(?<=Content-Type:)((.*|\n)*)";
    private static final String  EMPTY_STRING="";
    private static final String CONTENT_TYPE = "Content-Type: ";

    public static String getBounder(String request){
        Matcher matcher=PATTERN_FOR_BOUNDER.matcher(request);
        if(matcher.find()){
            return matcher.group(1).trim();
        }
        return EMPTY_STRING;
    }

    public static String getFileName(String request){
        Matcher matcher=PATTERN_FOR_FILE_NAME.matcher(request);
        if(matcher.find()){
            return matcher.group(1);
        }
        return EMPTY_STRING;
    }
    public static String getFileContent(String request,String bounder){

        request =request.split(CONTENT_TYPE)[1];
        request=request.split(bounder)[0].trim();
        request=request.substring(request.indexOf("\r\n\r\n")+4);
        return request;
    }

}
