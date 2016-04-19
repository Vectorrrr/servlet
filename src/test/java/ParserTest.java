import org.junit.Test;
import parser.RequestParser;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Ivan Gladush
 * @since 18.04.16.
 */
public class ParserTest {
    @Test
    public void get_bounder_test1(){
        assertEquals("------WebKitFormBoundaryXQPV7Y7QT1nxne5K", RequestParser.getBounder(" ------WebKitFormBoundaryXQPV7Y7QT1nxne5K \n" +
                "Content-Disposition: form-data; name=\"data\";" +
                " filename=\"vanya.txt\" " +
                "Content-Type: text/plain sdfsdf" +
                " ------WebKitFormBoundaryXQPV7Y7QT1nxne5K--"));
    }
    @Test
    public void get_bounder_test2(){
        assertEquals("------WebKitFormBoundary66LL8VC26oRxyJxp", RequestParser.getBounder("------WebKitFormBoundary66LL8VC26oRxyJxp\n" +
                " Content-Disposition: form-data; name=\"data\"; " +
                "filename=\"vanya.txt\" " +
                "Content-Type: text/plain" +
                " sdfsdf ------WebKitFormBoundary66LL8VC26oRxyJxp--"));
    }
    @Test
    public void get_not_exist_bounder(){
        assertEquals("", RequestParser.getBounder("\n" +
                " Content-Disposition: form-data; name=\"data\"; " +
                "filename=\"vanya.txt\" " +
                "Content-Type: text/plain" +
                " sdfsdf ------WebKitFormBoundary66LL8VC26oRxyJxp--"));
    }
    @Test
    public void get_file_name_test1(){
        assertEquals("vanya.txt", RequestParser.getFileName("Hello ------WebKitFormBoundary66LL8VC26oRxyJxp" +
                " Content-Disposition: form-data; name=\"data\"; " +
                "filename=\"vanya.txt\"" +
                " Content-Type: text/plain" +
                " sdfsdf ------WebKitFormBoundary66LL8VC26oRxyJxp--"));
    }
    @Test
    public void get_file_name_no_extension_test1(){
        assertEquals("SDSD", RequestParser.getFileName("Hello ------WebKitFormBoundary66LL8VC26oRxyJxp" +
                " Content-Disposition: form-data; name=\"data\"; " +
                "filename=\"SDSD\" " +
                "Content-Type: text/plain" +
                " sdfsdf ------WebKitFormBoundary66LL8VC26oRxyJxp--"));
    }
    @Test
    public void get_not_exist_file_name_test1(){
        assertEquals("", RequestParser.getFileName("Hello ------WebKitFormBoundary66LL8VC26oRxyJxp" +
                " Content-Disposition: form-data; name=\"data\"; " +
                "\" Content-Type: text/plain" +
                " sdfsdf ------WebKitFormBoundary66LL8VC26oRxyJxp--"));
    }
    @Test
    public void get_file_name_no_extension_test2(){
        assertEquals("", RequestParser.getFileName("Hello ------WebKitFormBoundary66LL8VC26oRxyJxp" +
                " Content-Disposition: form-data; name=\"data\"; " +
                "filename=" +
                "\"\"" +
                " Content-Type: text/plain" +
                " sdfsdf ------WebKitFormBoundary66LL8VC26oRxyJxp--"));
    }


}
