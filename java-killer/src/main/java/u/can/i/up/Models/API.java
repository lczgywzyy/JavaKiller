package u.can.i.up.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class API {

    @SerializedName("ruleId")
    private int ruleId;

    @SerializedName("apiName")
    private String apiName;

    @SerializedName("declaringClass")
    private String declaringClass;

    @SerializedName("description")
    private String description;

    @SerializedName("parameterTypes")
    private List<ParameterType> parameterTypes;

    @SerializedName("returnType")
    private String returnType;

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(String declaringClass) {
        this.declaringClass = declaringClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ParameterType> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(List<ParameterType> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
}
