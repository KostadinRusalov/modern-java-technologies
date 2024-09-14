package bg.sofia.uni.fmi.mjt.photoalbum;

public class Main {
    public static void main(String[] args) {
        // 800 photos with 50 processors   =>  64.208 s
        // 800 photos with 100 processors  =>  53.849 s
        // 800 photos with 200 processors  =>  threw java heap space exception and saved only 725 photos
        // 800 photos with 250 processors  =>  53.199 s

        long begin = System.currentTimeMillis();

        ParallelMonochromeAlbumCreator albumCreator = new ParallelMonochromeAlbumCreator(300);

        albumCreator.processImages("/Users/kostadinroussalov/Desktop/snimki",
            "/Users/kostadinroussalov/Desktop/snimki-5-cherni");

        System.out.println("finished in: " + (System.currentTimeMillis() - begin));
    }
}
