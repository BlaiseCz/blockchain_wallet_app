package com.wallet.blockchain_wallet.client.protocol.header.response;

import com.wallet.blockchain_wallet.client.wallet.HostInfo;

import java.util.List;

interface NodesResponse {
    List<HostInfo> hostInfoListFromData(String data);
}
