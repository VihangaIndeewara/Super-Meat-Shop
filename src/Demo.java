import db.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class Demo {
    public static void main(String[] args) {
        String id = "P001";

        String s = id.substring(0, 1);
        int s1 = Integer.parseInt(id.substring(2));


        System.out.println(s);
        System.out.println(s1);


    }

}
