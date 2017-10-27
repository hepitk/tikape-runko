package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.AnnosRaakaAine;

public class AnnosRaakaAineDao implements Dao<AnnosRaakaAine, Integer> {

    private Database database;

    public AnnosRaakaAineDao(Database database) {
        this.database = database;
    }

    @Override
    public AnnosRaakaAine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        Integer annos = rs.getInt("annos_id");
        Integer raakaAine = rs.getInt("raakaaine_id");

        AnnosRaakaAine t = new AnnosRaakaAine(id, annos, raakaAine, rs.getInt("jarjestys"), rs.getString("maara"), rs.getString("ohje"));

        rs.close();
        stmt.close();
        connection.close();

        return t;
    }

    @Override
    public List<AnnosRaakaAine> findAll() throws SQLException {
        //ei toteutettu        
        return null;
    }

    @Override
    public AnnosRaakaAine saveOrUpdate(AnnosRaakaAine object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO AnnosRaakaAine (annos_id, raakaaine_id, jarjestys,maara,ohje) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, object.getAnnosId());
            stmt.setInt(2, object.getRaakaAineId());
            stmt.setInt(3, object.getJarjestys());
            stmt.setString(4, object.getMaara());
            stmt.setString(5, object.getOhje());
            stmt.executeUpdate();
        }

        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

    // Etsii ja palauttaa tietyn annoksen ja raaka-aineen yhdistavan liitostaulun tiedot
    public List<AnnosRaakaAine> etsiAnnosRaakaAineet(Integer annosRaakaAineId) throws SQLException {
        String query = "SELECT AnnosRaakaAine.id, AnnosRaakaAine.annos_id, AnnosRaakaAine.raakaaine_id, AnnosRaakaAine.jarjestys, AnnosRaakaAine.maara, AnnosRaakaAine.ohje FROM AnnosRaakaAine\n"
                + "              WHERE AnnosRaakaAine.annos_id = ?";

        List<AnnosRaakaAine> annosRaakaaineet = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, annosRaakaAineId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                annosRaakaaineet.add(new AnnosRaakaAine(result.getInt("id"), result.getInt("annos_id"), result.getInt("raakaaine_Id"), result.getInt("jarjestys"), result.getString("maara"), result.getString("ohje")));
            }
        }

        return annosRaakaaineet;
    }

}
