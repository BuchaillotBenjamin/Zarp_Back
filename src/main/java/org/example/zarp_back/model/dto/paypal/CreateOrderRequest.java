package org.example.zarp_back.model.dto.paypal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    private String amount;
    private String currency;
    private String sellerPayerId;

}
