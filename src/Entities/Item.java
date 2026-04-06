/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import javax.swing.ImageIcon;

/**
 * Represents a usable item in the game.
 * Reusable across Inventory and Shop systems.
 */
public class Item {

    private String name;
    private String description;
    private String iconPath;     // resource path e.g. "/GUI/resources/items/coffee.png"
    private int    quantity;
    private int    price;        // for Shop use
    private ItemType type;

    public enum ItemType {
        CONSUMABLE,
        COSMETIC,
        SPECIAL
    }

    // Full constructor
    public Item(String name, String description, String iconPath, int quantity, int price, ItemType type) {
        this.name        = name;
        this.description = description;
        this.iconPath    = iconPath;
        this.quantity    = quantity;
        this.price       = price;
        this.type        = type;
    }

    // Convenience constructor (no price - inventory only)
    public Item(String name, String description, String iconPath, int quantity) {
        this(name, description, iconPath, quantity, 0, ItemType.CONSUMABLE);
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String   getName()        { return name; }
    public String   getDescription() { return description; }
    public String   getIconPath()    { return iconPath; }
    public int      getQuantity()    { return quantity; }
    public int      getPrice()       { return price; }
    public ItemType getType()        { return type; }
    public boolean  hasStock()       { return quantity > 0; }

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setName(String name)               { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setIconPath(String iconPath)       { this.iconPath = iconPath; }
    public void setQuantity(int quantity)          { this.quantity = Math.max(0, quantity); }
    public void setPrice(int price)                { this.price = price; }
    public void setType(ItemType type)             { this.type = type; }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Use one unit of the item. Returns true if successful, false if out of stock. */
    public boolean use() {
        if (quantity <= 0) return false;
        quantity--;
        return true;
    }

    /** Add quantity (for shop purchases). */
    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    /**
     * Attempts to load the icon as an ImageIcon scaled to the given size.
     * Returns null if the resource is not found (caller should show placeholder).
     */
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