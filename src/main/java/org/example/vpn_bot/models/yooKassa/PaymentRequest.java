package org.example.vpn_bot.models.yooKassa;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class PaymentRequest {
    private final Amount amount;
    private final String description;
    private final Receipt receipt;
    private final boolean capture = true;
    private final Confirmation confirmation = new Confirmation();
    private final Metadata metadata = new Metadata();

    public static Object create(double amount, String description, String userEmail) {
        return new PaymentRequest(
                new Amount(amount),
                description,
                new Receipt(new Customer(userEmail), List.of(new Item(new Amount(amount))))
        );
    }
}

@Data
class Confirmation {
    private final String type = "redirect";
    private final String return_url = "https://t.me/m004kavpn_bot";
}

@Data
class Metadata {
    private final String order_id = UUID.randomUUID().toString();
}

