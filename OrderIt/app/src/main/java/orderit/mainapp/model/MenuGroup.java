package orderit.mainapp.model;

/**
 * Created by LanTuan on 1/21/16.
 */
public class MenuGroup {

    // Variables
    private int     id;
    private String  name = "";
    private int     orderQuantity = 0;
    private boolean isHeader = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }
}
