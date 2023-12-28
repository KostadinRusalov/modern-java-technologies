package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;

import java.util.ArrayList;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.order.server.requirements.Requirements.requireNotNull;
import static bg.sofia.uni.fmi.mjt.order.server.requirements.Requirements.requireTrue;

public class MJTOrderRepository implements OrderRepository {
    private static final String INVALID = "invalid: ";
    private static final String INVALID_SIZE = "size";
    private static final String INVALID_COLOR = "color";
    private static final String INVALID_DESTINATION = "destination";
    private static final int INVALID_ORDER_ID = -1;

    private int orderId = 1;

    private final List<Order> orders = new ArrayList<>();

    @Override
    public Response request(String size, String color, String destination) {
        requireNotNull(size, "Size cannot be null");
        requireNotNull(color, "Color cannot be null");
        requireNotNull(destination, "Destination cannot be null");

        Size orderSize = Size.of(size);
        Color orderColor = Color.of(color);
        Destination orderDestination = Destination.of(destination);

        List<String> invalidFields = new ArrayList<>();
        if (orderSize == Size.UNKNOWN) {
            invalidFields.add(INVALID_SIZE);
        }
        if (orderColor == Color.UNKNOWN) {
            invalidFields.add(INVALID_COLOR);
        }
        if (orderDestination == Destination.UNKNOWN) {
            invalidFields.add(INVALID_DESTINATION);
        }

        TShirt tShirt = new TShirt(orderSize, orderColor);
        if (!invalidFields.isEmpty()) {
            orders.add(new Order(INVALID_ORDER_ID, tShirt, orderDestination));
            return Response.decline(INVALID + String.join(",", invalidFields));
        }

        orders.add(new Order(orderId, tShirt, orderDestination));
        return Response.create(orderId++);
    }

    @Override
    public Response getOrderById(int id) {
        requireTrue(id > 0, "Id cannot be non-positive");

        return orders.stream()
            .filter(order -> order.id() == id)
            .findFirst()
            .map(order -> Response.ok(List.of(order)))
            .orElseGet(() -> Response.notFound(id));
    }

    @Override
    public Response getAllOrders() {
        return Response.ok(List.copyOf(orders));
    }

    @Override
    public Response getAllSuccessfulOrders() {
        return Response.ok(orders.stream().filter(order -> order.id() != INVALID_ORDER_ID).toList());
    }
}
