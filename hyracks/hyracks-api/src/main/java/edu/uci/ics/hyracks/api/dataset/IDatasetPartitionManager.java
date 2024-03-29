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
package edu.uci.ics.hyracks.api.dataset;

import edu.uci.ics.hyracks.api.comm.IFrameWriter;
import edu.uci.ics.hyracks.api.context.IHyracksTaskContext;
import edu.uci.ics.hyracks.api.exceptions.HyracksException;
import edu.uci.ics.hyracks.api.io.IWorkspaceFileFactory;
import edu.uci.ics.hyracks.api.job.JobId;

public interface IDatasetPartitionManager extends IDatasetManager {
    public IFrameWriter createDatasetPartitionWriter(IHyracksTaskContext ctx, ResultSetId rsId, boolean orderedResult,
            boolean asyncMode, int partition, int nPartitions) throws HyracksException;

    public void reportPartitionWriteCompletion(JobId jobId, ResultSetId resultSetId, int partition)
            throws HyracksException;

    public void reportPartitionFailure(JobId jobId, ResultSetId resultSetId, int partition) throws HyracksException;

    public void initializeDatasetPartitionReader(JobId jobId, ResultSetId resultSetId, int partition, IFrameWriter noc)
            throws HyracksException;

    public void removePartition(JobId jobId, ResultSetId resultSetId, int partition);

    public void abortReader(JobId jobId);

    public IWorkspaceFileFactory getFileFactory();

    public void close();
}
