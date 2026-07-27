package Shop;

public class XpBoost extends Item {

    private int xpPoint;

    public XpBoost(String _name, int _price, String _description, int _xpPoint) {
        super(_name, _price, _description);
        this.xpPoint = _xpPoint;
    }

    public int getXpPoint() {
        return this.xpPoint;
    }
}
