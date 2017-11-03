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




public class PageCrawler {

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

        // get base URL
        String baseUrl = url.toExternalForm();

        // iterate through all links on page and add to linksOnPage
        Elements elements = page.select("a");
        for(Element element : elements){
//            UrlNormalizer.normalize(linkUrl, baseUrl);
            linksOnPage.add(element.attr("abs:href"));
        }

        // return array of links on page
        return linksOnPage;
    }

}
