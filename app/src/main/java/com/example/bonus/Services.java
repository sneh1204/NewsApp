package com.example.bonus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Services {

    public static class Articles {

        ArrayList<News> news = new ArrayList<>();

        public Articles(JSONObject output) {
            try {
                JSONArray news = output.getJSONArray("articles");
                for (int i = 0; i < news.length(); i++) {
                    this.news.add(new News(news.getJSONObject(i)));
                }
            } catch (JSONException exc) {
                exc.printStackTrace();
            }
        }

    }

    public static class News {

        public String author, title, description, url, content, urlToImage, pub_date;

        public News(JSONObject news) {
            try {
                this.author = news.getString("author").replace("\n", "");
                this.title = news.getString("title").replace("\n", "");
                this.description = news.getString("description").replace("\n", "");
                this.url = news.getString("url");
                this.urlToImage = news.getString("urlToImage");
                this.content = news.getString("content").replace("\n", "");
                this.pub_date = news.getString("publishedAt");
            } catch (JSONException exc) {
                exc.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "News{" +
                    "author='" + author + '\'' +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", url='" + url + '\'' +
                    ", content='" + content + '\'' +
                    ", urlToImage='" + urlToImage + '\'' +
                    ", pub_date='" + pub_date + '\'' +
                    '}';
        }
    }

    public static class NewsSource {

        ArrayList<Source> sources = new ArrayList<>();

        public NewsSource(JSONObject output) {
            try {
                JSONArray sources = output.getJSONArray("sources");
                for (int i = 0; i < sources.length(); i++) {
                    this.sources.add(new Source(sources.getJSONObject(i)));
                }
            } catch (JSONException exc) {
                exc.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "Services{" +
                    "sources=" + sources +
                    '}';
        }

    }

    public static class Source{

        public String id, name, description, url, category, language, country;

        public Source(JSONObject source){
            try{
                this.id = source.getString("id");
                this.name = source.getString("name");
                this.description = source.getString("description");
                this.url = source.getString("url");
                this.category = source.getString("category");
                this.language = source.getString("language");
                this.country = source.getString("country");
            }catch (JSONException exc){
                exc.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "Source{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", url='" + url + '\'' +
                    ", category='" + category + '\'' +
                    ", language='" + language + '\'' +
                    ", country='" + country + '\'' +
                    '}';
        }
    }

}
