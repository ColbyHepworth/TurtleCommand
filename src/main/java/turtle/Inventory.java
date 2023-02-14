package turtle;
import world.Item;
import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private final Map<Integer, Slot> inventory;
    private int selectedSlot;

    Inventory() {
        this.inventory = new HashMap<>();
        this.selectedSlot = 0;
    }

    public void setSelectedSlot(int slot) {
        this.selectedSlot = slot;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public Map<Integer, Slot> getInventory() {
        return inventory;
    }

    public void clearSlot(int slot) {
        if (inventory.containsKey(slot)) {
            inventory.get(slot).clearSlot();
        }
    }

    public void clearSelectedSlot() {
        if (inventory.containsKey(selectedSlot)) {
            inventory.get(selectedSlot).clearSlot();
        }
    }

    public void removeItem(Item item) {
        if (inventory.containsKey(selectedSlot)) {
            inventory.get(selectedSlot).removeItem(item);
        }
    }

    public void addItem(Item item) {
        if (inventory.containsKey(selectedSlot)) {
            inventory.get(selectedSlot).addItem(item);
        } else {
            inventory.put(selectedSlot, new Slot());
            inventory.get(selectedSlot).addItem(item);
        }
    }

    private static class Slot {

        private Map<Item, Integer> slot;

        public Slot() {
            this.slot = Map.of();
        }

        public void addItem(Item item) {
            if (slot.containsKey(item)) {
                slot.put(item, slot.get(item) + 1);
            } else {
                slot.put(item, 1);
            }
        }

        public void removeItem(Item item) {
            if (slot.containsKey(item)) {
                if (slot.get(item) > 1) {
                    slot.put(item, slot.get(item) - 1);
                } else {
                    slot.remove(item);
                }
            }
        }

        public Map<Item, Integer> getSlot() {
            return slot;
        }

        public void clearSlot() {
            this.slot = Map.of();
        }
    }
}