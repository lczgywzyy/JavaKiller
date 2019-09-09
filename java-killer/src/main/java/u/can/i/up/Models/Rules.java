package u.can.i.up.Models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Set;

public class Rules
{
    @SerializedName("API")
    private Set<API> api_list;

    private HashMap<String, Set<API>> api_map = new HashMap<>();

    public Set<API> getApiList() {
        return api_list;
    }

    public void setApiList(Set<API> api_list) {
        this.api_list = api_list;
    }

    public HashMap<String, Set<API>> getApiMap() {
        return api_map;
    }

    public void setApiMap(HashMap<String, Set<API>> api_map) {
        this.api_map = api_map;
    }
}