package bg.sofia.uni.fmi.mjt.photoalbum;


public class ParallelMonochromeAlbumCreator implements MonochromeAlbumCreator {
    private final int imageProcessorsCount;

    public ParallelMonochromeAlbumCreator(int imageProcessorsCount) {
        this.imageProcessorsCount = imageProcessorsCount;
    }

    @Override
    public void processImages(String sourceDirectory, String outputDirectory) {
    }
}
