package exnihilum.com.au.listdataviewer;

/**
 * Created by david on 29/07/2017.
 *
 */

public class LayerType {

    private String classification;
    private String layerName;
    private String geometryType;
    private String layerID;
    private String param1;
    private String param2;
    private String param3;

    public LayerType(String classification, String layerName, String geometryType, String layerID, String param1, String param2) {
        this.classification = classification;
        this.layerName = layerName;
        this.geometryType = geometryType;
        this.layerID = layerID;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = null;
    }

    public LayerType(String classification, String layerName, String geometryType, String layerID, String param1, String param2, String param3) {
        this.classification = classification;
        this.layerName = layerName;
        this.geometryType = geometryType;
        this.layerID = layerID;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }

    String getClassification() {
        return classification;
    }

    String getLayerName() {
        return layerName;
    }

    String getGeometryType() {
        return geometryType;
    }

    String getLayerID() {
        return layerID;
    }

    String getParam1() {
        return param1;
    }

    String getParam2() {
        return param2;
    }

    String getParam3() {
        return param3;
    }

    @Override
    public String toString() {
        return "LayerType{" +
                "classification='" + classification + '\'' +
                ", layerName='" + layerName + '\'' +
                ", geometryType='" + geometryType + '\'' +
                ", layerID='" + layerID + '\'' +
                ", param1='" + param1 + '\'' +
                ", param2='" + param2 + '\'' +
                '}';
    }

    public boolean containsName(String checkName) {
        return this.layerName.equals(checkName);
    }
}
