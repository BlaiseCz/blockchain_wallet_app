package com.wallet.blockchain_wallet.client.wallet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class HostInfo {
    private String address;
    private int port;
}
