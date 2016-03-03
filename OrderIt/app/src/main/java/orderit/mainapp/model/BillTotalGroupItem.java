package orderit.mainapp.model;

/**
 * Created by Blackcool on 3/3/16.
 */
public class BillTotalGroupItem {
    // Variables
    private String  titleItem;
    private int     totalValue;
    private boolean isHeader;

    public String getTitleItem() {
        return titleItem;
    }

    public void setTitleItem(String titleItem) {
        this.titleItem = titleItem;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }
}
