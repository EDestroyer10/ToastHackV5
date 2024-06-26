package me.jellysquid.mods.sodium.client.world.biome.renderer.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.MC;
import me.jellysquid.mods.sodium.client.world.biome.renderer.mixin.WorldRendererAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public enum RenderUtils
{
	;


	public static void drawString(String string, int x, int y, int color, float scale)
	{
		MatrixStack matrixStack = new MatrixStack();
		matrixStack.translate(0.0D, 0.0D, 0.0D);
		matrixStack.scale(scale, scale, 1);
		VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
		immediate.draw();
	}

	public static void drawItem(DrawContext drawContext, ItemStack itemStack, int x, int y, float scale, boolean overlay, String countOverride) {
		MatrixStack matrices = drawContext.getMatrices();
		matrices.push();
		matrices.scale(scale, scale, 1f);
		matrices.translate(0, 0, 401); // Thanks Mojang

		int scaledX = (int) (x / scale);
		int scaledY = (int) (y / scale);

		drawContext.drawItem(itemStack, scaledX, scaledY);
		if (overlay) drawContext.drawItemInSlot(MC.mc.textRenderer, itemStack, scaledX, scaledY, countOverride);

		matrices.pop();
	}

	public static void drawItem(DrawContext drawContext, ItemStack itemStack, int x, int y, float scale, boolean overlay) {
		drawItem(drawContext, itemStack, x, y, scale, overlay, null);
	}


	public static void drawBoxBoth(BlockPos blockPos, QuadColor color, Box box, float lineWidth, Direction... excludeDirs) {
		drawBoxBoth(new Box(blockPos), color, lineWidth, excludeDirs);
		BlockPos BlockPos = null;
		drawBoxBoth(new Box(BlockPos), color, lineWidth);
	}

	public static void drawBoxBoth(Box box, QuadColor color, float lineWidth, Direction... excludeDirs) {
		QuadColor outlineColor = color.clone();
		outlineColor.overwriteAlpha(255);

		drawBoxBoth(box, color, outlineColor, lineWidth, excludeDirs);
	}

	public static void drawBoxBoth(BlockPos blockPos, QuadColor fillColor, QuadColor outlineColor, float lineWidth, Direction... excludeDirs) {
		drawBoxBoth(new Box(blockPos), fillColor, outlineColor, lineWidth, excludeDirs);
	}

	public static void drawBoxBoth(Box box, QuadColor fillColor, QuadColor outlineColor, float lineWidth, Direction... excludeDirs) {
		drawBoxFill(box, fillColor, excludeDirs);
		drawBoxOutline(box, outlineColor, lineWidth, excludeDirs);
	}

	public static Frustum getFrustum() {
		return ((WorldRendererAccessor) MinecraftClient.getInstance().worldRenderer).getFrustum();
	}

	public static void drawBoxFill(Box box, QuadColor color, Direction... excludeDirs) {
		if (!getFrustum().isVisible(box)) {
			return;
		}

		setup3DRender(true);

		MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		// Fill
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);

		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		Vertexer.vertexBoxQuads(matrices, buffer, Boxes.moveToZero(box), color, excludeDirs);
		tessellator.draw();

		end3DRender();
	}

	public static MatrixStack matrixFrom(double x, double y, double z) {
		MatrixStack matrices = new MatrixStack();

		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();


		matrices.translate(x - camera.getPos().x, y - camera.getPos().y, z - camera.getPos().z);

		return matrices;
	}

	public static Box moveToZero(Box box) {
		return box.offset(getMinVec(box).negate());
	}

	public static Vec3d getMinVec(Box box) {
		return new Vec3d(box.minX, box.minY, box.minZ);
	}

	public static void setup3DRender(boolean disableDepth) {
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		if (disableDepth)
			RenderSystem.disableDepthTest();
		RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
		RenderSystem.enableCull();
	}

	public static void end3DRender() {
		RenderSystem.disableCull();
		RenderSystem.disableBlend();
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
	}

	public static void drawBoxOutline(Box box, QuadColor color, float lineWidth, Direction... excludeDirs) {
		if (!getFrustum().isVisible(box)) {
			return;
		}

		setup3DRender(true);

		MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		// Outline
		RenderSystem.disableCull();
		RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
		RenderSystem.lineWidth(lineWidth);

		buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
		Vertexer.vertexBoxLines(matrices, buffer, Boxes.moveToZero(box), color, excludeDirs);
		tessellator.draw();

		RenderSystem.enableCull();

		end3DRender();
	}

	public static void bindTexture(Identifier identifier) {
		RenderSystem.setShaderTexture(0, identifier);
	}

	public static void shaderColor(int rgb) {
		float alpha = (rgb >> 24 & 0xFF) / 255.0F;
		float red = (rgb >> 16 & 0xFF) / 255.0F;
		float green = (rgb >> 8 & 0xFF) / 255.0F;
		float blue = (rgb & 0xFF) / 255.0F;
		RenderSystem.setShaderColor(red, green, blue, alpha);
	}

	public static void setup2DRender(boolean disableDepth) {
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		if (disableDepth)
			RenderSystem.disableDepthTest();
	}

	public static void end2DRender() {
		RenderSystem.disableBlend();
		RenderSystem.enableDepthTest();
	}


	/*public static void drawItem(ItemStack itemStack, int x, int y, double scale, boolean overlay) {
		RenderSystem.disableDepthTest();

		MatrixStack matrices = RenderSystem.getModelViewStack();

		matrices.push();
		matrices.scale((float) scale, (float) scale, 1);
		if (overlay) MC.getItemRenderer().renderGuiItemOverlay(null, MC.textRenderer, itemStack, (int) (x / scale), (int) (y / scale));

		matrices.pop();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.enableDepthTest();
	}*/

	public static Vec3d getCameraPos()
	{
		return Optimizer.MC.getBlockEntityRenderDispatcher().camera.getPos();
	}

	public static Vec3d getRenderLookVec(double partialTicks)
	{
		double f = 0.017453292;
		double pi = Math.PI;

		double yaw = MathHelper.lerp(partialTicks, Optimizer.MC.player.prevYaw, Optimizer.MC.player.getYaw());
		double pitch = MathHelper.lerp(partialTicks, Optimizer.MC.player.prevPitch, Optimizer.MC.player.getPitch());

		double f1 = Math.cos(-yaw * f - pi);
		double f2 = Math.sin(-yaw * f - pi);
		double f3 = -Math.cos(-pitch * f);
		double f4 = Math.sin(-pitch * f);

		return new Vec3d(f2 * f3, f4, f1 * f3).normalize();
	}

	public static BlockPos getCameraBlockPos()
	{
		return Optimizer.MC.getBlockEntityRenderDispatcher().camera.getBlockPos();
	}

	public static void applyRegionalRenderOffset(MatrixStack matrixStack)
	{
		Vec3d camPos = getCameraPos();
		BlockPos blockPos = getCameraBlockPos();

		int regionX = (blockPos.getX() >> 9) * 512;
		int regionZ = (blockPos.getZ() >> 9) * 512;

		matrixStack.translate(regionX - camPos.x, -camPos.y, regionZ - camPos.z);
	}

	public static void fillBox(BufferBuilder bufferBuilder, Box bb, MatrixStack matrixStack)
	{
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
				.next();
	}

	public static void drawSolidBox(Box bb, MatrixStack matrixStack)
	{
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		bufferBuilder.begin(VertexFormat.DrawMode.QUADS,
				VertexFormats.POSITION);
		fillBox(bufferBuilder, bb, matrixStack);
		bufferBuilder.end();
		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
	}

	public static void fillOutlinedBox(BufferBuilder bufferBuilder, Box bb, MatrixStack matrixStack)
	{
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
				.next();

		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ)
				.next();
		bufferBuilder
				.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ)
				.next();
	}

	public static void drawOutlinedBox(Box bb, MatrixStack matrixStack)
	{
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
				VertexFormats.POSITION);
		fillOutlinedBox(bufferBuilder, bb, matrixStack);
		bufferBuilder.end();
		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
	}

	public static void drawQuad(float x1, float y1, float x2, float y2, MatrixStack matrixStack)
	{
		float minX = Math.min(x1, x2);
		float maxX = Math.max(x1, x2);
		float minY = Math.min(y1, y2);
		float maxY = Math.max(y1, y2);
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, x1, y2, 0).next();
		bufferBuilder.vertex(matrix, x2, y2, 0).next();
		bufferBuilder.vertex(matrix, x2, y1, 0).next();
		bufferBuilder.vertex(matrix, x1, y1, 0).next();

		bufferBuilder.end();
		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
	}
	public static void drawWorldText(String text, double x, double y, double z, double scale, int color, boolean background) {
		drawWorldText(text, x, y, z, 0, 0, scale, false, color, background);
	}
	public static void renderLine(Vec3d start, Vec3d end, java.awt.Color color, MatrixStack matrices) {
		float red = color.getRed() / 255f;
		float green = color.getGreen() / 255f;
		float blue = color.getBlue() / 255f;
		float alpha = color.getAlpha() / 255f;
		Camera c = Optimizer.MC.gameRenderer.getCamera();
		Vec3d camPos = c.getPos();
		start = start.subtract(camPos);
		end = end.subtract(camPos);
		Matrix4f matrix = matrices.peek().getPositionMatrix();
		float x1 = (float) start.x;
		float y1 = (float) start.y;
		float z1 = (float) start.z;
		float x2 = (float) end.x;
		float y2 = (float) end.y;
		float z2 = (float) end.z;
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		GL11.glDepthFunc(GL11.GL_ALWAYS);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

		buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
		buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();

		buffer.end();

		BufferRenderer.drawWithGlobalProgram(buffer.endNullable());
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		RenderSystem.disableBlend();
	}

	private static void drawWorldText(String text, double x, double y, double z, int i, int i1, double scale, boolean b, int color, boolean background) {
		drawWorldText(text, x, y, z, 0, 0, scale, false, color, background);
	}
}
