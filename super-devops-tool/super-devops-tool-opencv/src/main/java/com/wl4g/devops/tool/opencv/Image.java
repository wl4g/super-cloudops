package com.wl4g.devops.tool.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import com.wl4g.devops.tool.opencv.library.OpenCvNativeLibraryLoader;

/**
 * 
 * @Title: Image.java
 * @Description: OpenCV-4.0.0 测试文件
 * @Package com.xu.test
 * @author: xuhyacinth
 * @date: 2019年5月7日12:13:13
 * @version: V-1.0.0
 * @Copyright: 2019 xuhyacinth
 *
 */
public class Image {

	static {
		OpenCvNativeLibraryLoader.loadLibrarys();
	}

	public static void main(String[] args) {
		videoFace();
	}

	/**
	 * OpenCV-4.0.0 实时人脸识fF别
	 * 
	 * @return: void
	 * @date: 2019年5月7日12:16:55
	 */
	public static void videoFace() {
		VideoCapture capture = new VideoCapture(0);
		Mat image = new Mat();
		int index = 0;
		if (capture.isOpened()) {
			while (true) {
				capture.read(image);
				HighGui.imshow("实时人脸识别", getFace(image));
				index = HighGui.waitKey(1);
				if (index == 27) {
					break;
				}
			}
		}
		return;
	}

	/**
	 * OpenCV-4.0.0 人脸识别
	 * 
	 * @date: 2019年5月7日12:16:55
	 * @param image 待处理Mat图片(视频中的某一帧)
	 * @return 处理后的图片
	 * 
	 */
	public static Mat getFace(Mat image) {
		// 1 读取OpenCV自带的人脸识别特征XML文件
		CascadeClassifier facebook = new CascadeClassifier(
				"E:\\OpenCV-4.0.0\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
		// 2 特征匹配类
		MatOfRect face = new MatOfRect();
		// 3 特征匹配
		facebook.detectMultiScale(image, face);
		Rect[] rects = face.toArray();
		System.out.println("匹配到 " + rects.length + " 个人脸");
		// 4 为每张识别到的人脸画一个圈
		for (int i = 0; i < rects.length; i++) {
			Imgproc.rectangle(image, new Point(rects[i].x, rects[i].y),
					new Point(rects[i].x + rects[i].width, rects[i].y + rects[i].height), new Scalar(0, 255, 0));
			Imgproc.putText(image, "Human", new Point(rects[i].x, rects[i].y), Imgproc.FONT_HERSHEY_SCRIPT_SIMPLEX, 1.0,
					new Scalar(0, 255, 0), 1, Imgproc.LINE_AA, false);
			// Mat dst=image.clone();
			// Imgproc.resize(image, image, new Size(300,300));
		}
		return image;
	}

}