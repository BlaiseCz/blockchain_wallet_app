package com.wallet.blockchain_wallet.client.protocol.header;

import com.wallet.blockchain_wallet.client.wallet.HostInfo;
import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class NodesResponse implements HeaderProcessor {

    @SneakyThrows
    @Override
    public List<String> proceedData(String data) {
        List<String> response = new ArrayList<>();
        String header = data.substring(0, 3);

        if (header.equals("NS[")) {

            String stringJson = data.substring(3);
            JSONArray jsonArray = new JSONArray(stringJson);

            for (int i = 0; i < jsonArray.length(); i++) {
                response.add(jsonArray.getString(i));
            }

            return response;

        }

        return response;
    }

    @SneakyThrows
    public List<HostInfo> hostInfoListFromData(String data) {
        List<HostInfo> response = new ArrayList<>();
        String header = data.substring(0, 3);

        if (header.equals("NS[")) {

            String stringJson = data.substring(3);
            JSONArray jsonArray = new JSONArray(stringJson);

            for (int i = 0; i < jsonArray.length(); i++) {
                int port = (int) jsonArray.getJSONObject(i).get("port");
                String host = String.valueOf(jsonArray.getJSONObject(i).get("ip"));
                HostInfo bufferHostInfo = new HostInfo(host, port);
                response.add(bufferHostInfo);
            }

            return response;
        }

        return response;
    }

}
