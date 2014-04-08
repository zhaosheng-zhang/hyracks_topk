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

package edu.uci.ics.pregelix.api.io;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * Use this to load data for a BSP application. Note that the InputSplit must
 * also implement Writable. The InputSplits will determine the partitioning of
 * vertices across the mappers, so keep that in consideration when implementing
 * getSplits().
 * 
 * @param <I>
 *            Vertex id
 * @param <V>
 *            Vertex value
 * @param <E>
 *            Edge value
 * @param <M>
 *            Message data
 */
@SuppressWarnings("rawtypes")
public abstract class VertexInputFormat<I extends WritableComparable, V extends Writable, E extends Writable, M extends WritableSizable> {
    /**
     * Logically split the vertices for a graph processing application.
     * Each {@link InputSplit} is then assigned to a worker for processing.
     * <p>
     * <i>Note</i>: The split is a <i>logical</i> split of the inputs and the input files are not physically split into chunks. For e.g. a split could be <i>&lt;input-file-path, start, offset&gt;</i> tuple. The InputFormat also creates the {@link VertexReader} to read the {@link InputSplit}. Also, the number of workers is a hint given to the developer to try to intelligently determine how many splits to create (if this is adjustable) at runtime.
     * 
     * @param context
     *            Context of the job
     * @param numWorkers
     *            Number of workers used for this job
     * @return an array of {@link InputSplit}s for the job.
     */
    public abstract List<InputSplit> getSplits(JobContext context, int numWorkers) throws IOException,
            InterruptedException;

    /**
     * Create a vertex reader for a given split. The framework will call {@link VertexReader#initialize(InputSplit, TaskAttemptContext)} before
     * the split is used.
     * 
     * @param split
     *            the split to be read
     * @param context
     *            the information about the task
     * @return a new record reader
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract VertexReader<I, V, E, M> createVertexReader(InputSplit split, TaskAttemptContext context)
            throws IOException;
}
