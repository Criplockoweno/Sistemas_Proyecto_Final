public class Product {
    private String name;
    private int basePrice;
    private boolean isSold;

    public Product(String name, int basePrice) {
        this.name = name;
        this.basePrice = basePrice;
        this.isSold = false;
    }
    public void setbasePrice(int basePrice) {
        this.basePrice = basePrice;
    }
    public int getbasePrice() {
        return basePrice;
    }
    public void setSold(boolean isSold) {
        this.isSold = isSold;
    }
    public boolean getSold() {
        return isSold;
    }
    public String getName() {
        return name;
    }
    public String toString() {
        return name + " " + basePrice + " " + isSold;
    }
}
