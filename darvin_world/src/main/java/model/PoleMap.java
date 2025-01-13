package model;

public class PoleMap extends AbstractWorldMap {
    private final float a;

    public PoleMap(int height, int width, int cost) {
        super(height, width, cost);
        this.a = (float) (4 * cost) / (height);
    }

    private int calculateCost(int height){
        return Math.round(this.a * Math.abs(height - (float) this.height / 2));
    }

    @Override
    public void move(Animal animal, MapDirection direction) {
        super.move(animal, direction);
        animal.setEnergyLevel(animal.getEnergyLevel() - calculateCost(animal.getPosition().getY()));
    }
}
