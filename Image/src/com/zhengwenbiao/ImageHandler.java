package com.zhengwenbiao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ImageHandler {

	public static void main(String[] args) throws IOException,
			InterruptedException {
		ImageCuter cuter = new ImageCuter();
		cuter.cutImage(440, 755, 0, 0, 440, 755, "source");
		cuter.createSrcImage();
		handleImage();
	}

	// 处理图片和判断图片
	private static void handleImage() throws IOException, InterruptedException {
		String filename = ImageHelper.path + "\\srcImage\\";
		Hamming hamming = new Hamming();
		HashCodeDepot hashDepot = new HashCodeDepot();
		String sourceHashCode = hamming.produceFingerPrint(filename
				+ "source.jpg");
		List<Integer> differenceList = new ArrayList<Integer>();
		List<String> colorList = new ArrayList<String>();
		List<String> hashCodes = hashDepot.mList;
		for (int i = 0, size = hashCodes.size(); i < size; i++) {
			int difference = hamming.hammingDistance(sourceHashCode,
					hashCodes.get(i));
			differenceList.add(difference);
			colorList.add(hashCodes.get(i));
		}
		judgeAndGiveAnswer(differenceList, colorList, sourceHashCode);
	}

	// 根据汉明距离判断图片并给出相应的回答
	private static void judgeAndGiveAnswer(List<Integer> differenceList,
			List<String> colorList, String sourceHashCode) throws IOException,
			InterruptedException {
		int min = getMinDifference(differenceList);
		int minDifference = differenceList.get(min);
		String color = colorList.get(min);
		if (minDifference > 7) {
			System.out.println("请再拍一张，这张无法识别");
		} else if ((minDifference > 1) && (minDifference <= 7)) {
			getMaybeAnswer(color, sourceHashCode);
		} else if (minDifference < 2) {
			getSureAnswer(color, sourceHashCode);
		}
	}

	// 给出的答案是确定的，因为距离小于2，可以认为是同一张图片
	private static void getSureAnswer(String color, String sourceHashCode)
			throws IOException {
		if (color.equals("3f7f7ffffff3f3ff")) {
			showAnswer("红桃", sourceHashCode);
		} else if (color.equals("0cfcf7fffffbf3fc")) {
			showAnswer("方块", sourceHashCode);
		} else if (color.equals("ffcf073787ffffff")) {
			showAnswer("黑桃", sourceHashCode);
		} else if (color.equals("0047fbfffff7f7f7")) {
			showAnswer("梅花", sourceHashCode);
		}
	}

	// 控制台上显示回答
	private static void showAnswer(String color, String sourceHashCode)
			throws IOException {
		BufferedWriter bufferWriterBrick = getBufferedWriter("Brick");
		BufferedWriter bufferWriterBlackPitch = getBufferedWriter("BlackPitch");
		BufferedWriter bufferWriterRedPitch = getBufferedWriter("RedPitch");
		BufferedWriter bufferWriterPlum = getBufferedWriter("Plum");
		System.out.println("这是一张" + color + "A");
		System.out.println("是吧？");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				(System.in)));
		String answer = reader.readLine();
		if (answer.equals("Y")) {
			System.out.println("太好了!我果然是最聪明的!");
		} else {
			System.out.println("Oh,shit!下次不会错了!!");
			if (answer.equals("Brick")) {
				writeToColorTxt(bufferWriterBrick, sourceHashCode);
			} else if (answer.equals("BlackPitch")) {
				writeToColorTxt(bufferWriterBlackPitch, sourceHashCode);
			} else if (answer.equals("RedPitch")) {
				writeToColorTxt(bufferWriterRedPitch, sourceHashCode);
			} else if (answer.equals("Plum")) {
				writeToColorTxt(bufferWriterPlum, sourceHashCode);
			}
		}
	}

	// 得到相应txt的BufferedWriter
	private static BufferedWriter getBufferedWriter(String color)
			throws IOException {
		FileWriter writer = new FileWriter("D:\\workspace\\Image\\" + color
				+ ".txt", true);
		return new BufferedWriter(writer);
	}

	// 得到汉明距离中国最小值
	private static int getMinDifference(List<Integer> differenceList) {
		int min = 0;
		for (int i = 0, len = differenceList.size() - 1; i < len; i++) {
			if (differenceList.get(min) >= differenceList.get(i + 1)) {
				min = i + 1;
			}
		}
		return min;
	}

	// 将误判的图片的指纹写入相应花色的txt
	private static void writeToColorTxt(BufferedWriter bufferWriter,
			String sourceHashCode) throws IOException {
		bufferWriter.write(sourceHashCode);
		bufferWriter.newLine();
		bufferWriter.flush();
	}

	// 给出可能的答案，因为汉明距离是在2-7内，认为两张图片是相似的
	private static void getMaybeAnswer(String color, String sourceHashCode)
			throws IOException, InterruptedException {
		boolean isRedPitch = judgeFromList(sourceHashCode, "RedPitch");
		boolean isBrick = judgeFromList(sourceHashCode, "Brick");
		boolean isBlackPitch = judgeFromList(sourceHashCode, "BlackPitch");
		boolean isPlum = judgeFromList(sourceHashCode, "Plum");
		if (isRedPitch) {
			showAnswer("红桃", sourceHashCode);
			isRedPitch = false;
		} else if (isBrick) {
			showAnswer("方块", sourceHashCode);
			isBrick = false;
		} else if (isBlackPitch) {
			showAnswer("黑桃", sourceHashCode);
			isBlackPitch = false;
		} else if (isPlum) {
			showAnswer("梅花", sourceHashCode);
			isPlum = false;
		} else {
			getSureAnswer(color, sourceHashCode);
		}
	}

	// 从存储的库中的指纹先判断是否与先前误判的指纹一样
	private static boolean judgeFromList(String sourceHashCode, String color)
			throws IOException {
		boolean bool = false;
		List<String> list = new ArrayList<String>();
		BufferedReader reader = getReader(color);
		String context = reader.readLine();
		while (context != null) {
			list.add(context);
			context = reader.readLine();
		}
		Hamming hamming = new Hamming();
		for (int i = 0, len = list.size(); i < len; i++) {
			int difference = hamming.hammingDistance(sourceHashCode,
					list.get(i));
			if (difference < 2) {
				bool = true;
			} else {
				System.out.println("让我想想。。。");
			}
		}
		return bool;
	}

	// 得到相应的BufferedReader
	private static BufferedReader getReader(String color)
			throws FileNotFoundException {
		return new BufferedReader(new FileReader("D:\\workspace\\Image\\"
				+ color + ".txt"));
	}
}
