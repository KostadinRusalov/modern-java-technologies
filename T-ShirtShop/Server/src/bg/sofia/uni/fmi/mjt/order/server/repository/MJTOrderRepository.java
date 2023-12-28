package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.Response;

public class MJTOrderRepository implements OrderRepository {
    @Override
    public Response request(String size, String color, String destination) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Response getOrderById(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Response getAllOrders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Response getAllSuccessfulOrders() {
        throw new UnsupportedOperationException();
    }
}
