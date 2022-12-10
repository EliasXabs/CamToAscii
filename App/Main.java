package App;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.VideoInputFrameGrabber;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.bytedeco.javacv.FrameConverter;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Main
 */
public class Main implements Runnable {

    private JFrame f = new JFrame();
    private JTextArea area = new JTextArea();

    public static void main(String[] a) {
        Main m = new Main();
        m.run();
    }

    public Image loadImage(BufferedImage todisplay) throws IOException {
        Image newResizedImage = todisplay.getScaledInstance(1000, 700, Image.SCALE_SMOOTH);
        return newResizedImage;
    }

    public String imageToASCII(BufferedImage todisplay) {
        String density = "    .'`^,:;Il!i><~+_-?][}{1)(/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$";
        try {
            Image image = loadImage(todisplay);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(image, 0, 0, null);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int pixel = bufferedImage.getRGB(j, i);
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = (pixel) & 0xff;
                    int gray = (int) (red + green + blue) / 3;
                    int index = (int) (gray * (density.length() + 1) / 256);
                    if (index >= density.length()) {
                        sb.append(" ");
                    } else {
                        sb.append(density.charAt(index));
                    }
                }
                sb.append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void createFrame() {
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        area.setBounds(0, 0, 1000, 1000);
        area.setBackground(java.awt.Color.BLACK);
        area.setForeground(java.awt.Color.WHITE);
        area.setFont(new java.awt.Font("Courier", java.awt.Font.CENTER_BASELINE, 1));
        f.add(area);
        f.setSize(1000, 1000);
        f.setLayout(null);
        f.setVisible(true);
    }

    public void draw(String text) {
        area.setText(text);
        f.add(area);
        f.setSize(1000, 1000);
        f.setLayout(null);
        f.setVisible(true);
    }

    public static BufferedImage IplImageToBufferedImage(IplImage src) {
        try (OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage()) {
            try (Java2DFrameConverter paintConverter = new Java2DFrameConverter()) {
                Frame frame = grabberConverter.convert(src);
                return paintConverter.getBufferedImage(frame, 1);
            }
        }
    }

    @Override
    public void run() {
        try (FrameGrabber grabber = new VideoInputFrameGrabber(0)) {
            try {
                grabber.start();
                IplImage img;
                createFrame();
                while (true) {
                    Frame frame = grabber.grab();
                    try (FrameConverter<IplImage> converter = new org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage()) {
                        img = converter.convert(frame);
                    }
                    BufferedImage bufferedImage = IplImageToBufferedImage(img);
                    // mirror the image
                    for (int y = 0; y < bufferedImage.getHeight(); y++) {
                        for (int lx = 0, rx = bufferedImage.getWidth() - 1; lx < rx; lx++, rx--) {
                            int temp = bufferedImage.getRGB(lx, y);
                            bufferedImage.setRGB(lx, y, bufferedImage.getRGB(rx, y));
                            bufferedImage.setRGB(rx, y, temp);
                        }
                    }
                    String text = imageToASCII(bufferedImage);
                    draw(text);
                    Thread.sleep(50);
                }
            } catch (Exception e) {
            }
        } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}