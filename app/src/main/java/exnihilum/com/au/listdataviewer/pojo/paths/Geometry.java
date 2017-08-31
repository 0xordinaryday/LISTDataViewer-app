
package exnihilum.com.au.listdataviewer.pojo.paths;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("paths")
    @Expose
    private List<List<List<Double>>> paths = null;

    public List<List<List<Double>>> getPaths() {
        return paths;
    }

    public void setPaths(List<List<List<Double>>> paths) {
        this.paths = paths;
    }

}
