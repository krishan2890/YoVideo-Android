package com.inspius.canyon.yo_video.app;

/**
 * Created by Billy on 11/17/2015.
 */
public class AppEnum {
    /**
     * ENUM---START-----------
     */

    public enum VIDEO_TYPE {
        UPLOAD("UPLOAD"), YOUTUBE("YOUTUBE"), VIMEO("VIMEO"),MP3("MP3"), FACEBOOK("FACEBOOK"), DAILY_MOTION("DAILY_MOTION");

        private final String text;

        private VIDEO_TYPE(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum LOGIN_TYPE {
        NOT_LOGIN, SYSTEM, FACEBOOK;
    }

    public enum HOME_TYPE {
        MOST_VIEW("most-view"), LATEST("latest");

        private final String text;

        private HOME_TYPE(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    /**
     * ENUM---END-------------
     */
}
