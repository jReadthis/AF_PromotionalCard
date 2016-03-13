package com.example.nano1.af_promotionalcard;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by nano1 on 3/11/2016.
 */
public class Promotion {

    /**
     * button : {"target":"https://m.abercrombie.com","title":"Shop Now"}
     * description : GET READY FOR SUMMER DAYS
     * footer : In stores & online. Exclusions apply. <a href="https://www.abercrombie.com/anf/media/legalText/viewDetailsText20150618_Shorts25_US.html" class="legal promo-details">See details</a>
     * image : http://anf.scene7.com/is/image/anf/anf-US-20150629-app-women-shorts
     * title : Shorts Starting at $25
     */

    private List<PromotionsEntity> promotions;

    public List<PromotionsEntity> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionsEntity> promotions) {
        this.promotions = promotions;
    }

    public static class PromotionsEntity implements Parcelable {
        public static final Creator<PromotionsEntity> CREATOR = new Creator<PromotionsEntity>() {
            @Override
            public PromotionsEntity createFromParcel(Parcel in) {
                return new PromotionsEntity(in);
            }

            @Override
            public PromotionsEntity[] newArray(int size) {
                return new PromotionsEntity[size];
            }
        };
        /**
         * target : https://m.abercrombie.com
         * title : Shop Now
         */

        private List<ButtonEntity> button;
        private String description;
        private String footer;
        private String image;
        private String title;

        public PromotionsEntity(List<ButtonEntity> button, String description, @Nullable String footer, String image, String title) {
            this.button = button;
            this.description = description;
            this.footer = footer;
            this.image = image;
            this.title = title;
        }

        protected PromotionsEntity(Parcel in) {
            description = in.readString();
            footer = in.readString();
            image = in.readString();
            title = in.readString();
        }

        public List<ButtonEntity> getButton() {
            return button;
        }

        public void setButton(List<ButtonEntity> button) {
            this.button = button;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFooter() {
            return footer;
        }

        public void setFooter(String footer) {
            this.footer = footer;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(description);
            dest.writeString(footer);
            dest.writeString(image);
            dest.writeString(title);
        }
    }
}

