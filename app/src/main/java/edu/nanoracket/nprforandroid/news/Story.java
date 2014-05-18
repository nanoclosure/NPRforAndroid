package edu.nanoracket.nprforandroid.news;

import java.util.List;
import java.util.Map;

public class Story {

    private String id;
    private String title;
    private String teaser;
    private String slug;
    private String pubDate;
    private Byline byline = null;
    private Image image = null;
    private Text text = null;
    private TextWithHtml textWithHtml = null;
    private Audio audio;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public Byline getByline() {
        return byline;
    }

    public void setByline(Byline byline) {
        this.byline = byline;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public TextWithHtml getTextWithHtml() {
        return textWithHtml;
    }

    public void setTextWithHtml(TextWithHtml textWithHtml) {
        this.textWithHtml = textWithHtml;
    }

    public static class Byline {
        private final String name;


        public Byline(String name) {
            this.name = name;

        }

        public String getName() {
            return name;
        }


    }

    public static class Audio {

        private final String id;
        private final String type;
        private final String duration;
        private final List<Format> formats;

        public static class Format {
            private final String mp3;
            private final String wm;
            private final String rm;

            public Format(String mp3, String wm, String rm) {
                this.mp3 = mp3;
                this.wm = wm;
                this.rm = rm;
            }

            public String getMp3() {
                return mp3;
            }

            @SuppressWarnings("unused")
            public String getWm() {
                return wm;
            }

            @SuppressWarnings("unused")
            public String getRm() {
                return rm;
            }
        }

        public Audio(String id, String primary, String duration, List<Format> formats) {
            this.id = id;
            this.type = primary;
            this.duration = duration;
            this.formats = formats;
        }

        public List<Format> getFormats() {
            return formats;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getDuration() {
            return duration;
        }
    }

    public static class Image {
        @SuppressWarnings("unused")
        private final String id;
        private final String type;
        @SuppressWarnings("unused")
        private final String width;
        private final String src;
        @SuppressWarnings("unused")
        private final String hasBorder;
        //private final String caption;


        public Image(String id, String type, String width, String src, String hasBorder) {
            this.id = id;
            this.type = type;
            this.width = width;
            this.src = src;
            this.hasBorder = hasBorder;
            //this.caption = caption;
        }

        public String getSrc() {
            return src;
        }

        /*public String getCaption() {
            return caption;
        }*/

        public String getType() {
            return type;
        }
    }

    public static class Text {
        private final Map<Integer, String> paragraphs;

        public Text(Map<Integer, String> paragraphs) {
            this.paragraphs = paragraphs;
        }

        @SuppressWarnings("unused")
        public Map<Integer, String> getParagraphs() {
            return paragraphs;
        }
    }

    public static class TextWithHtml {
        private final Map<Integer, String> paragraphs;

        public TextWithHtml(Map<Integer, String> paragraphs) {
            this.paragraphs = paragraphs;
        }

        public Map<Integer, String> getParagraphs() {
            return paragraphs;
        }
    }

}
