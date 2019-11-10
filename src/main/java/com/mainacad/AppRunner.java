package com.mainacad;

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
        String url = "https://prom.ua/p999585650-noutbuk-omen-dc0007ua.html";

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
            LOG.info(items.get(0).getAvailability());
            LOG.info(items.get(0).getImageUrl());
            LOG.info(String.valueOf(items.get(0).getPrice()));
            LOG.info(String.valueOf(items.get(0).getInitialPrice()));
        }

    }
}
