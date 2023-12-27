package bg.sofia.uni.fmi.mjt.photoalbum;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ParallelMonochromeAlbumCreator implements MonochromeAlbumCreator {
    private final int imageProcessorsCount;

    public ParallelMonochromeAlbumCreator(int imageProcessorsCount) {
        this.imageProcessorsCount = imageProcessorsCount;
    }

    @Override
    public void processImages(String sourceDirectory, String outputDirectory) {
        BlockingQueue<Image> imageQueue = new BlockingQueue<>(imageProcessorsCount);
        BlockingQueue<Object> acks = new BlockingQueue<>(imageProcessorsCount);

        Thread[] consumers = startConsumers(outputDirectory, imageQueue, acks);
        int imagesCount = loadImages(sourceDirectory, imageQueue);
        acknowledgeSavedImages(imagesCount, acks, consumers);
    }

    private int loadImages(String directory, BlockingQueue<Image> imageQueue) {
        try (var imageDir = Files.newDirectoryStream(Path.of(directory), "*.{png,jpeg,jpg,JPG}")) {
            int count = 0;
            for (var imagePath : imageDir) {
                count++;
                Thread.ofVirtual().name("producer-" + count).start(() -> {
                    try {
                        imageQueue.put(Image.loadImage(imagePath));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            return count;
        } catch (IOException e) {
            throw new UncheckedIOException("Exception occurred while loading the image directory", e);
        }
    }

    private Thread[] startConsumers(String directory, BlockingQueue<Image> imageQueue, BlockingQueue<Object> acks) {
        Thread[] consumers = new Thread[imageProcessorsCount];
        for (int i = 0; i < imageProcessorsCount; i++) {
            consumers[i] = Thread.ofVirtual().start(() -> {
                while (true) {
                    try {
                        Image image = imageQueue.take();
                        Image converted = Image.convertToBlackAndWhite(image);
                        Image.saveImage(converted, directory);
                        acks.put(new Object());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
        return consumers;
    }

    private void acknowledgeSavedImages(int imagesCount, BlockingQueue<Object> acks, Thread[] consumers) {
        while (imagesCount > 0) {
            try {
                acks.take();
                imagesCount--;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        for (int i = 0; i < imageProcessorsCount; i++) {
            consumers[i].interrupt();
        }
    }
}
