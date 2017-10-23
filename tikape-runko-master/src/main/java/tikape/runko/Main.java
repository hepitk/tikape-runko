package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.domain.RaakaAine;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:smoothiet.db");
        database.init();

        RaakaAineDao raakaAineDao = new RaakaAineDao(database);
        ArrayList<String> aineet = new ArrayList<>();

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/raakaaineet", (req, res) -> {
            HashMap map2 = new HashMap<>();
            map2.put("raakaaineet", raakaAineDao.findAll());
            return new ModelAndView(map2, "raakaaineet");
        }, new ThymeleafTemplateEngine());

        /*Spark.get("/raakaaineet", (req, res) -> {
            String lisaaAine = "";
            for (String aine : aineet) {
                lisaaAine += aine + "<br/>";
            }

            return lisaaAine
                    + "<form method=\"POST\" action=\"/lisaaAine\">\n"
                    + "Raaka-aine:<br/>\n"
                    + "<input type=\"text\" name=\"raaka-aine\"/><br/>\n"
                    + "<input type=\"submit\" value=\"Lisää raaka-aine\"/>\n"
                    + "</form>";
        });*/

        /*get("/raakaaineet/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaine", raakaAineDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "raakaaine");
        }, new ThymeleafTemplateEngine());*/

        Spark.post("/raakaaineet", (req, res) -> {
            RaakaAine aine = new RaakaAine(-1, req.queryParams("nimi"));
            raakaAineDao.saveOrUpdate(aine);
            res.redirect("/raakaaineet");
            return "";
        });

    }
}
