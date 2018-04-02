package sqlexample;

import java.sql.*;
import java.util.Properties;
import java.util.Random;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

public class SQLProgramDatabaseExample 
{
    public static void run()
    {
        System.out.println("---=== SQLProgramDatabaseExample ===---");

        try
        {   
            // Ikrauname is anksto paruosta uzklausa sukurti duomenu bazei ir lentelei, jei ju nera.
            String[] startupSequence = loadStartupSequence();
            
            if (startupSequence == null)
                throw new IOException("Nerastas programos startup failas");
            
            // Pasinaudojame java.util.Properties vartotojo vardui ir slaptazodziui nustatyti.
            Properties properties = new Properties();
            
            properties.setProperty("user", "root");
            properties.setProperty("password", "");
            
            // Prisijungiame prie lokalaus serverio ir nurodome duomenu bazes pavadinima.
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost", properties);
            
            // Susikuriame teiginio/uzklausos sablona.
            Statement statement = connection.createStatement();
            
            // Pridedame uzklausos fragmentus po viena prie uzklausu serijos.
            for (String query : startupSequence)
                statement.addBatch(query);
            
            // Sukuriame irasa su atsitiktiniais duomenimis ir data.
            Random random = new Random();
            char[] randomChars = new char[45];
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            
            for (int i = 0; i < randomChars.length; ++i)
                randomChars[i] = alphabet.charAt(random.nextInt(alphabet.length()));
            
            String randomString = new String(randomChars);          
            statement.addBatch(String.format("INSERT INTO `random` (`random_int`, `random_double`, `random_string`, `entry_date`) VALUES ('%d', '%f', '%s', '%s');", random.nextInt(), random.nextDouble(), randomString, LocalDateTime.now()));
            
            statement.executeBatch();
            statement.clearBatch();

            // Ivykdome uzklausa patikrinti irasytiems duomenims.
            ResultSet result = statement.executeQuery("SELECT * FROM `random`;");

            // Kol yra rezultatu, paimame visu stulpeliu vertes ir isspausdiname specifiniu formatu.
            int counter = 0;
            while(result.next())
            {
                int random_int = result.getInt("random_int");
                double random_double = result.getDouble("random_double");
                String random_string = result.getString("random_string");
                
                // Noredami suzinoti tikslu laika atskirai prasome datos ir laiko is iraso laiko stulpelio.
                Date entry_date = result.getDate("entry_date");
                Time entry_date_time = result.getTime("entry_date");
                
                System.out.printf("%d | %d | %f | %s | %s %s |%n", counter++, random_int, random_double, random_string, entry_date, entry_date_time);
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
    
    private static String[] loadStartupSequence()
    {
        try
        {
            String filename = "programDatabaseExample_startup.sql";
            FileReader reader = new FileReader(filename);

            int fileSize = 0;
            while(reader.read() != - 1)
                fileSize++;

            reader = new FileReader(filename);
            char[] temporaryBuffer = new char[fileSize];
            int readStuff = reader.read(temporaryBuffer);
            
            if (readStuff != fileSize)
                throw new IOException("Nesutampa nuskaitytu ir numatytu simboliu skaicius");
            
            return new String(temporaryBuffer).split("\n");
        }
        catch(IOException e)
        {
            System.out.println(e);
        }

        // Funkcijai suveikus sio kodo programa nepasieks.
        return null;
    }
}
