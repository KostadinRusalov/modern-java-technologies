package bg.sofia.uni.fmi.mjt.order.server.tshirt;


public record TShirt(Size size, Color color) {

    @Override
    public String toString() {
        return STR. "{\"size\":\"\{ size }\", \"color\":\"\{ color }\"}" ;
    }
}
