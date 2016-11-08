/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * <p/>
 * Run it as a Java application (not Android).
 *
 * @author Markus
 */
public class ExampleDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(2, "com.inspius.canyon.yo_video.greendao");

        addWishList(schema);
        addRecentList(schema);
        addRecentSearch(schema);
        addNotifications(schema);
        addDownloadList(schema);

        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }

    private static void addWishList(Schema schema) {
        Entity note = schema.addEntity("DBWishListVideo");
        note.addIdProperty();
        note.addIntProperty("videoId").notNull();
        note.addStringProperty("category").notNull();
        note.addStringProperty("series").notNull();
        note.addStringProperty("view").notNull();
        note.addStringProperty("image").notNull();
        note.addStringProperty("link").notNull();
        note.addStringProperty("name").notNull();
        note.addIntProperty("userID").notNull();
    }

    private static void addDownloadList(Schema schema) {
        Entity note = schema.addEntity("DBVideoDownload");
        note.addIdProperty();
        note.addIntProperty("videoId").notNull();
        note.addStringProperty("path").notNull();
        note.addStringProperty("title").notNull();
    }

    private static void addRecentList(Schema schema) {
        Entity note = schema.addEntity("DBRecentVideo");
        note.addIdProperty();
        note.addIntProperty("videoId").notNull();
        note.addStringProperty("category").notNull();
        note.addStringProperty("series").notNull();
        note.addStringProperty("view").notNull();
        note.addStringProperty("image").notNull();
        note.addStringProperty("link").notNull();
        note.addStringProperty("name").notNull();
        note.addIntProperty("userID").notNull();
    }

    private static void addRecentSearch(Schema schema) {
        Entity note = schema.addEntity("DBKeywordSearch");
        note.addIdProperty();
        note.addStringProperty("keyword").notNull();
    }

    private static void addNotifications(Schema schema) {
        Entity note = schema.addEntity("DBNotification");
        note.addIdProperty();
        note.addStringProperty("title").notNull();
        note.addStringProperty("message").notNull();
        note.addStringProperty("content").notNull();
        note.addIntProperty("status").notNull();
    }
}
