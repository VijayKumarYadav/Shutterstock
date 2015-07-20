/**
 * 
 */
package com.example.shutterstock;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Model for response
 * 
 * @author VijayK
 * 
 */
public class ResponseModel {

    @SerializedName("page")
    private String page;

    @SerializedName("per_page")
    private String perPage;

    @SerializedName("total_count")
    private String totalCount;

    @SerializedName("data")
    private List<Data> data;

    public static class Data {

        private String id;

        @SerializedName("assets")
        private Assets assets;

        public static class Assets {

            @SerializedName("large_thumb")
            private LargeThumb largeThumb;

            public static class LargeThumb {
                private String url;

                /**
                 * @return the url
                 */
                public String getUrl() {
                    return url;
                }

                /**
                 * @param url the url to set
                 */
                public void setUrl(final String url) {
                    this.url = url;
                }
            }

            /**
             * @return the smallThumb
             */
            public LargeThumb getLargeThumb() {
                return largeThumb;
            }

            /**
             * @param smallThumb the smallThumb to set
             */
            public void setLargeThumb(final LargeThumb smallThumb) {
                this.largeThumb = smallThumb;
            }
        }

        /**
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(final String id) {
            this.id = id;
        }

        /**
         * @return the assets
         */
        public Assets getAssets() {
            return assets;
        }

        /**
         * @param assets the assets to set
         */
        public void setAssets(final Assets assets) {
            this.assets = assets;
        }
    }

    /**
     * @return the data
     */
    public List<Data> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(final List<Data> data) {
        this.data = data;
    }
}
