
package exnihilum.com.au.listdataviewer.pojo.rings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FieldAliases {

    @SerializedName("NAME")
    @Expose
    private String nAME;
    @SerializedName("POSTCODE")
    @Expose
    private String pOSTCODE;

    public String getNAME() {
        return nAME;
    }

    public void setNAME(String nAME) {
        this.nAME = nAME;
    }

    public String getPOSTCODE() {
        return pOSTCODE;
    }

    public void setPOSTCODE(String pOSTCODE) {
        this.pOSTCODE = pOSTCODE;
    }

}
