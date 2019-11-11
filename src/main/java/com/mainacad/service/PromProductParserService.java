package com.mainacad.service;

import com.mainacad.model.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Logger;

public class PromProductParserService extends Thread {

    private static final Logger LOG =
            Logger.getLogger(PromProductParserService.class.getName());

    private final List<Item> items;
    private final String url;

    public PromProductParserService(List<Item> items, String url) {
        this.items = items;
        this.url = url;
    }

    @Override
    public void run() {
        try {
            Document document = Jsoup.connect(url).get();
            Element productInfo =
                    document.getElementsByAttributeValue
                            ("data-qaid", "main_product_info").first();

            String itemId = extractItemId(productInfo);
            String name = extractName(productInfo);
            BigDecimal price = extractPrice(productInfo);
            BigDecimal initPrice = extractInitPrice(productInfo, price);
            String imageUrl = extractImageUrl(document);
            String availability = extractAvailability(productInfo);

            Item item = new Item(itemId, name, url, imageUrl, price, initPrice, availability);

            items.add(item);
        } catch (IOException e) {
            LOG.severe(String.format("Item by URL %s was not extracted", url));
        }

    }

    private String extractItemId(Element productInfo) {
        String result = " ";
        try {
            result = productInfo.
                    getElementsByAttributeValue("data-qaid", "product-sku").
                    first().text();
        } catch (Exception e) {
            LOG.severe(String.format("Item id by URL %s was not extracted", url));
        }
        return result;
    }

    private String extractName(Element productInfo) {
        String result = " ";
        try {
            result = productInfo.
                    getElementsByAttributeValue("data-qaid", "product_name").
                    first().text();
        } catch (Exception e) {
            LOG.severe(String.format("Item name by URL %s was not extracted", url));
        }
        return result;
    }

    private String extractAvailability(Element productInfo) {
        String result = " ";
        try {
            result = productInfo.
                    getElementsByAttributeValue("data-qaid", "product_presence").
                    first().text();
        } catch (Exception e) {
            LOG.severe(String.format("Item availability by URL %s was not extracted", url));
        }
        return result;
    }

    private String extractImageUrl(Document productInfo) {
        String result = " ";
        try {
            result = productInfo.
                    getElementsByAttributeValue("property", "og:image").
                    attr("content");
        } catch (Exception e) {
            LOG.severe(String.format("Item image url by URL %s was not extracted", url));
        }
        return result;
    }

    private BigDecimal extractPrice(Element productInfo) {
        BigDecimal result = null;
        try {
            String resultAsText = productInfo.
                    getElementsByAttributeValue("data-qaid", "product_price").
                    first().attr("data-qaprice");
            result = new BigDecimal (resultAsText).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            LOG.severe(String.format("Item price by URL %s was not extracted", url));
        }
        return result;
    }

    private BigDecimal extractInitPrice(Element productInfo, BigDecimal price) {
        BigDecimal result = price;
        try {
            String resultAsText = productInfo.
                    getElementsByAttributeValue("data-qaid", "price_without_discount").
                    first().attr("data-qaprice");
            result = new BigDecimal (resultAsText).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            LOG.severe(String.format("Item init price by URL %s was not extracted", url));
        }
        return result;
    }

}
