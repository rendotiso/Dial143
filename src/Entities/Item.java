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
    private EffectType effectType;
    private int      effectValue;

    public enum EffectType {
        PP_MULTIPLIER,     
        HINT_PER_CALL,      
        TIMER_BOOST,       
        LP_FLAT,        
        LP_MULTIPLIER_DAILY, 
        NONE
    }

    public Item(String name, String description, String iconPath,
                int quantity, int price, EffectType effectType, int effectValue) {
        this.name        = name;
        this.description = description;
        this.iconPath    = iconPath;
        this.quantity    = quantity;
        this.price       = price;
        this.effectType  = effectType;
        this.effectValue = effectValue;
    }

    public Item(String name, String description, String iconPath,
                int quantity, int price) {
        this(name, description, iconPath, quantity, price, EffectType.NONE, 0);
    }
    
    public Item(String name, String description, String iconPath, int quantity) {
        this(name, description, iconPath, quantity, 0, EffectType.NONE, 0);
    }
    
    public String     getName()        { return name; }
    public String     getDescription() { return description; }
    public String     getIconPath()    { return iconPath; }
    public int        getQuantity()    { return quantity; }
    public int        getPrice()       { return price; }
    public EffectType getEffectType()  { return effectType; }
    public int        getEffectValue() { return effectValue; }
    public boolean    hasStock()       { return quantity > 0; }

    public void setName(String name)     { this.name = name; }
    public void setDescription(String d) { this.description = d; }
    public void setIconPath(String p)    { this.iconPath = p; }
    public void setQuantity(int q)       { this.quantity = Math.max(0, q); }
    public void setPrice(int price)      { this.price = price; }

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
}