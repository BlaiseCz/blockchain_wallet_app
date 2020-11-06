package com.wallet.blockchain_wallet.client.protocol.header;


import com.wallet.blockchain_wallet.client.wallet.HostInfo;

import java.util.List;

public interface HeaderProcessor {
    List<HostInfo> hostInfoListFromData(String data);
}
