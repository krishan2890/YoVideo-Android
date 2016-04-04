package com.inspius.canyon.yo_video.app;

/**
 * Created by Billy on 11/17/2015.
 */
public class AppEnum {
    /**
     * ENUM---START-----------
     */

    public enum VIDEO_TYPE {
        UPLOAD("UPLOAD"), YOUTUBE("YOUTUBE"),VIMEO("VIMEO"),;

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

    /**
     * ENUM---END-------------
     */
}
