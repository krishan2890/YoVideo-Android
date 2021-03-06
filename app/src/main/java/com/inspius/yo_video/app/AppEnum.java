package com.inspius.yo_video.app;

/**
 * Created by Billy on 11/17/2015.
 */
public class AppEnum {
    /**
     * ENUM---START-----------
     */

    public enum VIDEO_TYPE {
        NONE("NONE"), UPLOAD("UPLOAD"), YOUTUBE("YOUTUBE"), VIMEO("VIMEO"), MP3("MP3"), FACEBOOK("FACEBOOK"), DAILY_MOTION("DAILY_MOTION"), JW_PLAYER("JW_PLAYER");

        private final String text;

        private VIDEO_TYPE(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

        public static VIDEO_TYPE fromString(String text) {
            if (text != null) {
                for (VIDEO_TYPE b : VIDEO_TYPE.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                        return b;
                    }
                }
            }
            return NONE;
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

    public enum VIDEO_DETAIL_SCREEN {
        DEFAULT("default"), PLUGIN_DETAIL_JW("plugin-detail-jw");

        private final String text;

        private VIDEO_DETAIL_SCREEN(final String text) {
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
