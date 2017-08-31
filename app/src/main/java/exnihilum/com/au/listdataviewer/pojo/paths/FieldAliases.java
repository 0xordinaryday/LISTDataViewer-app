
package exnihilum.com.au.listdataviewer.pojo.paths;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FieldAliases {

    @SerializedName("TYPE")
    @Expose
    private String tYPE;
    @SerializedName("CLASS")
    @Expose
    private String cLASS;

    public String getTYPE() {
        return tYPE;
    }

    public void setTYPE(String tYPE) {
        this.tYPE = tYPE;
    }

    public String getCLASS() {
        return cLASS;
    }

    public void setCLASS(String cLASS) {
        this.cLASS = cLASS;
    }

}
