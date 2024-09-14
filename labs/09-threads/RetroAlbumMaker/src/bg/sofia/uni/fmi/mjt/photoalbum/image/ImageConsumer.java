package bg.sofia.uni.fmi.mjt.photoalbum.image;

import bg.sofia.uni.fmi.mjt.photoalbum.queue.BlockingQueue;

public class ImageConsumer implements Runnable {
    private final String directory;

    private final BlockingQueue<Image> imageQueue;

    private final BlockingQueue<Object> acks;

    public ImageConsumer(String directory, BlockingQueue<Image> imageQueue, BlockingQueue<Object> acks) {
        this.directory = directory;
        this.imageQueue = imageQueue;
        this.acks = acks;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Image image = imageQueue.take();
                Image.saveImage(Image.convertToBlackAndWhite(image), directory);
                acks.put(new Object());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
