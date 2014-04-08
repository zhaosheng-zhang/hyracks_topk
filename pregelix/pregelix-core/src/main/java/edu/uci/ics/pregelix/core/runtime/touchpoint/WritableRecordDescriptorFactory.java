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
package edu.uci.ics.pregelix.core.runtime.touchpoint;

import org.apache.hadoop.conf.Configuration;

import edu.uci.ics.hyracks.api.context.IHyracksTaskContext;
import edu.uci.ics.hyracks.api.dataflow.value.RecordDescriptor;
import edu.uci.ics.hyracks.api.exceptions.HyracksDataException;
import edu.uci.ics.hyracks.api.exceptions.HyracksException;
import edu.uci.ics.pregelix.core.hadoop.config.ConfigurationFactory;
import edu.uci.ics.pregelix.core.util.DataflowUtils;
import edu.uci.ics.pregelix.dataflow.base.IConfigurationFactory;
import edu.uci.ics.pregelix.dataflow.std.base.IRecordDescriptorFactory;

public class WritableRecordDescriptorFactory implements IRecordDescriptorFactory {
    private static final long serialVersionUID = 1L;
    private String[] fieldClasses;
    private IConfigurationFactory confFactory;

    public WritableRecordDescriptorFactory(Configuration conf, String... fieldClasses) {
        this.fieldClasses = fieldClasses;
        this.confFactory = new ConfigurationFactory(conf);
    }

    @Override
    public RecordDescriptor createRecordDescriptor(IHyracksTaskContext ctx) throws HyracksDataException {
        try {
            Configuration conf = confFactory.createConfiguration(ctx);
            return DataflowUtils.getRecordDescriptorFromWritableClasses(ctx, conf, fieldClasses);
        } catch (HyracksException e) {
            throw new HyracksDataException(e);
        }
    }

}
