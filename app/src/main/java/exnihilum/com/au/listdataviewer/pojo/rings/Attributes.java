
package exnihilum.com.au.listdataviewer.pojo.rings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("NAME")
    @Expose
    private String nAME;
    @SerializedName("POSTCODE")
    @Expose
    private Integer pOSTCODE;

    public String getNAME() {
        return nAME;
    }

    public void setNAME(String nAME) {
        this.nAME = nAME;
    }

    public Integer getPOSTCODE() {
        return pOSTCODE;
    }

    public void setPOSTCODE(Integer pOSTCODE) {
        this.pOSTCODE = pOSTCODE;
    }

}
