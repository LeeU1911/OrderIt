package orderit.mainapp.model;

/**
 * Created by Blackcool on 3/3/16.
 */
public class BillTotalChildItem {
    // Variables
    private String  childName;
    private String  value;

    public BillTotalChildItem(String childName, String value) {
        this.childName = childName;
        this.value = value;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String orderCnt) {
        this.value = value;
    }
}
