package GUI.panels.saveComponents;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Holds data for one save slot.
 * In a real implementation this would serialize to disk.
 */
public class SaveSlot {

    public static final int SLOT_COUNT = 3;

    private boolean  occupied    = false;
    private int      day         = 1;
    private int      pp          = 0;
    private int      lp          = 0;
    private int      salary      = 0;
    private String   playerName  = "";
    private String   savedAt     = "";

    // ── Public API ────────────────────────────────────────────────────────────

    public void save(int day, int pp, int lp, int salary, String playerName) {
        this.day        = day;
        this.pp         = pp;
        this.lp         = lp;
        this.salary     = salary;
        this.playerName = playerName;
        this.savedAt    = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("MMM d, yyyy  HH:mm"));
        this.occupied   = true;
    }

    public void clear() {
        this.occupied   = false;
        this.day        = 1;
        this.pp         = 0;
        this.lp         = 0;
        this.salary     = 0;
        this.playerName = "";
        this.savedAt    = "";
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public boolean isOccupied()  { return occupied;    }
    public int     getDay()      { return day;         }
    public int     getPP()       { return pp;          }
    public int     getLP()       { return lp;          }
    public int     getSalary()   { return salary;      }
    public String  getPlayerName(){ return playerName; }
    public String  getSavedAt()  { return savedAt;     }
}