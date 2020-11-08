package com.wallet.blockchain_wallet.client.protocol.header.response;

import com.wallet.blockchain_wallet.client.wallet.HostInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class NodesResponseProcessor implements NodesResponse {

    @Override
    @SneakyThrows
    public List<HostInfo> hostInfoListFromData(String data) {
        List<HostInfo> response = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(data);

        for (int i = 0; i < jsonArray.length(); i++) {
            int port = (int) jsonArray.getJSONObject(i).get("port");
            String host = String.valueOf(jsonArray.getJSONObject(i).get("ip"));
            HostInfo bufferHostInfo = new HostInfo(host, port);
            response.add(bufferHostInfo);
        }

        log.info("Received HostInfoList {}", response.toString());
        return response;
    }

}
