import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class PageCrawler {

    private static final Logger logger = Logger.getLogger(SiteCrawler.class.getName());

    public List<String> getLinks(String link) {

        // initialise array of links on page
        List<String> linksOnPage = new ArrayList<>();

        // declare URL, corresponding connection and input stream
        URL url;
        URLConnection urlConnection;
        InputStream input;

        // initialise URL
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            System.out.println("Malformed link: " + link);
            return new ArrayList<>();
        }

        // open connection
        try {
            urlConnection = url.openConnection();
        } catch (IOException e) {
            System.out.println("Failed to open connection: " + link);
            return new ArrayList<>();
        }

        // initialise input stream
        try {
            input = urlConnection.getInputStream();
        } catch (IOException e) {
            System.out.println("Failed to get input stream: " + link);
            return new ArrayList<>();
        }

        // parse document
        Document page = null;
        try {
            page = Jsoup.parse(input, "UTF-8", "");
        } catch (IOException e) {
            System.out.println("Failed to parse page: " + link);
            return new ArrayList<>();
        }

        // iterate through all links on page and add to linksOnPage array
        Elements elements = page.select("a");
        for(Element element : elements){
            String rawLink = element.attr("abs:href");  // get link on page
            if (rawLink.isEmpty()) {
                continue;  // skip if dead link
            }
            logger.info("Considering " + rawLink);  // otherwise consider the link
            try {
                URL rawURL = new URL(rawLink);
                if (inside(url, rawURL)) {  // stay on same website
                    logger.info(rawURL + " is inside the domain of " + url.getHost() + "; adding to queue");
                    linksOnPage.add(normaliseURL(rawURL));  // add to linksOnPage array
                } else {
                    logger.info(rawURL + " is outside the domain of " + url.getHost());
                }
            } catch (MalformedURLException e) {
                logger.info("Malformed URL: " + rawLink);
            }
        }


        // return array of links on page
        return linksOnPage;
    }

    private boolean inside(URL host, URL link) {
        return normaliseHostName(link.getHost()).contains(normaliseHostName(host.getHost()));
    }

    private String normaliseHostName(String link) {
        return link.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","");
    }

    private String normaliseURL(URL url) {
        return url.getProtocol() + "://" + normaliseHostName(url.getHost()) + url.getPath();
    }

}
