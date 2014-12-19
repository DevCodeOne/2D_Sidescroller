package com.Game.Resources;

import com.Game.Graphics.Pixmap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;

public class ResourceLoader {

    public static Pixmap load_image(String src) {
        try {
            BufferedImage image = ImageIO.read(ResourceLoader.class.getResource(src));
            BufferedImage tmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            tmp.getGraphics().drawImage(image, 0, 0, null);
            Pixmap pixmap = new Pixmap(tmp);
            return pixmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String load_text_file(String src) {
        try {
            Scanner scanner = new Scanner(ResourceLoader.class.getResourceAsStream(src));
            String line;
            StringBuilder builder = new StringBuilder();
            while(scanner.hasNextLine()) {
                line = scanner.nextLine();
                builder.append(line);
                builder.append("\n");
            }
            return builder.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
