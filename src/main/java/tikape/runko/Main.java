package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
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

        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", annosDao.findAll());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.get("/raakaaineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaineet", raakaAineDao.findAll());
            return new ModelAndView(map, "raakaaineet");
        }, new ThymeleafTemplateEngine());

        Spark.get("/smoothiet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothiet", annosDao.findAll());
            map.put("raakaaineet", raakaAineDao.findAll());

            return new ModelAndView(map, "smoothiet");
        }, new ThymeleafTemplateEngine());

        Spark.get("/smoothiet/:id", (req, res) -> {
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

        Spark.post("/smoothiet", (req, res) -> {
            Annos annos = new Annos(-1, req.queryParams("nimi"));
            annosDao.saveOrUpdate(annos);

            res.redirect("/smoothiet");
            return "";
        });

        // Muokkaa smoothien reseptin eli muokkaa liitostaulua
        Spark.post("/smoothiet2", (req, res) -> {
            Integer smoothieId = Integer.parseInt(req.queryParams("smoothieId"));
            Integer raakaAineId = Integer.parseInt(req.queryParams("raakaaineId"));
            Integer jarjestysId = Integer.parseInt(req.queryParams("jarjestysId"));
            String maaraId = req.queryParams("maaraId");
            String ohjeId = req.queryParams("ohjeId");

            AnnosRaakaAine ta = new AnnosRaakaAine(-1, smoothieId, raakaAineId, jarjestysId, maaraId, ohjeId);
            annosRaakaAineDao.saveOrUpdate(ta);

            res.redirect("/smoothiet");
            return "";
        });

        // Poistaa smoothien
        Spark.post("/poistaAnnos", (req, res) -> {
            Integer poistaAnnosId = Integer.parseInt(req.queryParams("poistaAnnosId"));
            annosDao.delete(poistaAnnosId);

            res.redirect("/smoothiet");
            return "";
        });

        // Poistaa raaka-aineen
        Spark.post("/poistaRaakaAine/:id", (req, res) -> {
            Integer raakaAineId = Integer.parseInt(req.params(":id"));
            raakaAineDao.delete(raakaAineId);

            res.redirect("/raakaaineet");
            return "";
        });

    }
}
