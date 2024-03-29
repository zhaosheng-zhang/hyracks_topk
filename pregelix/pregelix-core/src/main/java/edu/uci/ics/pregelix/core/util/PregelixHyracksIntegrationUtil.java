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
package edu.uci.ics.pregelix.core.util;

import java.io.File;
import java.util.EnumSet;

import org.apache.commons.io.FileUtils;

import edu.uci.ics.hyracks.api.client.HyracksConnection;
import edu.uci.ics.hyracks.api.client.IHyracksClientConnection;
import edu.uci.ics.hyracks.api.job.JobFlag;
import edu.uci.ics.hyracks.api.job.JobId;
import edu.uci.ics.hyracks.api.job.JobSpecification;
import edu.uci.ics.hyracks.control.cc.ClusterControllerService;
import edu.uci.ics.hyracks.control.common.controllers.CCConfig;
import edu.uci.ics.hyracks.control.common.controllers.NCConfig;
import edu.uci.ics.hyracks.control.nc.NodeControllerService;
import edu.uci.ics.pregelix.core.jobgen.clusterconfig.ClusterConfig;
import edu.uci.ics.pregelix.runtime.bootstrap.NCApplicationEntryPoint;

public class PregelixHyracksIntegrationUtil {

    public static final String NC1_ID = "nc1";
    public static final String NC2_ID = "nc2";

    public static final int DEFAULT_HYRACKS_CC_PORT = 1099;
    public static final int TEST_HYRACKS_CC_PORT = 1099;
    public static final int TEST_HYRACKS_CC_CLIENT_PORT = 2099;
    public static final String CC_HOST = "localhost";

    public static final int FRAME_SIZE = 65536;

    private static ClusterControllerService cc;
    private static NodeControllerService nc1;
    private static NodeControllerService nc2;
    private static IHyracksClientConnection hcc;

    public static void init() throws Exception {
        FileUtils.forceMkdir(new File("dev1"));
        FileUtils.forceMkdir(new File("dev2"));
        FileUtils.forceMkdir(new File("dev3"));
        FileUtils.forceMkdir(new File("dev4"));
        CCConfig ccConfig = new CCConfig();
        ccConfig.clientNetIpAddress = CC_HOST;
        ccConfig.clusterNetIpAddress = CC_HOST;
        ccConfig.clusterNetPort = TEST_HYRACKS_CC_PORT;
        ccConfig.clientNetPort = TEST_HYRACKS_CC_CLIENT_PORT;
        ccConfig.defaultMaxJobAttempts = 0;
        ccConfig.jobHistorySize = 1;
        ccConfig.profileDumpPeriod = -1;
        ccConfig.heartbeatPeriod = 50;
        ccConfig.maxHeartbeatLapsePeriods = 10;

        // cluster controller
        cc = new ClusterControllerService(ccConfig);
        cc.start();

        // two node controllers
        NCConfig ncConfig1 = new NCConfig();
        ncConfig1.ccHost = "localhost";
        ncConfig1.clusterNetIPAddress = "localhost";
        ncConfig1.ccPort = TEST_HYRACKS_CC_PORT;
        ncConfig1.dataIPAddress = "127.0.0.1";
        ncConfig1.datasetIPAddress = "127.0.0.1";
        ncConfig1.nodeId = NC1_ID;
        ncConfig1.ioDevices = "dev1,dev2";
        ncConfig1.appNCMainClass = NCApplicationEntryPoint.class.getName();
        nc1 = new NodeControllerService(ncConfig1);
        nc1.start();

        NCConfig ncConfig2 = new NCConfig();
        ncConfig2.ccHost = "localhost";
        ncConfig2.clusterNetIPAddress = "localhost";
        ncConfig2.ccPort = TEST_HYRACKS_CC_PORT;
        ncConfig2.dataIPAddress = "127.0.0.1";
        ncConfig2.datasetIPAddress = "127.0.0.1";
        ncConfig2.nodeId = NC2_ID;
        ncConfig2.appNCMainClass = NCApplicationEntryPoint.class.getName();
        ncConfig2.ioDevices = "dev3,dev4";
        nc2 = new NodeControllerService(ncConfig2);
        nc2.start();

        // hyracks connection
        hcc = new HyracksConnection(CC_HOST, TEST_HYRACKS_CC_CLIENT_PORT);
        ClusterConfig.loadClusterConfig(CC_HOST, TEST_HYRACKS_CC_CLIENT_PORT);
    }

    public static void startNC1() throws Exception {
        nc1.start();
    }

    public static void shutdownNC1() throws Exception {
        nc1.stop();
    }

    public static void shutdownNC2() throws Exception {
        nc2.stop();
    }

    public static void shutdownCC() throws Exception {
        cc.stop();
    }

    public static void deinit() throws Exception {
        nc2.stop();
        nc1.stop();
        cc.stop();
    }

    public static void runJob(JobSpecification spec, String appName) throws Exception {
        spec.setFrameSize(FRAME_SIZE);
        JobId jobId = hcc.startJob(spec, EnumSet.of(JobFlag.PROFILE_RUNTIME));
        hcc.waitForCompletion(jobId);
    }

}
