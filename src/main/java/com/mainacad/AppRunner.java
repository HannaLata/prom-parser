package com.mainacad;

import com.mainacad.model.Item;
import com.mainacad.service.PromProductParserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class AppRunner {

    private static final Logger LOG = Logger.getLogger(AppRunner.class.getName());

    public static void main(String[] args)
    {

        List<Item> items = Collections.synchronizedList(new ArrayList<>());
        String url = "https://prom.ua/p991728628-noutbuk-probook-440.html";

        PromProductParserService promProductParserService =
                new PromProductParserService(items, url);

        promProductParserService.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!items.isEmpty()) {
            LOG.info(items.get(0).getItemId());
            LOG.info(items.get(0).getName());
            LOG.info(items.get(0).getUrl());
            LOG.info(items.get(0).getAvailability());
            LOG.info(items.get(0).getImageUrl());
            LOG.info(items.get(0).getPrice().toString());
            LOG.info(items.get(0).getInitialPrice().toString());
        }

    }
}
