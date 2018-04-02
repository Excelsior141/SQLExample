package sqlexample;

import java.sql.*;
import java.util.Properties;

public class SQLSelectExample
{
    public static void run(String limit) 
    {
        System.out.println("---=== SQLSelectExample ===---");

        try
        {   
            // Pasinaudojame java.util.Properties vartotojo vardui ir slaptazodziui nustatyti.
            Properties properties = new Properties();
            
            properties.setProperty("user", "root");
            properties.setProperty("password", "");
            
            // Prisijungiame prie lokalaus serverio ir nurodome duomenu bazes pavadinima.
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/employees", properties);
            
            // Susikuriame teiginio/uzklausos sablona.
            Statement statement = connection.createStatement();
            
            String query = "SELECT * FROM `employees`" + (limit != null ? limit : ";");
            
            // Ivykdome uzklausa ir ismatuojame laika.
            long queryStartTime = System.nanoTime();
            ResultSet result = statement.executeQuery(query);
            long queryFinishTime = System.nanoTime();
            
            float queryTime = (queryFinishTime - queryStartTime) / 1000000000f;
            
            System.out.printf("Query took %.3f seconds%n", queryTime);   
            System.out.println("  index   |   emp_no  | birth_date |   first_name   |    last_name     |  hire_date |");
            
            // Kol yra rezultatu, paimame visu stulpeliu vertes ir isspausdiname specifiniu formatu.
            int counter = 0;
            while(result.next())
            {
                int emp_no = result.getInt("emp_no");
                Date birth_date = result.getDate("birth_date");
                String first_name = result.getString("first_name");
                String last_name = result.getString("last_name");
                Date hire_date = result.getDate("hire_date");
                
                // Naudojame sistemine printf funkcija graziai isspaudinti rezultatus - tam naudojami formato specifikatoriai.
                System.out.printf("%9d | %9d | %s | %14s | %16s | %s |%n", counter++, emp_no, birth_date, first_name, last_name, hire_date);
            }
            
            // Uzdarome prisijungima ir nebenaudojamus objektus.
            result.close();
            statement.close();
            connection.close();
        }
        catch(SQLException se)
        {
           System.out.println(se);
        }
        catch(Exception e)
        {
           System.out.println(e);
        }
    }
}
