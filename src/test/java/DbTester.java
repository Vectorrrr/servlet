import database.ConnectionFactory;
import database.DbManager;
import file.FileBean;
import org.junit.AfterClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * Before starting the test you want to change the name
 * of the database in the Property from the servlet to the test
 * If you use fields that are primary keys in the tables and text
 * fields is, they should be called as a method to avoid incorrectly test
 * @author Ivan Gladush
 * @since 26.04.16.
 */
public class DbTester {
    public static final DbManager DB_MANAGER = DbManager.getDbManager();

    @Test
    public void add_new_user_add_again_this_user_test1() {
        String name = "add_new_user_add_again_this_user_test1";
        String password = "123456";
        assertEquals(true, DB_MANAGER.addNewUser(name, password));
        assertEquals(false, DB_MANAGER.addNewUser(name, password));
    }

    @Test
    public void check_exist_login() {
        String name = "check_exist_login";
        String password = "123456";
        DB_MANAGER.addNewUser(name, password);
        assertEquals(true, DB_MANAGER.thisLoginExist(name));
    }

    /***
     * The max length tet it's 40 symbol CHeck this information in properties
     */
    @Test
    public void add_to_large_name_in_user_tables() {
        String largeName = "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiop";
        String password = "sdfsdg3w";
        assertEquals(false, DB_MANAGER.addNewUser(largeName, password));
    }

    @Test
    public void check_not_exist_login() {
        String name = "check_not_exist_login";
        assertEquals(false, DB_MANAGER.thisLoginExist(name));
    }

    @Test
    public void add_new_temp_user_test1() {
        String name = "add_new_temp_user_test1";
        String password = "sdfxctr23@";
        String secretKey = "secregdf";
        assertEquals(true, DB_MANAGER.addNewTempUser(name, password, secretKey));
        assertEquals(false, DB_MANAGER.addNewTempUser(name, password, secretKey));
    }

    @Test
    public void add_to_large_name_in_temp_user_tables_test1() {
        String name = "add_to_large_name_in_temp_user_tables_test1";
        String password = "sdf";
        String key = "sada";
        assertEquals(false, DB_MANAGER.addNewTempUser(name, password, key));
    }

    @Test
    public void add_file_in_db_with_incorrect_user_name(){
        String fileName="add_file_in_db_with_incorrect_user_name";
        String contentType="contentType";
        String notExistUserName="add_file_in_db_with_incorrect_user_name";
        assertEquals(false,DB_MANAGER.addNewFile(fileName,contentType,notExistUserName));
    }
    @Test
    public void add_new_file(){
        String user="add_new_file";
        String password="asd";
        String fileName="file-name";
        String contentType="content-Type";
        assertEquals(true,DB_MANAGER.addNewUser(user,password));
        assertEquals(true,DB_MANAGER.addNewFile(fileName,contentType,user));
    }

    @Test
    public void confirm_user_test(){
        String userName="confirm_user_test";
        String userPassword="12ewrsd@r";
        String secretKey="qwerasdfzxcvfdsa";
        assertEquals(true,DB_MANAGER.addNewTempUser(userName,userPassword,secretKey));
        assertEquals(true,DB_MANAGER.isTempUser(userName));
        assertEquals(true,DB_MANAGER.confirmUser(userName,userPassword,secretKey));
        assertEquals(false,DB_MANAGER.isTempUser(userName));
    }
    @Test
    public void incorect_confirm_user_test(){
        String userName="incorect_confirm_user_test";
        String userPassword="12ewrsd@r";
        String secretKey="qwerasdfzxcvfdsa";
        assertEquals(true,DB_MANAGER.addNewTempUser(userName,userPassword,secretKey));
        assertEquals(true,DB_MANAGER.isTempUser(userName));
        assertEquals(true,DB_MANAGER.confirmUser(userName,userPassword,secretKey+"sfd"));
        assertEquals(true,DB_MANAGER.isTempUser(userName));
    }
    @Test
    public void contains_user(){
        String name="contains_user";
        String password="123456";
        assertEquals(true,DB_MANAGER.addNewUser(name,password));
        assertEquals(true,DB_MANAGER.containsUser(name,password));
    }
    @Test
    public void get_file_bean(){
        String fileName="get_file_bean";
        String contentType="content-type";
        String userName="get_file_bean";
        String password="user";
        assertEquals(true,DB_MANAGER.addNewUser(userName,password));
        assertEquals(true,DB_MANAGER.addNewFile(fileName,contentType,userName));
        equalseFileBean(DB_MANAGER.getFileBean(fileName), new FileBean(fileName,contentType));
    }

    private void equalseFileBean(FileBean actualFileBean, FileBean excpectedFileBean) {
        assertEquals(excpectedFileBean.getFileName(),actualFileBean.getFileName());
        assertEquals(excpectedFileBean.getContentType(),actualFileBean.getContentType());
    }

    @Test
    public void is_temp_user(){
        String name="is_temp_user";
        String password="is_temp_user";
        String secretKey="Key";
        assertEquals(true,DB_MANAGER.addNewTempUser(name,password,secretKey));
        assertEquals(true,DB_MANAGER.isTempUser(name));
    }
    @AfterClass
    public static void dropTempDataBase() {
        Connection connection = ConnectionFactory.getConnection();
        try {
            connection.createStatement().executeUpdate("Drop DATABASE Test;");
        } catch (SQLException e) {
            throw new IllegalArgumentException("I can't delete db");
        }
    }
}
