
package exnihilum.com.au.listdataviewer.pojo.points;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("NAME")
    @Expose
    private String nAME;
    @SerializedName("ADDRESS")
    @Expose
    private String aDDRESS;

    public String getNAME() {
        return nAME;
    }

    public void setNAME(String nAME) {
        this.nAME = nAME;
    }

    public String getADDRESS() {
        return aDDRESS;
    }

    public void setADDRESS(String aDDRESS) {
        this.aDDRESS = aDDRESS;
    }

}
