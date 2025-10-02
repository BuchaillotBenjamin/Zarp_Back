package org.example.zarp_back.model.dto.paypal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaptureOrderRequest {
    private String sellerPayerId;

}