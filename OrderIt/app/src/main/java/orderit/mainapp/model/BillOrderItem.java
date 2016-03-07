package orderit.mainapp.model;

import java.io.Serializable;

/**
 * Created by Blackcool on 2/28/16.
 */
public class BillOrderItem implements Serializable {
    private String orderName;
    private String quantity;
    private String singlePrice;

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(String singlePrice) {
        this.singlePrice = singlePrice;
    }
}
