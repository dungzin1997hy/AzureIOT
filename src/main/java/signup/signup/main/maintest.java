package signup.signup.main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import signup.signup.dao.ServerDAO;
import signup.signup.entity.Servers;


import java.io.IOException;
@Component
public class maintest {

    @Autowired
    ServerDAO serverDAO;
    public void readHtml() throws IOException {
        org.jsoup.nodes.Document doc = Jsoup.connect("https://williamyaps.github.io/wlmjavascript/servercli.html#").maxBodySize(0).get();
        org.jsoup.select.Elements rows = doc.select("tr");
        System.out.println(rows.size());
        for(org.jsoup.nodes.Element row :rows)
        {
            org.jsoup.select.Elements columns = row.select("td");
            if(columns.size()==0){
                continue;
            }
            String country = columns.get(0).text();
            String city = columns.get(1).text();
            String provider = columns.get(2).text();
            String host = columns.get(3).text();
            String id = columns.get(4).text();
            Servers servers = new Servers(country,city,provider,host,id);
            serverDAO.addServer(servers);
        }

    }
}
