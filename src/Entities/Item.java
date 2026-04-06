package Entities;
import javax.swing.ImageIcon;

public class Item {
    private String name;
    private String description;
    private String iconPath;
    private int    quantity;
    private int    price;
    private ItemType type;
    private EffectType effectType;
    private int    effectValue;

    public enum ItemType {
        CONSUMABLE,
        COSMETIC,
        SPECIAL
    }

    public enum EffectType {
        PP_MULTIPLIER,      // % boost to PP earned (Coffee)
        HINT_PER_CALL,      // reveals best option once per call (Sticky Note)
        TIMER_BOOST,        // adds seconds to call timer (Clock)
        LP_FLAT,            // flat LP gain on use (Chocolate, Book)
        LP_MULTIPLIER_DAILY,// passive % LP per day (Desk Plant)
        NONE
    }

    // Full constructor
    public Item(String name, String description, String iconPath,
                int quantity, int price, ItemType type,
                EffectType effectType, int effectValue) {
        this.name        = name;
        this.description = description;
        this.iconPath    = iconPath;
        this.quantity    = quantity;
        this.price       = price;
        this.type        = type;
        this.effectType  = effectType;
        this.effectValue = effectValue;
    }

    // Backwards-compatible constructor (no effect)
    public Item(String name, String description, String iconPath,
                int quantity, int price, ItemType type) {
        this(name, description, iconPath, quantity, price, type, EffectType.NONE, 0);
    }

    // Inventory-only convenience constructor
    public Item(String name, String description, String iconPath, int quantity) {
        this(name, description, iconPath, quantity, 0, ItemType.CONSUMABLE, EffectType.NONE, 0);
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String     getName()        { return name; }
    public String     getDescription() { return description; }
    public String     getIconPath()    { return iconPath; }
    public int        getQuantity()    { return quantity; }
    public int        getPrice()       { return price; }
    public ItemType   getType()        { return type; }
    public EffectType getEffectType()  { return effectType; }
    public int        getEffectValue() { return effectValue; }
    public boolean    hasStock()       { return quantity > 0; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setName(String name)               { this.name = name; }
    public void setDescription(String d)           { this.description = d; }
    public void setIconPath(String p)              { this.iconPath = p; }
    public void setQuantity(int q)                 { this.quantity = Math.max(0, q); }
    public void setPrice(int price)                { this.price = price; }
    public void setType(ItemType type)             { this.type = type; }

    // ── Helpers ───────────────────────────────────────────────────────────────
    public boolean use() {
        if (quantity <= 0) return false;
        quantity--;
        return true;
    }

    public void addQuantity(int amount) { this.quantity += amount; }

    public ImageIcon getIcon(int width, int height) {
        try {
            java.io.InputStream stream = getClass().getResourceAsStream(iconPath);
            if (stream == null) return null;
            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(stream);
            java.awt.Image scaled = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Item{name='" + name + "', qty=" + quantity + ", price=" + price + "}";
    }
}