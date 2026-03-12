package GUI.panels.dialogueComponents;

import GUI.panels.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class IdentityCreationLayer extends JPanel {

    private final MainFrame mainFrame;
    private JDialog  dialog;
    private Runnable onComplete;

    private String playerName   = "";
    private String playerGender = "";

    private static final String SPRITE_MALE    = "/GUI/resources/icons/maleAvatar.PNG";
    private static final String SPRITE_FEMALE  = "/GUI/resources/icons/femaleAvatar.PNG";
    private static final String SPRITE_DEFAULT = "/GUI/resources/icons/defaultAvatar.PNG";
    private static final int    AVATAR_W       = 200;
    private static final int    AVATAR_H       = 200;

    private JTextField nameField;
    private JLabel     avatarLabel;
    private JLabel     idNumberLabel;
    private JButton    maleBtn;
    private JButton    femaleBtn;
    private JButton    confirmBtn;

    private static final Color CARD_BG      = new Color(250, 248, 243);
    private static final Color CARD_BORDER  = new Color(30,  30,  30);
    private static final Color HEADER_BG    = new Color(15,  30,  60);
    private static final Color HEADER_TEXT  = Color.WHITE;               
    private static final Color LABEL_COLOR  = new Color(50,  50,  60);
    private static final Color MALE_BASE    = new Color(190, 215, 245);
    private static final Color FEMALE_BASE  = new Color(245, 200, 215);
    private static final Color SELECTED_HL  = new Color(80,  180, 110);

    private final String idNumber = "CC-" + (1000 + (int)(Math.random() * 9000));

    private Font bodyFont;
    private Font boldFont;
    private Font headerFont;

    private ImageIcon iconDefault;
    private ImageIcon iconMale;
    private ImageIcon iconFemale;

    public IdentityCreationLayer(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        loadFonts();
        preloadAvatarIcons();
        setOpaque(false);
        setLayout(new GridBagLayout());
        buildUI();
    }

    // ── Public API ────────────────────────────────────────────────────────────

       public void setOnComplete(Runnable callback) {
        this.onComplete = callback;
    }

    public void showAsPopup() {
        dialog = new JDialog(mainFrame, "Employee Identity", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);

        JPanel backdrop = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {

            }
        };
        backdrop.setOpaque(false);
        backdrop.setPreferredSize(new Dimension(660, 520));
        backdrop.add(this);

        dialog.setContentPane(backdrop);
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true); 
    }

    public void hidePopup() {
        if (dialog != null) {
            dialog.dispose();
            dialog = null;
        }
    }

    public void reset() {
        playerName   = "";
        playerGender = "";
        if (nameField    != null) nameField.setText("");
        if (avatarLabel  != null) avatarLabel.setIcon(iconDefault); 
        if (maleBtn   != null) maleBtn.repaint();
        if (femaleBtn != null) femaleBtn.repaint();
    }

    // ── Avatar icon loading ───────────────────────────────────────────────────

    private void preloadAvatarIcons() {
        iconDefault = loadSpriteIcon(SPRITE_DEFAULT);
        iconMale    = loadSpriteIcon(SPRITE_MALE);
        iconFemale  = loadSpriteIcon(SPRITE_FEMALE);
        
        if (iconDefault == null) {
            iconDefault = createDefaultAvatar();
        }
        if (iconMale == null) {
            iconMale = createGenderAvatar("MALE");
        }
        if (iconFemale == null) {
            iconFemale = createGenderAvatar("FEMALE");
        }
    }

    private ImageIcon loadSpriteIcon(String resourcePath) {
        try {
            InputStream s = getClass().getResourceAsStream(resourcePath);
            if (s == null) return null;

            BufferedImage raw = ImageIO.read(s);
            int imgW = raw.getWidth(), imgH = raw.getHeight();

            double scale = Math.min(
                (double)(AVATAR_W - 4) / imgW,
                (double)(AVATAR_H - 4) / imgH
            );
            int newW = Math.max(1, (int)(imgW * scale));
            int newH = Math.max(1, (int)(imgH * scale));

            Image scaled = raw.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);

        } catch (Exception ex) {
            return null;
        }
    }

    private ImageIcon createDefaultAvatar() {
        BufferedImage img = new BufferedImage(AVATAR_W, AVATAR_H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(new Color(200, 195, 185));
        g2.fillOval(50, 30, 100, 80);  
        g2.fillRoundRect(40, 110, 120, 70, 30, 30); 
        
        g2.dispose();
        return new ImageIcon(img);
    }

    private ImageIcon createGenderAvatar(String gender) {
        BufferedImage img = new BufferedImage(AVATAR_W, AVATAR_H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (gender.equals("MALE")) {
            g2.setColor(new Color(190, 215, 245));
            g2.fillOval(50, 30, 100, 80);   // head
            g2.fillRoundRect(40, 110, 120, 70, 30, 30); // body
        } else {
            g2.setColor(new Color(245, 200, 215));
            g2.fillOval(50, 30, 100, 80);   // head
            g2.fillRoundRect(40, 110, 120, 70, 40, 40); // body with more curve
        }
        
        g2.dispose();
        return new ImageIcon(img);
    }

    private void selectGender(String gender) {
        playerGender = gender;
        avatarLabel.setIcon(gender.equals("MALE") ? iconMale : iconFemale);
        avatarLabel.revalidate();
        avatarLabel.repaint();
        maleBtn.repaint();
        femaleBtn.repaint();
    }


    private void buildUI() {

        JPanel card = new JPanel(new BorderLayout(0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                g2.setColor(new Color(0, 0, 0, 55));
                g2.fillRoundRect(5, 5, w - 2, h - 2, 14, 14);

                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, w - 5, h - 5, 12, 12);

                g2.setColor(CARD_BORDER);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, w - 6, h - 6, 12, 12);

                // Header bar
                g2.setColor(HEADER_BG);
                g2.fillRoundRect(0, 0, w - 6, 56, 12, 12);
                g2.fillRect(0, 36, w - 6, 22);

                // Bottom stripe
                g2.setColor(HEADER_BG);
                g2.fillRoundRect(0, h - 32, w - 6, 32, 12, 12);
                g2.fillRect(0, h - 44, w - 6, 18);

                g2.dispose();
            }
        };
    card.setOpaque(false);
    
        // NORTH: header
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(0, 56));
        JLabel company = new JLabel("Khai G. Call Center Agency", SwingConstants.CENTER);
        company.setFont(headerFont);
        company.setForeground(HEADER_TEXT);
        headerPanel.add(company);
        card.add(headerPanel, BorderLayout.NORTH);

        // CENTER: avatar + fields
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 24));

        GridBagConstraints gc = new GridBagConstraints();

        JPanel avatarBox = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(220, 215, 205));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(155, 150, 140));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };
        avatarBox.setOpaque(false);
        avatarBox.setPreferredSize(new Dimension(AVATAR_W, AVATAR_H));
        avatarBox.setMinimumSize(new Dimension(AVATAR_W, AVATAR_H));
        avatarBox.setMaximumSize(new Dimension(AVATAR_W, AVATAR_H));
        avatarLabel = new JLabel(iconDefault, SwingConstants.CENTER);
        avatarLabel.setVerticalAlignment(SwingConstants.CENTER);
        avatarBox.add(avatarLabel);

        gc.gridx = 0; gc.gridy = 0; gc.gridheight = 4;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill   = GridBagConstraints.NONE;
        gc.insets = new Insets(0, 0, 0, 24);
        centerPanel.add(avatarBox, gc);

        gc.gridheight = 1; gc.gridx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.anchor = GridBagConstraints.WEST;

        gc.gridy = 0; gc.insets = new Insets(0, 0, 4, 0);
        JLabel nameLbl = new JLabel("Name");
        nameLbl.setFont(boldFont);
        nameLbl.setForeground(LABEL_COLOR);
        centerPanel.add(nameLbl, gc);

        gc.gridy = 1; gc.insets = new Insets(0, 0, 20, 0);
        nameField = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(100, 100, 110));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        nameField.setFont(boldFont.deriveFont(14f));
        nameField.setOpaque(false);
        nameField.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        nameField.setForeground(new Color(20, 20, 40));
        nameField.setCaretColor(new Color(20, 20, 40));
        nameField.setPreferredSize(new Dimension(0, 36));
        nameField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { playerName = nameField.getText().trim(); }
        });
        centerPanel.add(nameField, gc);

        gc.gridy = 2; gc.insets = new Insets(0, 0, 8, 0);
        JLabel genderLbl = new JLabel("Gender");
        genderLbl.setFont(boldFont);
        genderLbl.setForeground(LABEL_COLOR);
        centerPanel.add(genderLbl, gc);

        gc.gridy = 3; gc.insets = new Insets(0, 0, 0, 0);
        gc.fill = GridBagConstraints.NONE;
        JPanel genderRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        genderRow.setOpaque(false);
        maleBtn   = buildGenderButton("\u2642", "MALE",   MALE_BASE);
        femaleBtn = buildGenderButton("\u2640", "FEMALE", FEMALE_BASE);
        maleBtn.setPreferredSize(new Dimension(90, 46));
        femaleBtn.setPreferredSize(new Dimension(90, 46));
        genderRow.add(maleBtn);
        genderRow.add(femaleBtn);
        centerPanel.add(genderRow, gc);

        card.add(centerPanel, BorderLayout.CENTER);

        // SOUTH: ID stripe
        JPanel stripePanel = new JPanel(new GridBagLayout());
        stripePanel.setOpaque(false);
        stripePanel.setPreferredSize(new Dimension(0, 36));
        idNumberLabel = new JLabel(idNumber + "   \u2022   EMPLOYEE", SwingConstants.CENTER);
        idNumberLabel.setFont(bodyFont.deriveFont(Font.BOLD, 10f));
        idNumberLabel.setForeground(new Color(200, 210, 230));              // ← lighter blue-white, was gold
        stripePanel.add(idNumberLabel);
        card.add(stripePanel, BorderLayout.SOUTH);

        // Below card: warning + confirm
        JPanel south = new JPanel();
        south.setOpaque(false);
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        confirmBtn = buildConfirmButton();
        confirmBtn.setFocusable(false);
        confirmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        south.add(confirmBtn);

        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(card);
        container.add(south);

        add(container);
    }

    // ── Gender button ─────────────────────────────────────────────────────────

    private JButton buildGenderButton(String symbol, String gender, Color baseColor) {
        JButton btn = new JButton() {
            private boolean hov = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hov = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hov = false; repaint(); }
                });
            }
            @Override public void update(Graphics g) { paintComponent(g); } // prevents stale layer buildup
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Clear first
                g2.setComposite(AlphaComposite.Clear);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setComposite(AlphaComposite.SrcOver);
                boolean selected = gender.equals(playerGender);
                Color bg = selected ? SELECTED_HL : (hov ? baseColor.darker() : baseColor);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(selected ? new Color(40, 130, 70) : new Color(100, 100, 110));
                g2.setStroke(new BasicStroke(selected ? 2f : 1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setFont(new Font("Arial", Font.BOLD, 22));
                g2.setColor(selected ? Color.WHITE : LABEL_COLOR);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(symbol,
                    (getWidth()  - fm.stringWidth(symbol)) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(gender.equals("MALE") ? "Male (he/him)" : "Female (she/her)");
        btn.addActionListener(e -> selectGender(gender));
        return btn;
    }

    // ── Confirm button ────────────────────────────────────────────────────────

    private JButton buildConfirmButton() {
        JButton btn = new JButton("CONFIRM IDENTITY") {
            private boolean hov = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hov = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hov = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hov ? new Color(30, 55, 100) : HEADER_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(HEADER_TEXT);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setFont(boldFont.deriveFont(14f));
        btn.setPreferredSize(new Dimension(220, 46));
        btn.setMaximumSize(new Dimension(220, 46));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> confirmIdentity());
        return btn;
    }

    // ── Confirm logic ─────────────────────────────────────────────────────────

    private void confirmIdentity() {
        playerName = nameField.getText().trim();

        if (playerName.isEmpty()) {
            shakeField(nameField);
            return;
        }
        if (playerGender.isEmpty()) {
            shakeField(maleBtn);
            return;
        }

        String pronounDisplay = playerGender.equals("FEMALE") ? "she/her" : "he/him";

        JPanel msg = new JPanel();
        msg.setLayout(new BoxLayout(msg, BoxLayout.Y_AXIS));
        msg.setOpaque(false);
        msg.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        JLabel line1 = new JLabel("Are you sure? All changes made are final.", SwingConstants.CENTER);
        line1.setFont(boldFont.deriveFont(14f));
        line1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel summaryRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 4));
        summaryRow.setOpaque(false);

        JLabel lName = new JLabel(playerName);
        lName.setFont(boldFont.deriveFont(13f));
        lName.setForeground(new Color(30, 60, 120));

        JLabel sep1 = new JLabel("\u2022");
        sep1.setForeground(new Color(160, 160, 160));

        JLabel lGender = new JLabel(playerGender);
        lGender.setFont(boldFont.deriveFont(13f));
        lGender.setForeground(playerGender.equals("FEMALE")
            ? new Color(180, 60, 110) : new Color(50, 100, 180));

        JLabel sep2 = new JLabel("\u2022");
        sep2.setForeground(new Color(160, 160, 160));

        JLabel lPronoun = new JLabel(pronounDisplay);
        lPronoun.setFont(bodyFont.deriveFont(13f));
        lPronoun.setForeground(new Color(80, 80, 100));

        summaryRow.add(lName);
        summaryRow.add(sep1);
        summaryRow.add(lGender);
        summaryRow.add(sep2);
        summaryRow.add(lPronoun);

        msg.add(line1);
        msg.add(Box.createVerticalStrut(6));
        msg.add(summaryRow);

        int result = JOptionPane.showConfirmDialog(
            this, msg, "Confirm Identity",
            JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            String pronoun = playerGender.equals("FEMALE") ? "she/her" : "he/him";
            mainFrame.setPlayerIdentity(playerName, playerGender, pronoun);
            hidePopup();
            if (onComplete != null) onComplete.run();
        }
    }

    private void shakeField(JComponent comp) {
        Point origin = comp.getLocation();
        int[] offsets = {-6, 6, -5, 5, -3, 3, 0};
        final int[] step = {0};
        Timer t = new Timer(30, null);
        t.addActionListener(e -> {
            if (step[0] >= offsets.length) { t.stop(); comp.setLocation(origin); return; }
            comp.setLocation(origin.x + offsets[step[0]++], origin.y);
        });
        t.start();
    }

    // ── Font loading ──────────────────────────────────────────────────────────

    private void loadFonts() {
        try {
            InputStream s = getClass().getResourceAsStream("/GUI/resources/font/Mulish-VariableFont_wght.ttf");
            Font base = (s != null)
                ? Font.createFont(Font.TRUETYPE_FONT, s)
                : new Font("Georgia", Font.PLAIN, 12);
            bodyFont   = base.deriveFont(Font.PLAIN, 13f);
            boldFont   = base.deriveFont(Font.BOLD,  13f);
            headerFont = base.deriveFont(Font.BOLD,  17f);
        } catch (Exception ex) {
            bodyFont   = new Font("Georgia", Font.PLAIN,  13);
            boldFont   = new Font("Georgia", Font.BOLD,   13);
            headerFont = new Font("Georgia", Font.BOLD,   17);
        }
    }
}