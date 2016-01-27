package orderit.mainapp.model;

/**
 * Created by LanTuan on 1/21/16.
 */
public class OrderGroupItem {

    // Variables
    private String  catName;
    private int     catType;
    private int     orderCnt;
    private boolean isHeader;

    public int getCatType() {
        return catType;
    }

    public void setCatType(int catType) {
        this.catType = catType;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getOrderCnt() {
        return orderCnt;
    }

    public void setOrderCnt(int orderCnt) {
        this.orderCnt = orderCnt;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }
}
