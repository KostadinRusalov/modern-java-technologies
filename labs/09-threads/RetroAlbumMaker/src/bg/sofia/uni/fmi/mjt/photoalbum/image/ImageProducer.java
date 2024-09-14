package bg.sofia.uni.fmi.mjt.photoalbum.image;

import bg.sofia.uni.fmi.mjt.photoalbum.queue.BlockingQueue;

import java.nio.file.Path;

public class ImageProducer implements Runnable {
    private final BlockingQueue<Image> imageQueue;
    private final Path imagePath;

    public ImageProducer(BlockingQueue<Image> imageQueue, Path imagePath) {
        this.imageQueue = imageQueue;
        this.imagePath = imagePath;
    }

    @Override
    public void run() {
        try {
            imageQueue.put(Image.loadImage(imagePath));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
