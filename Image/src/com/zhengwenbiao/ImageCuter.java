package com.zhengwenbiao;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ImageCuter {
    //该类负责将相应的图片处理并放在相应制定的路径上，方便主类处理
	public void cutImage(int targetWidth, int targetHeight, int srcLeftTopX,
			int srcLeftTopY, int srcRightButtomX, int srcRightButtomY,
			String name) throws IOException {
		InputStream inputStream = null;
		String path =  "srcImage\\" + name + ".jpg";
		inputStream = new FileInputStream(path);
		// 用ImageIO读取字节流
		BufferedImage bufferedImage = ImageIO.read(inputStream);
		BufferedImage distin = null;
		// 生成图片
		distin = new BufferedImage(targetWidth, targetHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = distin.getGraphics();
		g.drawImage(bufferedImage, 0, 0, targetWidth, targetHeight,
				srcLeftTopX, srcLeftTopY, srcRightButtomX, srcRightButtomY,
				null);
		ImageIO.write(distin, "jpg", new File(path));
	}

	public void createSrcImage() throws IOException {
		File file = new File("srcImage\\source.jpg");
		BufferedImage image = ImageIO.read(file);// 通过imageio将图像载入
		ImageIO.write(image, "jpg", file);
	}
}
