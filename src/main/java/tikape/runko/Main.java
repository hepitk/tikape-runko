package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AnnosDao;
import tikape.runko.database.Database;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.database.AnnosRaakaAineDao;
import tikape.runko.domain.Annos;
import tikape.runko.domain.RaakaAine;
import tikape.runko.domain.AnnosRaakaAine;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:smoothiet.db");
        database.init();

        AnnosDao annosDao = new AnnosDao(database);
        ArrayList<String> annokset = new ArrayList<>();

        RaakaAineDao raakaAineDao = new RaakaAineDao(database);
        ArrayList<String> aineet = new ArrayList<>();

        AnnosRaakaAineDao annosRaakaAineDao = new AnnosRaakaAineDao(database);
        //AnnosRaakaAine t = listat.findOne(1);
        //System.out.println("Annos: " + t.getAnnos().getNimi());
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", annosDao.findAll());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/raakaaineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaineet", raakaAineDao.findAll());
            return new ModelAndView(map, "raakaaineet");
        }, new ThymeleafTemplateEngine());
        

        get("/smoothiet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer raakaAineId = Integer.parseInt(req.params(":id"));
            map.put("smoothie", annosDao.findOne(raakaAineId));
            map.put("raakaaineet", annosDao.etsiRaakaAineet(raakaAineId));            
            map.put("muut", annosRaakaAineDao.etsiAnnosRaakaAineet(raakaAineId));
            

            return new ModelAndView(map, "smoothie");
        }, new ThymeleafTemplateEngine());
        
        
        Spark.post("/raakaaineet", (req, res) -> {
            RaakaAine aine = new RaakaAine(-1, req.queryParams("nimi"));
            raakaAineDao.saveOrUpdate(aine);
            res.redirect("/raakaaineet");
            return "";
        });

        Spark.get("/smoothiet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothiet", annosDao.findAll());
            map.put("raakaaineet", raakaAineDao.findAll());

            return new ModelAndView(map, "smoothiet");
        }, new ThymeleafTemplateEngine());

        Spark.post("/smoothiet", (req, res) -> {
            Annos annos = new Annos(-1, req.queryParams("nimi"));
            annosDao.saveOrUpdate(annos);

            res.redirect("/smoothiet");
            return "";
        });

        Spark.post("/smoothiet/:id", (req, res) -> {
            Integer annosId = Integer.parseInt(req.params(":id"));
            Integer raakaAineId = Integer.parseInt(req.queryParams("raakaaineId"));

            AnnosRaakaAine ta = new AnnosRaakaAine(-1, annosId, raakaAineId, 1,1,"jotain");
            annosRaakaAineDao.saveOrUpdate(ta);

            res.redirect("/smoothiet");
            return "";
        });

    }
}
