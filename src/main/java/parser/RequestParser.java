package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ivan Gladush
 * @since 18.04.16.
 */
public class RequestParser {
    private static final Pattern PATTERN_FOR_BOUNDER =Pattern.compile("(.*)");
    private static final Pattern PATTERN_FOR_FILE_NAME=Pattern.compile("(?<=filename=\")(.*?)(?=\")");

    private static final String  EMPTY_STRING="";

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

}
