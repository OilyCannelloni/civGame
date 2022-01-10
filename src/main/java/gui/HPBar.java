package gui;

import civ.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HPBar {
    private int BAR_LENGTH = 30;
    public Vector2D offset;
    public int HP, maxHP;

    public HPBar(int HP, int maxHP) {
        this.HP = HP;
        this.maxHP = maxHP;
    }

    public void draw(GraphicsContext ctx, Vector2D fieldOrigin) {
        Vector2D HP_BAR_OFFSET = new Vector2D(34, 20);
        Vector2D barOrigin = fieldOrigin.add(HP_BAR_OFFSET);
        ctx.moveTo(barOrigin.x, barOrigin.y);

        float HPPercentage = (float) HP / maxHP;
        System.out.println(HPPercentage);

        if (HPPercentage < 0.3) ctx.setStroke(Color.RED);
        else if (HPPercentage < 0.7) ctx.setStroke(Color.YELLOW);
        else ctx.setStroke(Color.GREEN);
        ctx.setLineWidth(2);

        ctx.strokeLine(barOrigin.x, barOrigin.y, barOrigin.x + BAR_LENGTH * HPPercentage, barOrigin.y);
        ctx.setStroke(Color.GRAY);
        ctx.strokeLine(barOrigin.x + BAR_LENGTH * HPPercentage,barOrigin.y, barOrigin.x + BAR_LENGTH, barOrigin.y);
        ctx.stroke();
    }
}
