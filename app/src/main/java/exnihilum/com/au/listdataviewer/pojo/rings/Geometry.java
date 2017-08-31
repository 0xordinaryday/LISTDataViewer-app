
package exnihilum.com.au.listdataviewer.pojo.rings;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("rings")
    @Expose
    private List<List<List<Double>>> rings = null;

    public List<List<List<Double>>> getRings() {
        return rings;
    }

    public void setRings(List<List<List<Double>>> rings) {
        this.rings = rings;
    }

}
