package Shop;

public class Stone extends Item {

    private StoneType stoneType;

    public Stone(String _name, int _price, String _description, StoneType _stoneType) {
        super(_name, _price, _description);
        this.stoneType = _stoneType;
    }

    public StoneType getStoneType() {
        return this.stoneType;
    }
}
