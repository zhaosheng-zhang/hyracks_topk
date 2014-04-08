/*
 * Copyright 2009-2013 by The Regents of the University of California
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License from
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.uci.ics.pregelix.runtime.touchpoint;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;

import edu.uci.ics.hyracks.api.dataflow.value.ISerializerDeserializer;
import edu.uci.ics.pregelix.dataflow.std.base.ISerializerDeserializerFactory;

public class WritableSerializerDeserializerFactory<T extends Writable> implements ISerializerDeserializerFactory<T> {
    private static final long serialVersionUID = 1L;
    private final Class<T> clazz;

    public WritableSerializerDeserializerFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public ISerializerDeserializer getSerializerDeserializer(Configuration conf) {
        return DatatypeHelper.createSerializerDeserializer(clazz, conf);
    }
}
