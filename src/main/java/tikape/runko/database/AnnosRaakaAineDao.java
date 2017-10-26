/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Annos;
import tikape.runko.domain.RaakaAine;
import tikape.runko.domain.AnnosRaakaAine;

public class AnnosRaakaAineDao implements Dao<AnnosRaakaAine, Integer> {

    private Database database;
    //private Dao<Annos, Integer> annosDao;
    //private Dao<RaakaAine, Integer> raakaAineDao;

    public AnnosRaakaAineDao(Database database) {
        this.database = database;
        //this.annosDao = annosDao;
        //this.raakaAineDao = raakaAineDao;
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

        AnnosRaakaAine t = new AnnosRaakaAine(id, annos, raakaAine, rs.getInt("jarjestys"), rs.getInt("maara"), rs.getString("ohje"));

        rs.close();
        stmt.close();
        connection.close();

        return t;
    }

    @Override
    public List<AnnosRaakaAine> findAll() throws SQLException {
        //ei toteutettu
        List<AnnosRaakaAine> annokset = new ArrayList<>();
        return annokset;
        /*
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<AnnosRaakaAine> annokset = new ArrayList<>();
        while (rs.next()) {
            Annos annos = annosDao.findOne(rs.getInt("annos_id"));
            Integer jarjestys = rs.getInt("jarjestys");
            Integer maara = rs.getInt("maara");
            String ohje = rs.getString("ohje");

            annokset.add(new AnnosRaakaAine(annos,jarjestys, maara, ohje));
        }

        rs.close();
        stmt.close();
        connection.close();

        return annokset;*/
    }

    @Override
    public AnnosRaakaAine saveOrUpdate(AnnosRaakaAine object) throws SQLException {
          try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO AnnosRaakaAine (annos_id, raakaaine_id, jarjestys,maara,ohje) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, object.getAnnosId());
            stmt.setInt(2, object.getRaakaAineId());
            stmt.setInt(3, object.getJarjestys());
            stmt.setInt(4, object.getMaara());
            stmt.setString(5, object.getOhje());
            stmt.executeUpdate();
        }

        return null;
    }
    
    // simply support saving -- disallow saving if task with 
    // same name exists

    /*   AnnosRaakaAine byName = findByName(object.getOhje());

        if (byName != null) {
            return byName;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO AnnosRaakaAine (annos) VALUES (?)");
            stmt.setString(1, object.getOhje());
            stmt.executeUpdate();
        }

        return findByName(object.getOhje());
    }

    private AnnosRaakaAine findByName(String nimi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT annos.nimi FROM AnnosRaakaAine WHERE annos.nimi = ?");
            stmt.setString(1, nimi);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }
            
            return new AnnosRaakaAine(result.getInt("id"), result.getString("nimi"));
        }
    }*/
    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }
    
    public List<AnnosRaakaAine> etsiAnnosRaakaAineet(Integer annosRaakaAineId) throws SQLException {
        String query = "SELECT AnnosRaakaAine.id, AnnosRaakaAine.annos_id, AnnosRaakaAine.raakaaine_id, AnnosRaakaAine.jarjestys, AnnosRaakaAine.maara, AnnosRaakaAine.ohje FROM AnnosRaakaAine\n"
                + "              WHERE AnnosRaakaAine.annos_id = ?";                

        List<AnnosRaakaAine> annosRaakaaineet = new ArrayList<>();
        

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, annosRaakaAineId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                annosRaakaaineet.add(new AnnosRaakaAine(result.getInt("id"), result.getInt("annos_id"), result.getInt("raakaaine_Id"), result.getInt("jarjestys"), result.getInt("maara"), result.getString("ohje")));                
            }
        }

        return annosRaakaaineet;
    }

}
