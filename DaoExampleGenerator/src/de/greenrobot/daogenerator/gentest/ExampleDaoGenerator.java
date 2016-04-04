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
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * 
 * Run it as a Java application (not Android).
 * 
 * @author Markus
 */
public class ExampleDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.inspius.canyon.yo_video.greendao");

        addWishList(schema);
        addRecentList(schema);

        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }

    private static void addWishList(Schema schema) {
        Entity note = schema.addEntity("WishList");
        note.addIdProperty();
        note.addLongProperty("videoId").notNull();
        note.addStringProperty("name");
    }

    private static void addRecentList(Schema schema) {
        Entity note = schema.addEntity("RecentVideos");
        note.addIdProperty();
        note.addLongProperty("videoId").notNull();
        note.addStringProperty("name");
    }
}