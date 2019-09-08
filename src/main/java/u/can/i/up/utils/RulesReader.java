package u.can.i.up.utils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import u.can.i.up.Models.API;
import u.can.i.up.Models.Rules;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;

public class RulesReader {

    private Logger logger = LogManager.getLogger();

    private volatile static RulesReader instance = new RulesReader();

    private RulesReader() {}

    public static RulesReader getInstance() {
        return instance;
    }

    private Rules ApiRules;

    public void initialRules() {
        Rules rulesModel = new Rules();
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader("conf/apis.json"));
            rulesModel = gson.fromJson(reader, Rules.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (API api: rulesModel.getApiList()) {
            if (rulesModel.getApiMap().get(api.getApiName()) == null) {
                rulesModel.getApiMap().put(api.getApiName(), new HashSet<>());

            }
            rulesModel.getApiMap().get(api.getApiName()).add(api);
        }
        this.ApiRules = rulesModel;
    }

    public Rules getApiRules() {
        return this.ApiRules;
    }
}
