package orderit.mainapp.model;

/**
 * Created by LanTuan on 1/21/16.
 */
public class OrderChildItem {

    // Variables
    private String  childName;
    private int     orderCnt;

    public OrderChildItem(String childName, int orderCnt) {
        this.childName = childName;
        this.orderCnt = orderCnt;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public int getOrderCnt() {
        return orderCnt;
    }

    public void setOrderCnt(int orderCnt) {
        this.orderCnt = orderCnt;
    }
}
