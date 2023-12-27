package bg.sofia.uni.fmi.mjt.photoalbum.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public class Image {
    String name;
    BufferedImage data;

    public Image(String name, BufferedImage data) {
        this.name = name;
        this.data = data;
    }

    public static Image loadImage(Path imagePath) {
        try {
            BufferedImage imageData = ImageIO.read(imagePath.toFile());
            return new Image(imagePath.getFileName().toString(), imageData);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Failed to load image %s", imagePath), e);
        }
    }

    public static Image convertToBlackAndWhite(Image image) {
        BufferedImage processedData =
            new BufferedImage(image.data.getWidth(), image.data.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        processedData.getGraphics().drawImage(image.data, 0, 0, null);

        return new Image(image.name, processedData);
    }

    public static void saveImage(Image image, String directory) {
        try {
            ImageIO.write(image.data, "png", new File(directory, image.name));
            System.out.println("Saved " + image.name + " to " + directory);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("While saving image %s", image.name), e);
        }
    }
}
