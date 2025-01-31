package net.blightbuster.kiln;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class KilnScreen extends HandledScreen<KilnScreenHandler> {

    private static final Identifier TEXTURE = new Identifier("skyutils", "textures/gui/container/kiln.png");

    public KilnScreen(KilnScreenHandler container, PlayerInventory playerInventory, Text title) {
        super(container, playerInventory, title);
        this.backgroundHeight = 114 + 6 * 18;
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        float p = (float) this.handler.getProgress() / 10.0f;
        String string = this.title.asString() + " " + String.format("%.01f", p) + "%";
        this.resize(client, width, height);
        this.textRenderer.draw(matrices, string,
                (float) (this.backgroundWidth / 2 - this.textRenderer.getWidth(string) / 2), 6.0F, 4210752);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int i = this.x;
        int j = this.y;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        int p = this.handler.getBurnTime();
        float prog = (p / (float) KilnBlockEntity.CHARCOAL_BURN_TIME);
        if (prog != 0) {
            int l = (int) (12.0 * prog);
            this.drawTexture(matrices, i + 56, j + 36 + 12 - l, 176, 12 - l, 14, l + 1);
        }
        float prog2 = (float) this.handler.getProgress() / 1000.0f;
        if (prog2 != 0) {
            this.drawTexture(matrices, i + 79, j + 34, 176, 14, (int) (24 * prog2) + 1, 16);
        }
    }

}