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

package edu.uci.ics.hyracks.storage.am.lsm.rtree.dataflow;

import java.util.Map;

import edu.uci.ics.hyracks.api.context.IHyracksTaskContext;
import edu.uci.ics.hyracks.api.dataflow.value.IBinaryComparatorFactory;
import edu.uci.ics.hyracks.api.dataflow.value.ILinearizeComparatorFactory;
import edu.uci.ics.hyracks.storage.am.common.api.IPrimitiveValueProviderFactory;
import edu.uci.ics.hyracks.storage.am.common.dataflow.IIndexOperatorDescriptor;
import edu.uci.ics.hyracks.storage.am.common.dataflow.IndexDataflowHelper;
import edu.uci.ics.hyracks.storage.am.lsm.common.api.ILSMIOOperationCallbackFactory;
import edu.uci.ics.hyracks.storage.am.lsm.common.api.ILSMIOOperationSchedulerProvider;
import edu.uci.ics.hyracks.storage.am.lsm.common.api.ILSMMergePolicyFactory;
import edu.uci.ics.hyracks.storage.am.lsm.common.api.ILSMOperationTrackerProvider;
import edu.uci.ics.hyracks.storage.am.lsm.common.api.IVirtualBufferCacheProvider;
import edu.uci.ics.hyracks.storage.am.lsm.common.dataflow.AbstractLSMIndexDataflowHelperFactory;
import edu.uci.ics.hyracks.storage.am.rtree.frames.RTreePolicyType;

public class LSMRTreeDataflowHelperFactory extends AbstractLSMIndexDataflowHelperFactory {

    private static final long serialVersionUID = 1L;

    private final IBinaryComparatorFactory[] btreeComparatorFactories;
    private final IPrimitiveValueProviderFactory[] valueProviderFactories;
    private final RTreePolicyType rtreePolicyType;
    private final ILinearizeComparatorFactory linearizeCmpFactory;
    private final int[] btreeFields;

    public LSMRTreeDataflowHelperFactory(IPrimitiveValueProviderFactory[] valueProviderFactories,
            RTreePolicyType rtreePolicyType, IBinaryComparatorFactory[] btreeComparatorFactories,
            IVirtualBufferCacheProvider virtualBufferCacheProvider, ILSMMergePolicyFactory mergePolicyFactory,
            Map<String, String> mergePolicyProperties, ILSMOperationTrackerProvider opTrackerFactory,
            ILSMIOOperationSchedulerProvider ioSchedulerProvider, ILSMIOOperationCallbackFactory ioOpCallbackFactory,
            ILinearizeComparatorFactory linearizeCmpFactory, double bloomFilterFalsePositiveRate, int[] btreeFields) {
        super(virtualBufferCacheProvider, mergePolicyFactory, mergePolicyProperties, opTrackerFactory,
                ioSchedulerProvider, ioOpCallbackFactory, bloomFilterFalsePositiveRate);
        this.btreeComparatorFactories = btreeComparatorFactories;
        this.valueProviderFactories = valueProviderFactories;
        this.rtreePolicyType = rtreePolicyType;
        this.linearizeCmpFactory = linearizeCmpFactory;
        this.btreeFields = btreeFields;
    }

    @Override
    public IndexDataflowHelper createIndexDataflowHelper(IIndexOperatorDescriptor opDesc, IHyracksTaskContext ctx,
            int partition) {
        return new LSMRTreeDataflowHelper(opDesc, ctx, partition,
                virtualBufferCacheProvider.getVirtualBufferCaches(ctx), bloomFilterFalsePositiveRate,
                btreeComparatorFactories, valueProviderFactories, rtreePolicyType,
                mergePolicyFactory.createMergePolicy(mergePolicyProperties), opTrackerFactory,
                ioSchedulerProvider.getIOScheduler(ctx), ioOpCallbackFactory, linearizeCmpFactory, btreeFields);
    }
}
