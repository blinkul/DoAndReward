package com.example.doandreward.recyclerview.helper;

public final class SwipeUI {

    private final String label;
    private final int labelColor;
    private final int icon;
    private final int iconTint;
    private final int backgroundColor;

    public SwipeUI(String label, int labelColor, int icon, int iconTint, int backgroundColor) {
        this.label = label;
        this.labelColor = labelColor;
        this.icon = icon;
        this.iconTint = iconTint;
        this.backgroundColor = backgroundColor;
    }

    public String getLabel() {
        return label;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public int getIcon() {
        return icon;
    }

    public int getIconTint() {
        return iconTint;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }
}

