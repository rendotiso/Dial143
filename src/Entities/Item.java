package Entities;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Item {

    private String   name;
    private String   description;
    private String   iconPath;  
    private int      quantity;
    private int      price;
    private ItemType type;
    private EffectType effectType;
    private int      effectValue;

    public enum ItemType   { CONSUMABLE, COSMETIC, SPECIAL }

    public enum EffectType {
        PP_MULTIPLIER,     
        HINT_PER_CALL,      
        TIMER_BOOST,       
        LP_FLAT,        
        LP_MULTIPLIER_DAILY, 
        NONE
    }

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

    public Item(String name, String description, String iconPath,
                int quantity, int price, ItemType type) {
        this(name, description, iconPath, quantity, price, type, EffectType.NONE, 0);
    }
    
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
    public void setName(String name)     { this.name = name; }
    public void setDescription(String d) { this.description = d; }
    public void setIconPath(String p)    { this.iconPath = p; }
    public void setQuantity(int q)       { this.quantity = Math.max(0, q); }
    public void setPrice(int price)      { this.price = price; }
    public void setType(ItemType type)   { this.type = type; }

    // ── Helpers ───────────────────────────────────────────────────────────────
    public boolean use() {
        if (quantity <= 0) return false;
        quantity--;
        return true;
    }

    public void addQuantity(int amount) { this.quantity += amount; }
    public ImageIcon getIcon(int width, int height) {
        if (iconPath == null || iconPath.isEmpty()) return null;
        try {
            InputStream stream = Item.class.getResourceAsStream(iconPath);
            if (stream == null) return null;
            BufferedImage img = ImageIO.read(stream);
            if (img == null) return null;
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
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