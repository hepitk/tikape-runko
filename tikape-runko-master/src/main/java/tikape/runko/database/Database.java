package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä        
        lista.add("CREATE TABLE RaakaAine (id integer PRIMARY KEY, nimi varchar(255));");
        lista.add("CREATE TABLE Annos (id integer PRIMARY KEY, nimi varchar(255));");
        lista.add("CREATE TABLE AnnosRaakaAine (id integer PRIMARY KEY, annos_id integer, raakaaine_id integer, jarjestys integer, maara integer, ohje varchar(255), FOREIGN KEY (annos_id) REFERENCES Annos(id), FOREIGN KEY (raakaaine_id) REFERENCES RaakaAine(id));");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Banaani');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Mansikka');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Mustikka');");
        lista.add("INSERT INTO Annos (nimi) VALUES ('Makee');");
        lista.add("INSERT INTO Annos (nimi) VALUES ('Huono');");
        lista.add("INSERT INTO Annos (nimi) VALUES ('Hyoky');");
        //lista.add("INSERT INTO AnnosRaakaAine (annos_id, raakaaine_id, jarjestys, maara, ohje) VALUES (1,1,1,1,'sekoita');");

        return lista;
    }
}